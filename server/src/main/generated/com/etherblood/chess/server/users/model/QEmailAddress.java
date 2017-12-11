package com.etherblood.chess.server.users.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEmailAddress is a Querydsl query type for EmailAddress
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmailAddress extends EntityPathBase<EmailAddress> {

    private static final long serialVersionUID = 529871507L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailAddress emailAddress = new QEmailAddress("emailAddress");

    public final com.etherblood.chess.server.persistence.QMutableEntity _super = new com.etherblood.chess.server.persistence.QMutableEntity(this);

    public final QAccount account;

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.util.Date> updated = _super.updated;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QEmailAddress(String variable) {
        this(EmailAddress.class, forVariable(variable), INITS);
    }

    public QEmailAddress(Path<? extends EmailAddress> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmailAddress(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmailAddress(PathMetadata<?> metadata, PathInits inits) {
        this(EmailAddress.class, metadata, inits);
    }

    public QEmailAddress(Class<? extends EmailAddress> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
    }

}

