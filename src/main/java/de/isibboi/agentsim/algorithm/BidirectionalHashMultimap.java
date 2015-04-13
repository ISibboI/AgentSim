package de.isibboi.agentsim.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/**
 * A Multimap that contains unique values and can map value -> key.
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <K> The type of the keys.
 * @param <V> The type of the values.
 */
public class BidirectionalHashMultimap<K, V> implements Multimap<K, V> {
	private Multimap<K, V> _forward = HashMultimap.create();
	private Map<V, K> _reverse = new HashMap<>();

	@Override
	public int size() {
		return _reverse.size();
	}

	@Override
	public boolean isEmpty() {
		return _reverse.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return _forward.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return _reverse.containsKey(value);
	}

	@Override
	public boolean containsEntry(final Object key, final Object value) {
		return _forward.containsEntry(key, value);
	}

	@Override
	public boolean put(final K key, final V value) {
		if (_reverse.containsKey(value)) {
			throw new IllegalArgumentException("Value is already mapped!");
		}

		_forward.put(key, value);
		_reverse.put(value, key);

		return true;
	}

	@Override
	public boolean remove(final Object key, final Object value) {
		boolean resultForward = _forward.remove(key, value);
		boolean resultReverse = _reverse.remove(value) != null;

		// Ensure that the maps are consistent.
		assert resultForward == resultReverse;

		return resultForward;
	}

	@Override
	public boolean putAll(final K key, final Iterable<? extends V> values) {
		boolean changed = false;

		for (V value : values) {
			put(key, value);
		}

		return changed;
	}

	@Override
	public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
		boolean changed = false;

		for (Entry<? extends K, ? extends V> entry : multimap.entries()) {
			changed |= put(entry.getKey(), entry.getValue());
		}

		return changed;
	}

	@Override
	public Collection<V> replaceValues(final K key, final Iterable<? extends V> values) {
		// Implement if needed.
		throw new UnsupportedOperationException("Operation not supported!");
	}

	@Override
	public Collection<V> removeAll(final Object key) {
		// Implement if needed.
		throw new UnsupportedOperationException("Operation not supported!");
	}

	@Override
	public void clear() {
		_forward.clear();
		_reverse.clear();
	}

	@Override
	public Collection<V> get(final K key) {
		return _forward.get(key);
	}

	@Override
	public Set<K> keySet() {
		return _forward.keySet();
	}

	@Override
	public Multiset<K> keys() {
		return _forward.keys();
	}

	@Override
	public Collection<V> values() {
		return _forward.values();
	}

	@Override
	public Collection<Entry<K, V>> entries() {
		return _forward.entries();
	}

	@Override
	public Map<K, Collection<V>> asMap() {
		return _forward.asMap();
	}

	/**
	 * Returns the key to the given value.
	 * @param value The value.
	 * @return The key the value is mapped to.
	 */
	public K getKey(final V value) {
		return _reverse.get(value);
	}

	/**
	 * Updates the key for the given value. If the given value is not yet mapped, it is put into the map.
	 * @param key The key.
	 * @param value The value.
	 */
	public void update(final K key, final V value) {
		removeValue(value);
		put(key, value);
	}

	/**
	 * Removes the given value from the map.
	 * @param value The value to remove.
	 */
	public void removeValue(final V value) {
		K key = getKey(value);
		remove(key, value);
	}
}