package de.isibboi.agentsim;

/**
 * @author Sebastian Schmidt
 * @since 
 *
 */
public interface FrameRateStabilizer {

	/**
	 * The transition parameter determines where the entity is exactly drawn.
	 * For zero, the entity should be drawn at the location from the last update, for one, it should be drawn at the current location.
	 * Values between zero and one should be used for linear interpolation between old and new location.
	 * 
	 * @return Returns the transition value for the current update.
	 */
	double getTransition();

	/**
	 * Decides if the next step should be an update or a render step. Waits if necessary to keep the desired frame and update rate.
	 * 
	 * @return True if a new frame should be rendered, false if the game should be updated.
	 */
	boolean stabilize();
}