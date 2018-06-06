package com.etherblood.chess.server.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Philipp
 */
@MappedSuperclass
public abstract class MutableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date created;
    @Version
    private int version;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public int getVersion() {
        return version;
    }

    public Date getUpdated() {
        return updated;
    }
}
