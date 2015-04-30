package de.isibboi.agentsim.ui;

/**
 * Receives the status messages created by the game.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface GameStatusMessageListener {
	/**
	 * The game over message. The player has the option to restart or quit the game.
	 * @param message The message.
	 */
	void receiveGameOverMessage(String message);

	/**
	 * The restart game message.
	 */
	void receiveRestartGameMessage();
}