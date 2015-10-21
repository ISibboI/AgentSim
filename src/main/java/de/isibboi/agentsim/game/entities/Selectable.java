package de.isibboi.agentsim.game.entities;

/**
 * An object that can be selected by the user.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface Selectable {

	/**
	 * Sets the selected status of this object.
	 * 
	 * @param isSelected True if the object is selected, false otherwise.
	 */
	void setSelected(boolean isSelected);

	/**
	 * Returns the selected status of this object.
	 * @return True if the object is selected, false otherwise.
	 */
	boolean isSelected();
}
