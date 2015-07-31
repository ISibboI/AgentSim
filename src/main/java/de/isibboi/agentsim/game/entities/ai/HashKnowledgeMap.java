package de.isibboi.agentsim.game.entities.ai;

import java.util.Set;

import de.isibboi.agentsim.game.map.Point;

/**
 * A {@link KnowledgeMap} that uses a hashtable to store its information.
 * @author Sebastian Schmidt
 * @since 0.3.0
 * 
 * @param <Knowledge> The type of knowledge.
 */
public class HashKnowledgeMap<Knowledge> implements KnowledgeMap<Knowledge> {
	private static class KnowledgeEntry<Knowledge> {
		private final long _age;
		private final Knowledge _knowledge;

		/**
		 * Creates a new object.
		 * @param age The age of the knowledge.
		 * @param knowledge The knowledge.
		 */
		public KnowledgeEntry(final long age, final Knowledge knowledge) {
			_age = age;
			_knowledge = knowledge;
		}

		/**
		 * Returns the age of the knowledge.
		 * @return The age of the knowledge.
		 */
		public long getAge() {
			return _age;
		}

		/**
		 * Returns the knowledge.
		 * @return The knowledge.
		 */
		public Knowledge getKnowledge() {
			return _knowledge;
		}

		@Override
		@SuppressWarnings("rawtypes")
		public boolean equals(final Object o) {
			if (o instanceof KnowledgeEntry) {
				KnowledgeEntry ke = (KnowledgeEntry) o;

				return _age == ke._age && _knowledge == ke._knowledge;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return ((int) _age >> 32) ^ ((int) _age) ^ _knowledge.hashCode();
		}
	}

	@Override
	public void updateLocation(final Point location, final Knowledge knowledge, final long tick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exchangeInformation(final KnowledgeMap<Knowledge> other) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getLocationKnowledgeAge(final Point location) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Knowledge getLocationKnowledge(final Point location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Point> getKnownLocationSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
