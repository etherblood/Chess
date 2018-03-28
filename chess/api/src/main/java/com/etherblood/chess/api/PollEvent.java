package com.etherblood.chess.api;

/**
 *
 * @author Philipp
 */
public abstract class PollEvent<T> {

    private final String type;
    private final T data;

    public PollEvent(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public T getData() {
        return data;
    }
}
