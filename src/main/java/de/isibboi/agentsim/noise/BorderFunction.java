package de.isibboi.agentsim.noise;

public class BorderFunction implements Noise {
	private final double centerX;
	private final double centerY;
	private final double width;
	private final double height;
	private final double start;
	private final double gradient;

	public BorderFunction(final double width, final double height, final double start, final double gradient) {
		centerX = width / 2;
		centerY = height / 2;
		this.width = width;
		this.height = height;
		this.start = start;
		this.gradient = gradient;
	}

	@Override
	public double noise(final double x, final double y) {
		double xSquareDistance = Math.abs((x - centerX) * (x - centerX) / width * height);
		double ySquareDistance = Math.abs((y - centerY) * (y - centerY) / height * width);
		double distance = Math.sqrt(xSquareDistance + ySquareDistance);

		double result = start + distance * gradient;
		if (result < 0) {
			result = 0;
		}

		return result;
	}
}