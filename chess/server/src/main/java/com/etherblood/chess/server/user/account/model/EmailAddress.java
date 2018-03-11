package com.etherblood.chess.server.user.account.model;

import com.etherblood.chess.server.persistence.MutableEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

/**
 *
 * @author Philipp
 */
@Entity
public class EmailAddress extends MutableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Type(type = "pg-uuid")
    private UUID id;
    
    @NotNull
    @ManyToOne
    private Account account;

    @Column(unique = true, nullable = false)
    private String address;

}
