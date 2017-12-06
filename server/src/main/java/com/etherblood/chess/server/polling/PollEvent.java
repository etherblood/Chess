package com.etherblood.chess.server.polling;

/**
 *
 * @author Philipp
 */
public abstract class PollEvent {

    private final String type;
    private final Object data;

    public PollEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
}
