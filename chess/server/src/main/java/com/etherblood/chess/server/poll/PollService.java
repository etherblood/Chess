package com.etherblood.chess.server.poll;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.server.poll.model.PollClient;
import com.etherblood.chess.server.user.authentication.UserContextService;

/**
 *
 * @author Philipp
 */
@Service
public class PollService {

    private final static Logger LOG = LoggerFactory.getLogger(PollService.class);
    private final UserContextService userContextService;
    private final Map<Integer, PollClient> clients = new ConcurrentHashMap<>();
    private final long timeoutSeconds = 300;

    @Autowired
    public PollService(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    public int subscribe(Instant instant) {
        UUID currentUserId = userContextService.currentUserId();
        int clientId;
        do {
            clientId = ThreadLocalRandom.current().nextInt();
        } while (!tryAddClient(clientId, currentUserId, instant));
        return clientId;
    }

    private boolean tryAddClient(int clientId, UUID currentUserId, Instant instant) {
        return clients.computeIfAbsent(clientId, id -> {
            PollClient client = new PollClient(id, currentUserId, instant);
            LOG.info("client(id={}, userId={}) subscribed", client.getId(), client.getUserId());
            return client;
        }) != null;
    }

    public void unsubscribeAll() {
        UUID currentUserId = userContextService.currentUserId();
        for (PollClient client : clients.values()) {
            if (client.getUserId().equals(currentUserId)) {
                clients.remove(client.getId());
                LOG.info("client(id={}, userId={}) unsubscribed", client.getId(), client.getUserId());
            }
        }
    }
    //
    // public void unsubscribe(long clientId) {
    // UUID currentUserId = userContextService.currentUserId();
    // PollClient client = clients.get(clientId);
    // if (!client.getUserId().equals(currentUserId)) {
    // throw new AccessDeniedException("user may only remove own clients");
    // }
    // client = removeClient(clientId);
    // LOG.info("client(id={}, userId={}) unsubscribed", client.getId(),
    // client.getUserId());
    // }

    public DeferredResult<List<PollEvent>> poll(int clientId) {
        UUID currentUserId = userContextService.currentUserId();
        PollClient client = clients.get(clientId);
        if (!currentUserId.equals(client.getUserId())) {
            throw new RuntimeException();
        }
        DeferredResult<List<PollEvent>> deferredResult = new DeferredResult<>(null, Collections.emptyList());
        client.replace(deferredResult);// previous result is discarded
        client.setHeartbeat(Instant.now());
        return deferredResult;
    }

    public void sendEvent(PollEvent message, Collection<UUID> recipients) {
        sendEvent(message, recipients::contains);
    }

    public void sendEvent(PollEvent message, Predicate<UUID> recipients) {
        for (PollClient client : clients.values()) {
            if (recipients.test(client.getUserId())) {
                client.offer(message);
                client.tryResolve();
            }
        }
    }

    @Scheduled(fixedRate = 30000)
    public void timeoutClients() {
        Instant timeout = Instant.now().minusSeconds(timeoutSeconds);
        for (PollClient client : clients.values()) {
            if (client.getHeartbeat().isBefore(timeout)) {
                clients.remove(client.getId());
                LOG.info("client(id={}, userId={}) timeouted", client.getId(), client.getUserId());
            }
        }
    }

}
