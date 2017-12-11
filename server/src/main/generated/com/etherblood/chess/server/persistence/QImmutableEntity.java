package com.etherblood.chess.server.persistence;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QImmutableEntity is a Querydsl query type for ImmutableEntity
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QImmutableEntity extends EntityPathBase<ImmutableEntity> {

    private static final long serialVersionUID = 306583334L;

    public static final QImmutableEntity immutableEntity = new QImmutableEntity("immutableEntity");

    public final DateTimePath<java.util.Date> created = createDateTime("created", java.util.Date.class);

    public QImmutableEntity(String variable) {
        super(ImmutableEntity.class, forVariable(variable));
    }

    public QImmutableEntity(Path<? extends ImmutableEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImmutableEntity(PathMetadata<?> metadata) {
        super(ImmutableEntity.class, metadata);
    }

}

