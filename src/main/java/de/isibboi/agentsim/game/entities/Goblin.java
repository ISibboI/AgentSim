package de.isibboi.agentsim.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.ai.GoblinSwarmAI;
import de.isibboi.agentsim.game.map.Point;

/**
 * A goblin that digs dirt.
 * @author Sebastian Schmidt
 * @since 0.1.0
 */
public class Goblin extends MapEntity {
	private static final Logger LOG = LogManager.getLogger(Goblin.class);

	private final Color _color = new Color(0x55bb55);
	private final Color _selectedColor = new Color(0xaaddaa);

	private GoblinAttributes _attributes;
	private final GoblinSwarmAI _ai;

	/**
	 * Creates a new goblin at the specified location.
	 * 
	 * @param entityLocationManager The entity location manager that manages the location of this goblin.
	 */
	public Goblin(final EntityLocationManager entityLocationManager) {
		super(entityLocationManager);

		_attributes = new GoblinAttributes(entityLocationManager.getSettings());
		_ai = new GoblinSwarmAI(entityLocationManager, this);
	}

	/**
	 * Returns the AI of this goblin.
	 * 
	 * @return The AI of this goblin.
	 */
	public GoblinSwarmAI getAI() {
		return _ai;
	}

	@Override
	public void draw(final Graphics2D g, final double transition) {
		super.draw(g, transition);

		if (isSelected()) {
			_ai.draw(g, transition);
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		if (isSelected()) {
			g.setColor(_selectedColor);
		} else {
			g.setColor(_color);
		}

		g.fillRect(0, 0, 1, 1);
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		super.update(random, tick);

		updateAttributes();

		if (!_attributes.isAlive()) {
			getEntityLocationManager().removeEntity(this);
		} else {
			_ai.update(_attributes, random, tick);
			calculateNewLocation(_ai.getMovement());
		}
	}

	/**
	 * Updates the attributes of the goblin.
	 */
	private void updateAttributes() {
		// Make goblin older.
		_attributes.setAge(_attributes.getAge() + 1);

		// Make goblin hungry.
		_attributes.setSaturation(_attributes.getSaturation() - 1);

		// Check if the goblin is still alive.
		if (_attributes.getSaturation() < 0) {
			_attributes.setAlive(false);
			LOG.trace("Goblin starved to death. It is " + getLocation().manhattanDistance(getMap().getSpawnPoint()) + " steps away from spawn.");
		}
	}

	/**
	 * Calculates the new location of the goblin.
	 * @param movement The desired movement.
	 */
	private void calculateNewLocation(final Movement movement) {
		if (movement == Movement.NONE) {
			return;
		}

		Point newLocation = movement.move(getLocation());

		if (getMap().isValidEntityLocation(newLocation)) {
			setLocation(newLocation);
			_ai.eventMoveTo(newLocation);
		} else if (getMap().isLocationOnMap(newLocation)) {
			_ai.eventCollideWithWall(newLocation);
		} else {
			_ai.eventCollideWithMapBorder(newLocation);
		}
	}

	@Override
	public void collideWith(final Entity entity, final int tick) {
		_ai.eventCollideWithEntity(entity, tick);
	}

	/**
	 * Returns the attributes of this goblin.
	 * 
	 * @return The attributes of this goblin.
	 */
	public GoblinAttributes getAttributes() {
		return _attributes;
	}

	@Override
	public boolean blocksBuildings() {
		return false;
	}
}