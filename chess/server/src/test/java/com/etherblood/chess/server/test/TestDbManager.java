package com.etherblood.chess.server.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class TestDbManager {

	@PersistenceContext
	private EntityManager entityManager;
	
	private List<EntityType<?>> entityTypes;
	
	@PostConstruct
	public void init() {
		Map<Class<?>, Collection<Class<?>>> dependencies = new HashMap<>();
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		for (EntityType<?> entityType : entities) {
			Class<?> clazz = entityType.getJavaType();
			dependencies.put(clazz, new ArrayList<>());
			while (clazz != null) {
				for (Field field : clazz.getDeclaredFields()) {
					if(field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
						dependencies.get(clazz).add(field.getType());
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		
		Map<Class<?>, EntityType<?>> result = new LinkedHashMap<>();
		int iterations = 0;
		while(result.size() != dependencies.size()) {
			if(iterations++ > 100) {
				throw new IllegalStateException();
			}
			for (EntityType<?> entityType : entities) {
				if(result.containsKey(entityType.getJavaType())) {
					continue;
				}
				if(new ArrayList<>(result.keySet()).containsAll(dependencies.get(entityType.getJavaType()))) {
					result.put(entityType.getJavaType(), entityType);
				}
			}
		}
		entityTypes = new ArrayList<>(result.values());
		Collections.reverse(entityTypes);
	}

	@Transactional
	public void cleanDb() {
		for (EntityType<?> entityType : entityTypes) {
			entityManager.createNativeQuery("delete from " + entityType.getName() + ";").executeUpdate();
		}
	}
}
