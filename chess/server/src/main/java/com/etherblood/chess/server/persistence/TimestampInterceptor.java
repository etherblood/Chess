package com.etherblood.chess.server.persistence;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.etherblood.chess.server.time.TimeService;

/**
 *
 * @author Philipp
 */
public class TimestampInterceptor extends EmptyInterceptor {

    private final static long serialVersionUID = 1L;

    private final TimeService time;

    @Autowired
    public TimestampInterceptor(TimeService time) {
        this.time = time;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof MutableEntity) {
        	Date now = time.now().toDate();
        	state[Arrays.asList(propertyNames).indexOf("created")] = now;
        	state[Arrays.asList(propertyNames).indexOf("updated")] = now;
        	return true;
        }
        if (entity instanceof ImmutableEntity) {
        	state[Arrays.asList(propertyNames).indexOf("created")] = time.now().toDate();
        	return true;
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (entity instanceof MutableEntity) {
        	currentState[Arrays.asList(propertyNames).indexOf("updated")] = time.now().toDate();
        	return true;
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

}
