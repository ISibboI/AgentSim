package de.isibboi.agentsim.game.entities.buildings;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.MapEntity;

/**
 * The base class for the buildings.
 * Buildings can give orders to the swarm entities or interact in other special ways.
 * They also might provide some information to the entities.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public abstract class Building extends MapEntity {
	/**
	 * Creates a new object.
	 * 
	 * @param entityLocationManager The entity location manager.
	 */
	public Building(EntityLocationManager entityLocationManager) {
		super(entityLocationManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getDrawPriority() {
		return 0;
	}
}