package de.isibboi.agentsim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Some utility functions.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public final class Util {
	private static long seedCount = 0;

	/**
	 * Utility class.
	 */
	private Util() {
	}

	/**
	 * Generate {@code amount} distinct random numbers.
	 * The resulting list is sorted.
	 * 
	 * @param amount The amount of numbers to generate.
	 * @param max The exclusive maximum of the number range.
	 * @return A sorted list of {@code amount} distinct equally distributed random numbers from [0, amount).
	 */
	public static List<Integer> getSortedDistinctRandomNumbers(final int amount, final int max) {
		if (amount > max) {
			throw new IllegalArgumentException("amount must be lower than or equal to max!");
		}

		SortedSet<Integer> result = new TreeSet<>();
		Random r = new Random();
		r.setSeed(seedCount++ + (amount << 20) + (max << 30));

		while (result.size() < amount) {
			result.add(r.nextInt(max));
		}

		return new ArrayList<>(result);
	}
}