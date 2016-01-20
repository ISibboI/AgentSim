package de.isibboi.agentsim.game.entities.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.ai.intends.Intend;
import de.isibboi.agentsim.game.entities.ai.intends.MiningIntend;

/**
 * The main building of the swarm.
 * It is to be decided what it exactly does.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class SwarmMainBuilding extends Building {
	private final Color _color = new Color(0x5522dd);
	private final Color _selectedColor = new Color(0xaa88ff);

	/**
	 * Creates a new object.
	 * 
	 * @param entityLocationManager The entity location manager.
	 */
	public SwarmMainBuilding(final EntityLocationManager entityLocationManager) {
		super(entityLocationManager);
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
	public boolean blocksBuildings() {
		return true;
	}

	@Override
	public Iterable<? extends Intend> getIntends(final int tick) {
		return Collections.singleton(new MiningIntend(tick, 1, Environment.MATERIAL_AIR));
	}
}