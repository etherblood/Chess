package com.etherblood.chess.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MapBuilder<K, V> {
	private final Supplier<Map<K, V>> supply;
	private final Map<K, V> map;

	public MapBuilder() {
		this(HashMap::new);
	}

	public MapBuilder(Supplier<Map<K, V>> supply) {
		super();
		this.supply = supply;
		this.map = supply.get();
	}
	
	public MapBuilder<K, V> with(K key, V value) {
		map.put(key, value);
		return this;
	}
	
	public Map<K, V> build() {
		Map<K, V> result = supply.get();
		result.putAll(map);
		return result;
	}
}
