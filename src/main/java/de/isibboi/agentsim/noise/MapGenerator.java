package de.isibboi.agentsim.noise;

import java.awt.image.BufferedImage;

import de.isibboi.agentsim.game.GameMap;

public class MapGenerator {
	private int _width;
	private int _height;

	public MapGenerator(final int width, final int height) {
		_width = width;
		_height = height;
	}

	private BufferedImage generateNoiseImage() {
		BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
		CombinedNoise noise = new CombinedNoise(new Noise[] { new NoiseMap(200, 200, 0, 256), new NoiseMap(110, 110, 30, 100), new BorderFunction(_width, _height, -1500, 3) });

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = 0xFF000000;
				int grey = (int) (noise.noise(x, y));

				if (grey > 255) {
					grey = 255;
				}

				if (grey < 255) {
					grey = 0;
				}

				rgb |= grey;
				rgb |= grey << 8;
				rgb |= grey << 16;

				image.setRGB(x, y, rgb);
			}
		}

		return image;
	}
	
	public GameMap getMap() {
		return new GameMap(generateNoiseImage());
	}
}