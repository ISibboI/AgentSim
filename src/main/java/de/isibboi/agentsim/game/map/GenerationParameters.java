package de.isibboi.agentsim.game.map;

public class GenerationParameters {
	public enum GenerationType {
		DENSITY_MAP
	}
	
	private final GenerationType _generationType;
	private final double _min;
	private final double _max;
	
	public GenerationParameters(final GenerationType generationType, final double min, final double max) {
		_generationType = generationType;
		_min = min;
		_max = max;
	}

	public GenerationType getGenerationType() {
		return _generationType;
	}

	public double getMin() {
		return _min;
	}

	public double getMax() {
		return _max;
	}

	public boolean inRange(double sample) {
		return sample >= _min && sample < _max;
	}
}
