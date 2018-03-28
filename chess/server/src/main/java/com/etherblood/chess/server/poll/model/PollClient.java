package com.etherblood.chess.server.poll.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.web.context.request.async.DeferredResult;

import com.etherblood.chess.api.PollEvent;

/**
 *
 * @author Philipp
 */
public class PollClient {

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
	}

	public void offer(PollEvent<?> event) {
		Objects.requireNonNull(event);
		eventQueue.add(event);
	}

	public void tryResolve() {
		DeferredResult<List<PollEvent<?>>> result = replace(null);
		if (result == null) {
			return;
		}
		List<PollEvent<?>> events = pollEvents();
		if (events.isEmpty() && deferredResult.compareAndSet(null, result)) {
			// deferredResult is discarded if replacement was added in the meantime
			return;
		}
		result.setResult(events);
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
