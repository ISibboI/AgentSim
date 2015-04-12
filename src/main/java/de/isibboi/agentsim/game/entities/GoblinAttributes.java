package de.isibboi.agentsim.game.entities;

/**
 * Contains all game-related attributes of a goblin.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class GoblinAttributes {
	private int _age;
	private int _saturation;

	/**
	 * Creates a new object initializing all the attributes.
	 * @param age The age of the goblin.
	 * @param saturation The saturation of the goblin.
	 */
	public GoblinAttributes(final int age, final int saturation) {
		_age = age;
		_saturation = saturation;
	}

	/**
	 * Returns the age.
	 * @return The age.
	 */
	public int getAge() {
		return _age;
	}

	/**
	 * Sets the age.
	 * @param age The age.
	 */
	public void setAge(final int age) {
		_age = age;
	}

	/**
	 * Returns the saturation.
	 * @return The saturation.
	 */
	public int getSaturation() {
		return _saturation;
	}

	/**
	 * Sets the saturation.
	 * @param saturation The saturation.
	 */
	public void setSaturation(final int saturation) {
		_saturation = saturation;
	}
}