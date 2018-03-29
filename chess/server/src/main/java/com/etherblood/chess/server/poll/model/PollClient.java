package com.etherblood.chess.server.poll.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import com.etherblood.chess.api.PollEvent;

/**
 *
 * @author Philipp
 */
public class PollClient {
	private final static Logger LOG = LoggerFactory.getLogger(PollClient.class);

	private final int id;
	private final UUID userId;
	private volatile Instant heartbeat;
	private final Queue<PollEvent<?>> eventQueue = new ConcurrentLinkedQueue<>();
	private final AtomicReference<DeferredResult<List<PollEvent<?>>>> deferredResult = new AtomicReference<>(null);

	public PollClient(int id, UUID userId, Instant heartbeat) {
		this.id = id;
		Objects.requireNonNull(userId);
		this.userId = userId;
		setHeartbeat(heartbeat);
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public Instant getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(Instant heartbeat) {
		Objects.requireNonNull(heartbeat);
		this.heartbeat = heartbeat;
		LOG.debug("updated heartbeat to {} for client(userId={},clientId={})", heartbeat, userId, id);
	}

	public void offer(PollEvent<?> event) {
		Objects.requireNonNull(event);
		eventQueue.add(event);
		LOG.debug("added event {} to queue of client(userId={},clientId={})", event, userId, id);
	}

	public void tryResolve() {
		DeferredResult<List<PollEvent<?>>> result = deferredResult.getAndSet(null);
		if (result == null) {
			return;
		}
		List<PollEvent<?>> events = pollEvents();
		if (events.isEmpty() && deferredResult.compareAndSet(null, result)) {
			// deferredResult is discarded if replacement was set in the meantime
			return;
		}
		// events might be empty if an other deferredResult was set in the meantime
		if(result.setResult(events)) {
			LOG.debug("resolved events {} for client(userId={},clientId={})", events, userId, id);
		} else {
			LOG.error("lost events {} for client(userId={},clientId={})", events, userId, id);
		}
	}

	public List<PollEvent<?>> pollEvents() {
		List<PollEvent<?>> result = new ArrayList<>();
		PollEvent<?> next;
		while ((next = eventQueue.poll()) != null) {
			result.add(next);
		}
		return result;
	}

	public DeferredResult<List<PollEvent<?>>> replace(DeferredResult<List<PollEvent<?>>> result) {
		return deferredResult.getAndSet(result);
	}

}
