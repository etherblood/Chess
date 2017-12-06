package com.etherblood.chess.server.users;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Philipp
 */
public class User {

    private UUID id;
    private String name;
    private Date created;

    public User() {
    }

    public User(UUID id, String name, Date created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreated() {
        return created;
    }
}
