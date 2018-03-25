package com.etherblood.chess.server.user.account.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.etherblood.chess.server.persistence.MutableEntity;

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
    @ManyToOne(fetch=FetchType.LAZY)
    private Account account;

    @NotNull
    private String address;

}
