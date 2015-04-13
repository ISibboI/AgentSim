package de.isibboi.agentsim.algorithm;

import org.junit.Before;

/**
 * Tests if the {@link BidirectionalHashMultimap} works correctly.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class BidirectionalHashMultimapTest {
	private BidirectionalHashMultimap<Integer, Integer> _map;

	/**
	 * Creates a multimap with some entries.
	 */
	@Before
	public void setUp() {
		_map = new BidirectionalHashMultimap<>();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				_map.put(i, j);
			}
		}
	}
}
