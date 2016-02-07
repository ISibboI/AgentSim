package de.isibboi.agentsim.algorithm;

/**
 * A type that stores information that is temporal variant.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface TemporalVariant {
	/**
	 * Returns the time the information stored by this object was recorded.
	 * 
	 * @return The time the information stored by this object was recorded.
	 */
	int getInformationRecordTime();
}