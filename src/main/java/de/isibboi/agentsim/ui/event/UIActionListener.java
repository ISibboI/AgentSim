package de.isibboi.agentsim.ui.event;

/**
 * Receives input actions of the user.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface UIActionListener {
	/**
	 * The user produced some input.
	 * 
	 * @param e the event.
	 */
	void userAction(UserActionEvent e);
}