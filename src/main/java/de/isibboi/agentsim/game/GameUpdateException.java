package de.isibboi.agentsim.game;

/**
 * A exception that is thrown if updating goes wrong.
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
@SuppressWarnings("serial")
public class GameUpdateException extends Exception {
	/**
	 * Creates a new exception with the given message.
	 * @param message The message.
	 */
	public GameUpdateException(final String message) {
		super(message);
	}
}