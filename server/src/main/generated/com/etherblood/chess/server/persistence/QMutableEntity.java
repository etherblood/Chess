package com.etherblood.chess.server.persistence;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QMutableEntity is a Querydsl query type for MutableEntity
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QMutableEntity extends EntityPathBase<MutableEntity> {

    private static final long serialVersionUID = 1450573322L;

    public static final QMutableEntity mutableEntity = new QMutableEntity("mutableEntity");

    public final DateTimePath<java.util.Date> created = createDateTime("created", java.util.Date.class);

    public final DateTimePath<java.util.Date> updated = createDateTime("updated", java.util.Date.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QMutableEntity(String variable) {
        super(MutableEntity.class, forVariable(variable));
    }

    public QMutableEntity(Path<? extends MutableEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMutableEntity(PathMetadata<?> metadata) {
        super(MutableEntity.class, metadata);
    }

}

