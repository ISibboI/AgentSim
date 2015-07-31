package de.isibboi.agentsim.game.entities.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.isibboi.agentsim.game.map.Point;

/**
 * A {@link KnowledgeMap} representation that is backed by a hash table.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <Knowledge> The type of knowledge that is stored.
 */
public class HashKnowledgeMap<Knowledge> extends AbstractKnowledgeMap<Knowledge> {
	private static class KnowledgeBucket<Knowledge> {
		// If this is made non-final, check the knowledge exchange methods for object reuse.
		private final Knowledge _knowledge;
		private final long _lastUpdate;

		/**
		 * Creates a knowledge bucket with the specified knowledge and update time.
		 * @param knowledge The knowledge.
		 * @param lastUpdate The last time this knowledge was updated.
		 */
		public KnowledgeBucket(final Knowledge knowledge, final long lastUpdate) {
			_knowledge = knowledge;
			_lastUpdate = lastUpdate;
		}

		/**
		 * Returns the knowledge.
		 * @return The knowledge.
		 */
		public Knowledge getKnowledge() {
			return _knowledge;
		}

		/**
		 * Returns the last time the knowledge was updated.
		 * @return The last time the knowledge was updated.
		 */
		public long getLastUpdate() {
			return _lastUpdate;
		}
	}

	private final KnowledgeBucket<Knowledge> _emptyBucket = new KnowledgeBucket<>(null, 0);

	private final HashMap<Point, KnowledgeBucket<Knowledge>> _map = new HashMap<>();

	/**
	 * Returns the KnowledgeBucket at the specified location,
	 * or the empty bucket, if there is no knowledge about the specified location.
	 * 
	 * @param location The location.
	 * @return The knowledge bucket for the given location.
	 */
	private KnowledgeBucket<Knowledge> getKnowledgeBucket(final Point location) {
		KnowledgeBucket<Knowledge> result = _map.get(location);

		if (result == null) {
			result = _emptyBucket;
		}

		return result;
	}

	@Override
	public void updateLocation(final Point location, final Knowledge knowledge, final long tick) {
		if (tick <= 0) {
			throw new IllegalArgumentException("Tick must be greater than zero!");
		}

		KnowledgeBucket<Knowledge> oldKnowledge = getKnowledgeBucket(location);

		if (oldKnowledge.getLastUpdate() < tick) {
			_map.put(location, new KnowledgeBucket<Knowledge>(knowledge, tick));
		}
	}

	@Override
	public long getLocationKnowledgeAge(final Point location) {
		return getKnowledgeBucket(location).getLastUpdate();
	}

	@Override
	public Knowledge getLocationKnowledge(final Point location) {
		return getKnowledgeBucket(location).getKnowledge();
	}

	@Override
	public Set<Point> getKnownLocationSet() {
		return _map.keySet();
	}

	@Override
	public void exchangeInformation(final KnowledgeMap<Knowledge> other) {
		if (other instanceof ProviderBackedKnowledgeMap) {
			exchangeInformation(((ProviderBackedKnowledgeMap<Knowledge>) other).getDecoratedKnowledgeMap());
		} else if (other instanceof HashKnowledgeMap) {
			exchangeInformation((HashKnowledgeMap<Knowledge>) other);
		} else {
			super.exchangeInformation(other);
		}
	}

	/**
	 * @see #exchangeInformation(KnowledgeMap)
	 * @param other The other map.
	 */
	public void exchangeInformation(final HashKnowledgeMap<Knowledge> other) {
		final HashMap<Point, KnowledgeBucket<Knowledge>> toPutIntoOther = new HashMap<>();

		for (Map.Entry<Point, KnowledgeBucket<Knowledge>> mapping : other._map.entrySet()) {
			KnowledgeBucket<Knowledge> ownKnowledge = getKnowledgeBucket(mapping.getKey());

			if (ownKnowledge._lastUpdate < mapping.getValue().getLastUpdate()) {
				_map.put(mapping.getKey(), mapping.getValue());
			} else {
				toPutIntoOther.put(mapping.getKey(), ownKnowledge);
			}
		}

		other._map.putAll(toPutIntoOther);
	}
}
