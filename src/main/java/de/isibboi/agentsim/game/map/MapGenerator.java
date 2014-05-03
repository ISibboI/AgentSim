package de.isibboi.agentsim.game.map;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.map.GenerationParameters.GenerationType;
import de.isibboi.agentsim.noise.BorderFunction;
import de.isibboi.agentsim.noise.CombinedNoise;
import de.isibboi.agentsim.noise.Noise;
import de.isibboi.agentsim.noise.ScaledSimplexNoise;

/**
 * Generates the game map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class MapGenerator {
	private final Logger _log = LogManager.getLogger(getClass());
	
	private int _width;
	private int _height;

	/**
	 * Creates a new map generator, generating a map with the given width and height.
	 * 
	 * @param width The width.
	 * @param height The height.
	 */
	public MapGenerator(final int width, final int height) {
		_width = width;
		_height = height;
	}

	/**
	 * Generates the density map. That is the map that decides, which stone type to use. Lower density means air, higher density means various types of dense stone.
	 * The point with the lowest density will be the spawn point.
	 * 
	 * @param image The image to draw on.
	 * @return The spawn point.
	 */
	private Point generateDensityMap(final BufferedImage image) {
		CombinedNoise noise = new CombinedNoise(new Noise[] {
				new ScaledSimplexNoise(200 * _width / 1920, 200 * _height / 1080, 20, 256),
				new ScaledSimplexNoise(110 * _width / 1920, 110 * _height / 1080, 110, 190),
				new BorderFunction(_width, _height, -1500 * (_width + _height) / 4000, 3) });

		Collection<Material> materials = new ArrayList<>();
		Point spawnPoint = new Point();
		double minimalDensity = 1e100;
		
		for (Material material : Environment.MATERIAL_FACTORY.getAllMaterials()) {
			if (material.getGenerationParameters().getGenerationType() == GenerationType.DENSITY_MAP) {
				materials.add(material);
			}
		}

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				final double sample = noise.noise(x, y);

				// Search lowest density for the spawn point.
				if (sample < minimalDensity) {
					minimalDensity = sample;
					spawnPoint.x = x;
					spawnPoint.y = y;
				}

				boolean foundMaterial = false;

				for (Material material : materials) {
					if (material.inRange(sample)) {
						image.setRGB(x, y, material.getColor());
						foundMaterial = true;
						break;
					}
				}
				
				if (!foundMaterial) {
					_log.error("Missing material for density sample value: " + sample);
				}
			}
		}

		return spawnPoint;
	}

	/**
	 * Generates a new map.
	 * @return A newly generated map.
	 */
	public GameMap generateMap() {
		BufferedImage map = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
		Point spawnPoint = generateDensityMap(map);

		return new GameMap(map, spawnPoint);
	}
}