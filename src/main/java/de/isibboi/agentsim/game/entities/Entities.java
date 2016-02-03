package de.isibboi.agentsim.game.entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;

/**
 * Contains all the entities that are alive. Entities can be added or removed, but the operations are only applied when update is called.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class Entities implements Collection<Entity>, Updateable, Drawable {

	private final Collection<Entity> _entities = new ArrayList<>();
	private final Collection<Entity> _newEntities = new ArrayList<>();
	private final Collection<Entity> _removedEntities = new ArrayList<>();

	@Override
	public int size() {
		return _entities.size();
	}

	@Override
	public boolean isEmpty() {
		return _entities.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		return _entities.contains(o);
	}

	@Override
	public Iterator<Entity> iterator() {
		return _entities.iterator();
	}

	@Override
	public Object[] toArray() {
		return _entities.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return _entities.toArray(a);
	}

	@Override
	public boolean add(final Entity e) {
		_newEntities.add(e);
		return true;
	}

	/**
	 * Throws an IllegalArgumentException.
	 * 
	 * This implementation only works with {@link Entity} objects. 
	 */
	@Override
	public boolean remove(final Object o) {
		throw new IllegalArgumentException("Can only remove entities.");
	}

	/**
	 * Removes the given entity on the next update.
	 * @param e The entity to remove.
	 * @return True.
	 */
	public boolean remove(final Entity e) {
		_removedEntities.add(e);
		return true;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return _entities.containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends Entity> c) {
		return _entities.addAll(c);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		// Implement when needed.
		throw new UnsupportedOperationException("Operation currently not supported.");
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		// Implement when needed.
		throw new UnsupportedOperationException("Operation currently not supported.");
	}

	@Override
	public void clear() {
		_removedEntities.addAll(_entities);
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		if (_newEntities.size() > 0) {
			_entities.addAll(_newEntities);
			_newEntities.clear();
		}

		for (Entity entity : _entities) {
			try {
				entity.update(random, tick);
			} catch (GameUpdateException e) {
			}
		}

		if (_removedEntities.size() > 0) {
			_entities.removeAll(_removedEntities);
			_removedEntities.clear();
		}
	}

	@Override
	public void draw(final Graphics2D g, final double transition) {
		for (Entity entity : _entities) {
			entity.draw(g, transition);
		}
	}

	@Override
	public int getDrawPriority() {
		return Integer.MAX_VALUE;
	}
}