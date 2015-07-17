package de.isibboi.agentsim.algorithm;

import de.isibboi.agentsim.game.entities.ai.KnowledgeMap;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * Translates from a knowledge map to a blockade map.
 * Unknown locations are known as blocking.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class KnowledgeBasedBlockadeMap implements BlockadeMap {
	private final KnowledgeMap<Material> _mapKnowledge;

	/**
	 * Creates a new blockade map based on the given map knowledge.
	 * 
	 * @param mapKnowledge The map knowledge.
	 */
	public KnowledgeBasedBlockadeMap(final KnowledgeMap<Material> mapKnowledge) {
		_mapKnowledge = mapKnowledge;
	}

	@Override
	public boolean isBlocked(final Point location) {
		Material material = _mapKnowledge.getLocationKnowledge(location);

		if (material == null) {
			return true;
		} else {
			return material.isSolid();
		}
	}

}
