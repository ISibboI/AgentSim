package de.isibboi.agentsim.game.map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.map.GenerationParameters.GenerationType;
import de.isibboi.agentsim.noise.BorderFunction;
import de.isibboi.agentsim.noise.CombinedNoise;
import de.isibboi.agentsim.noise.Noise;
import de.isibboi.agentsim.noise.NoiseMap;

/**
 * Generates the game map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class MapGenerator {
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
	 * @param image The image to draw on.
	 */
	private void generateDensityMap(final BufferedImage image) {
		CombinedNoise noise = new CombinedNoise(new Noise[] {
				new NoiseMap(200, 200, 0, 256),
				new NoiseMap(110, 110, 30, 100),
				new BorderFunction(_width, _height, -1500, 3) });
		
		Collection<Material> materials = new ArrayList<>();
		
		for (Material material: Environment.MATERIAL_FACTORY.getAllMaterials()) {
			if (material.getGenerationParameters().getGenerationType() == GenerationType.DENSITY_MAP) {
				materials.add(material);
			}
		}

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double sample = noise.noise(x, y);
				
				for (Material material : materials) {
					if (material.inRange(sample)) {
						image.setRGB(x, y, material.getColor());
					}
				}
			}
		}
	}

	/**
	 * Generates a new map.
	 * @return A newly generated map.
	 */
	public GameMap generateMap() {
		BufferedImage map = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
		generateDensityMap(map);
		
		return new GameMap(map);
	}
}