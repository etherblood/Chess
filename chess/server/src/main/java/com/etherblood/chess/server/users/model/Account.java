package com.etherblood.chess.server.users.model;

import com.etherblood.chess.server.persistence.MutableEntity;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

/**
 *
 * @author Philipp
 */
@Entity
public class Account extends MutableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Type(type = "pg-uuid")
    private UUID id;
    @NotNull
    private String loginHandle;
    @NotNull
    private String password;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLoginHandle() {
        return loginHandle;
    }

    public void setLoginHandle(String loginHandle) {
        this.loginHandle = loginHandle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
