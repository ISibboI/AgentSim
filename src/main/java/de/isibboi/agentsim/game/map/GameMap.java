package de.isibboi.agentsim.game.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import de.isibboi.agentsim.game.Block;
import de.isibboi.agentsim.game.Drawable;

/**
 * Represents the game map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class GameMap implements Drawable {
	private final BufferedImage _map;
	private final Map<Integer, Block> _blocks;

	/**
	 * Creates the map from the given image.
	 * @param image The map image.
	 */
	public GameMap(final BufferedImage image) {
		this._map = image;
		
		_blocks = new HashMap<>();
	}

	@Override
	public void draw(final Graphics2D g) {
		g.drawImage(_map, 0, 0, null);
	}
}