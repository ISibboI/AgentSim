package de.isibboi.agentsim.game.entities;

import de.isibboi.agentsim.Settings;

/**
 * Contains all game-related attributes of a goblin.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class GoblinAttributes implements Attributes {
	private int _age;
	private int _saturation;
	private int _maxSaturation;
	private boolean _alive;

	/**
	 * Creates a new object initializing all the attributes.
	 * @param age The age of the goblin.
	 * @param saturation The saturation of the goblin.
	 * @param maxSaturation The maximum saturation of the goblin.
	 */
	public GoblinAttributes(final int age, final int saturation, final int maxSaturation) {
		_age = age;
		_saturation = saturation;
		_maxSaturation = maxSaturation;
		_alive = true;
	}

	/**
	 * Creates a new goblin attributes object. Initializes the attributes with the given settings.
	 * @param settings The settings.
	 */
	public GoblinAttributes(final Settings settings) {
		_age = 0;
		_saturation = settings.getInt(Settings.GAME_ENTITIES_GOBLIN_INITIAL_SATURATION);
		_maxSaturation = settings.getInt(Settings.GAME_ENTITIES_GOBLIN_INITIAL_MAX_SATURATION);
		_alive = true;
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

	/**
	 * Returns {@code true} if the goblin is alive.
	 * @return The alive.
	 */
	public boolean isAlive() {
		return _alive;
	}

	/**
	 * Sets the alive status of the goblin. If it is {@code false}, the goblin is dead.
	 * @param alive The alive status.
	 */
	public void setAlive(final boolean alive) {
		_alive = alive;
	}

	/**
	 * Feeds the goblin until it is full.
	 * @return The saturation difference created by this method.
	 */
	public int feedCompletely() {
		int feedAmount = _maxSaturation - _saturation;
		_saturation = _maxSaturation;

		return feedAmount;
	}
}