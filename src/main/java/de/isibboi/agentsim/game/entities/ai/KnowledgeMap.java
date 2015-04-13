package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.map.Point;

/**
 * Represents knowledge about a specific feature on the map as known to a specific entity.
 * @author Sebastian Schmidt
 * @since 0.3.0
 * 
 * @param <Knowledge> The type of knowledge.
 */
public class KnowledgeMap<Knowledge> {
	/**
	 * Contains the last update times for the fields. If the last update time is zero, the field value is invalid.
	 */
	private final long[][] _lastUpdate;
	private final Knowledge[][] _materials;

	/**
	 * Creates a new empty mapKnowledge.
	 * @param width The width of the map.
	 * @param height The height of the map.
	 */
	@SuppressWarnings("unchecked")
	public KnowledgeMap(final int width, final int height) {
		_lastUpdate = new long[width][height];
		_materials = (Knowledge[][]) new Object[width][height];
	}

	/**
	 * Updates the knowledge about the material at the specified location.
	 * @param location The location.
	 * @param knowledge The knowledge.
	 * @param tick The tick this knowledge was created.
	 */
	public void updateLocation(final Point location, final Knowledge knowledge, final int tick) {
		if (_lastUpdate[location.getX()][location.getY()] < tick) {
			_lastUpdate[location.getX()][location.getY()] = tick;
			_materials[location.getX()][location.getY()] = knowledge;
		}
	}
}
