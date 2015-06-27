package de.isibboi.agentsim.game.entities.ai;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.isibboi.agentsim.game.map.Point;

/**
 * A {@link KnowledgeMap} that uses an array to store its information.
 * @author Sebastian Schmidt
 * @since 0.3.0
 * 
 * @param <Knowledge> The type of knowledge.
 */
public class ArrayKnowledgeMap<Knowledge> extends AbstractKnowledgeMap<Knowledge> {
	/**
	 * Contains the last update times for the fields. If the last update time is zero, the field value is invalid.
	 */
	private final long[][] _lastUpdate;
	private final Knowledge[][] _knowledge;
	private final int _width;
	private final int _height;
	private final Set<Point> _knownLocations;

	/**
	 * Creates a new empty {@code ArrayKnowledgeMap}.
	 * @param width The width of the map.
	 * @param height The height of the map.
	 */
	@SuppressWarnings("unchecked")
	public ArrayKnowledgeMap(final int width, final int height) {
		_lastUpdate = new long[width][height];
		_knowledge = (Knowledge[][]) new Object[width][height];
		_width = width;
		_height = height;
		_knownLocations = new HashSet<>();
	}

	@Override
	public void updateLocation(final Point location, final Knowledge knowledge, final long tick) {
		if (tick <= 0) {
			throw new IllegalArgumentException("Tick must be greater than zero!");
		}

		final long knownTick = _lastUpdate[location.getX()][location.getY()];

		if (knownTick < tick) {
			_lastUpdate[location.getX()][location.getY()] = tick;
			_knowledge[location.getX()][location.getY()] = knowledge;
		}

		if (knownTick == 0) {
			_knownLocations.add(location);
		}
	}

	@Override
	public void exchangeInformation(final KnowledgeMap<Knowledge> other) {
		if (other instanceof ProviderBackedKnowledgeMap) {
			exchangeInformation(((ProviderBackedKnowledgeMap<Knowledge>) other).getDecoratedKnowledgeMap());
		} else if (other instanceof ArrayKnowledgeMap) {
			exchangeInformation((ArrayKnowledgeMap<Knowledge>) other);
		} else {
			super.exchangeInformation(other);
		}
	}

	/**
	 * @see #exchangeInformation(KnowledgeMap)
	 * @param other The other map.
	 */
	public void exchangeInformation(final ArrayKnowledgeMap<Knowledge> other) {
		if (_width != other._width || _height != other._height) {
			throw new IllegalArgumentException("Dimension mismatch!");
		}

		for (int i = 0; i < _width; i++) {
			for (int j = 0; j < _height; j++) {
				if (_lastUpdate[i][j] > other._lastUpdate[i][j]) {
					other._lastUpdate[i][j] = _lastUpdate[i][j];
					other._knowledge[i][j] = _knowledge[i][j];
				} else {
					_lastUpdate[i][j] = other._lastUpdate[i][j];
					_knowledge[i][j] = other._knowledge[i][j];
				}
			}
		}
	}

	@Override
	public long getLocationKnowledgeAge(final Point location) {
		return _lastUpdate[location.getX()][location.getY()];
	}

	@Override
	public Knowledge getLocationKnowledge(final Point location) {
		return _knowledge[location.getX()][location.getY()];
	}

	@Override
	public Set<Point> getKnownLocationSet() {
		return Collections.unmodifiableSet(_knownLocations);
	}
}
