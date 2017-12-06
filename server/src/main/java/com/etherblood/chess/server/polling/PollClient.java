package com.etherblood.chess.server.polling;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author Philipp
 */
public class PollClient {

    private final UUID userId;
    private final long id;
    private volatile Instant heartbeat;
    private final Queue<PollEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private final AtomicReference<DeferredResult<List<PollEvent>>> deferredResult = new AtomicReference<>(null);

    public PollClient(UUID userId, long id, Instant heartbeat) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(heartbeat);
        this.userId = userId;
        this.id = id;
        this.heartbeat = heartbeat;
    }

    public UUID getUserId() {
        return userId;
    }

    public long getId() {
        return id;
    }

    public Instant getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Instant heartbeat) {
        Objects.requireNonNull(heartbeat);
        this.heartbeat = heartbeat;
    }

    public void offer(PollEvent event) {
        eventQueue.add(event);
    }

    public void tryResolve() {
        DeferredResult<List<PollEvent>> result = replace(null);
        if (result == null) {
            return;
        }
        List<PollEvent> events = pollEvents();
        if (events.isEmpty() && deferredResult.compareAndSet(null, result)) {//result is discarded if replacement was added in the meantime
            return;
        }
        result.setResult(events);
    }

    public List<PollEvent> pollEvents() {
        List<PollEvent> result = new ArrayList<>();
        PollEvent next;
        while ((next = eventQueue.poll()) != null) {
            result.add(next);
        }
        return result;
    }

    public DeferredResult<List<PollEvent>> replace(DeferredResult<List<PollEvent>> result) {
        return deferredResult.getAndSet(result);
    }

}
