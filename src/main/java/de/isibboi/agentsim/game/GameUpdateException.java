package de.isibboi.agentsim.game;

/**
 * A exception that is thrown if updating goes wrong.
 * @author sibbo
 *
 */
public class GameUpdateException extends Exception {
	/**
	 * Creates a new exception with the given message.
	 * @param message The message.
	 */
	public GameUpdateException(final String message) {
		super(message);
	}
}