package com.etherblood.chess.server.persistence;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Philipp
 */
public abstract class AbstractRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    public void remove(Object entity) {
        entityManager.remove(entity);
    }

    protected JPAQuery from(EntityPath<?>... paths) {
        return new JPAQuery(entityManager).from(paths);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
