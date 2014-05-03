package de.isibboi.agentsim.noise;

/**
 * A function like a bath tube, that is zero in an oval shaped area and raises with a specific gradient outside of this area.
 * It is used to create a soft border for a map.
 * 
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class BorderFunction implements Noise {
	private final double _centerX;
	private final double _centerY;
	private final double _width;
	private final double _height;
	private final double _start;
	private final double _gradient;

	/***
	 * Creates a new border function.
	 * 
	 * @param width The width of the oval area.
	 * @param height The height of the oval area.
	 * @param start The value of the function at the center. Note that this is not the value returned. The returned value will be set to zero, if it would be lower than that.
	 * @param gradient The gradient of the function. The function rises linearly in an oval shape from the center point to the outside.
	 * @see BorderFunction
	 */
	public BorderFunction(final double width, final double height, final double start, final double gradient) {
		_centerX = width / 2;
		_centerY = height / 2;
		this._width = width;
		this._height = height;
		this._start = start;
		this._gradient = gradient;
	}

	@Override
	public double noise(final double x, final double y) {
		double xSquareDistance = Math.abs((x - _centerX) * (x - _centerX) / _width * _height);
		double ySquareDistance = Math.abs((y - _centerY) * (y - _centerY) / _height * _width);
		double distance = Math.sqrt(xSquareDistance + ySquareDistance);

		double result = _start + distance * _gradient;
		if (result < 0) {
			result = 0;
		}

		return result;
	}
}