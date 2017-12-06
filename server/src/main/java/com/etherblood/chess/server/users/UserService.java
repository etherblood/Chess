package com.etherblood.chess.server.users;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Service
public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
    
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    {
        users.put(UUID.fromString("c1c2cad2-2f9a-4e25-ba46-14872851c5dd"), new User(UUID.fromString("c1c2cad2-2f9a-4e25-ba46-14872851c5dd"), "tester", new Date()));
    }

    public UUID createUser(String name) {
        Objects.requireNonNull(name);
        User user = new User(UUID.randomUUID(), name, new Date());
        users.put(user.getId(), user);
        LOG.info("created new User {}", name);
        return user.getId();
    }

    public User getUser(UUID userId) {
        return users.get(userId);
    }

//    public Collection<User> getAllUsers() {
//        return users.values();
//    }

}
