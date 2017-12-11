package com.etherblood.chess.server.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

    @PrePersist
    protected void onPersist() {
        Date now = new Date();
        created = now;
        updated = now;
    }

    public Date getCreated() {
        return created;
    }

    public int getVersion() {
        return version;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
