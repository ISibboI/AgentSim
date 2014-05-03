package de.isibboi.agentsim.noise;

import java.awt.image.BufferedImage;

public class MapGenerator {
	private final BufferedImage map;

	public MapGenerator() {
		map = generateNoiseImage();
	}

	private static BufferedImage generateNoiseImage() {
		BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
		CombinedNoise noise = new CombinedNoise(new Noise[] { new NoiseMap(200, 200, 0, 256), new NoiseMap(110, 110, 30, 100), new BorderFunction(1920, 1080, -1500, 3) });

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
}