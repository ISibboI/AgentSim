package de.isibboi.agentsim.entities.ai;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.isibboi.agentsim.game.entities.ai.ArrayKnowledgeMap;
import de.isibboi.agentsim.game.entities.ai.HashKnowledgeMap;
import de.isibboi.agentsim.game.entities.ai.KnowledgeMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * Tests if the knowledge exchange works correctly.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class KnowledgeMapTest {
	private KnowledgeMap<Integer> _knowledgeMapA;
	private KnowledgeMap<Integer> _knowledgeMapB;

	/**
	 * Tests the {@link ArrayKnowledgeMap}.
	 */
	@Test
	public void testArrayKnowledgeMap() {
		_knowledgeMapA = new ArrayKnowledgeMap<>(10, 10);
		_knowledgeMapB = new ArrayKnowledgeMap<>(10, 10);

		testMap();
	}

	/**
	 * Tests the {@link HashKnowledgeMap}.
	 */
	@Test
	public void testHashKnowledgeMap() {
		_knowledgeMapA = new HashKnowledgeMap<>();
		_knowledgeMapB = new HashKnowledgeMap<>();

		testMap();
	}

	/**
	 * Tests the current knowledge map.
	 */
	private void testMap() {
		_knowledgeMapA.updateLocation(new Point(2, 2), 4, 2);
		_knowledgeMapA.updateLocation(new Point(2, 3), 5, 2);
		_knowledgeMapA.updateLocation(new Point(2, 4), 6, 2);
		_knowledgeMapA.updateLocation(new Point(2, 0), 7, 2);
		_knowledgeMapA.updateLocation(new Point(0, 2), 8, 4);
		_knowledgeMapA.updateLocation(new Point(3, 2), 9, 4);
		_knowledgeMapA.updateLocation(new Point(5, 2), 1, 4);
		_knowledgeMapA.updateLocation(new Point(6, 2), 2, 2);
		_knowledgeMapA.updateLocation(new Point(1, 2), 3, 2);

		_knowledgeMapB.updateLocation(new Point(2, 2), 23, 5);
		_knowledgeMapB.updateLocation(new Point(2, 3), 24, 3);
		_knowledgeMapB.updateLocation(new Point(2, 4), 25, 1);
		_knowledgeMapB.updateLocation(new Point(2, 0), 26, 3);
		_knowledgeMapB.updateLocation(new Point(0, 2), 27, 2);
		_knowledgeMapB.updateLocation(new Point(3, 2), 28, 3);
		_knowledgeMapB.updateLocation(new Point(5, 2), 29, 2);
		_knowledgeMapB.updateLocation(new Point(6, 2), 21, 1);
		_knowledgeMapB.updateLocation(new Point(1, 2), 22, 6);

		_knowledgeMapA.exchangeInformation(_knowledgeMapB);

		assertEquals(23, _knowledgeMapA.getLocationKnowledge(new Point(2, 2)).intValue());
		assertEquals(24, _knowledgeMapA.getLocationKnowledge(new Point(2, 3)).intValue());
		assertEquals(6, _knowledgeMapA.getLocationKnowledge(new Point(2, 4)).intValue());
		assertEquals(26, _knowledgeMapA.getLocationKnowledge(new Point(2, 0)).intValue());
		assertEquals(8, _knowledgeMapA.getLocationKnowledge(new Point(0, 2)).intValue());
		assertEquals(9, _knowledgeMapA.getLocationKnowledge(new Point(3, 2)).intValue());
		assertEquals(1, _knowledgeMapA.getLocationKnowledge(new Point(5, 2)).intValue());
		assertEquals(2, _knowledgeMapA.getLocationKnowledge(new Point(6, 2)).intValue());
		assertEquals(22, _knowledgeMapA.getLocationKnowledge(new Point(1, 2)).intValue());

		assertEquals(23, _knowledgeMapB.getLocationKnowledge(new Point(2, 2)).intValue());
		assertEquals(24, _knowledgeMapB.getLocationKnowledge(new Point(2, 3)).intValue());
		assertEquals(6, _knowledgeMapB.getLocationKnowledge(new Point(2, 4)).intValue());
		assertEquals(26, _knowledgeMapB.getLocationKnowledge(new Point(2, 0)).intValue());
		assertEquals(8, _knowledgeMapB.getLocationKnowledge(new Point(0, 2)).intValue());
		assertEquals(9, _knowledgeMapB.getLocationKnowledge(new Point(3, 2)).intValue());
		assertEquals(1, _knowledgeMapB.getLocationKnowledge(new Point(5, 2)).intValue());
		assertEquals(2, _knowledgeMapB.getLocationKnowledge(new Point(6, 2)).intValue());
		assertEquals(22, _knowledgeMapB.getLocationKnowledge(new Point(1, 2)).intValue());

		assertEquals(5, _knowledgeMapA.getLocationKnowledgeAge(new Point(2, 2)));
		assertEquals(3, _knowledgeMapA.getLocationKnowledgeAge(new Point(2, 3)));
		assertEquals(2, _knowledgeMapA.getLocationKnowledgeAge(new Point(2, 4)));
		assertEquals(3, _knowledgeMapA.getLocationKnowledgeAge(new Point(2, 0)));
		assertEquals(4, _knowledgeMapA.getLocationKnowledgeAge(new Point(0, 2)));
		assertEquals(4, _knowledgeMapA.getLocationKnowledgeAge(new Point(3, 2)));
		assertEquals(4, _knowledgeMapA.getLocationKnowledgeAge(new Point(5, 2)));
		assertEquals(2, _knowledgeMapA.getLocationKnowledgeAge(new Point(6, 2)));
		assertEquals(6, _knowledgeMapA.getLocationKnowledgeAge(new Point(1, 2)));

		assertEquals(5, _knowledgeMapB.getLocationKnowledgeAge(new Point(2, 2)));
		assertEquals(3, _knowledgeMapB.getLocationKnowledgeAge(new Point(2, 3)));
		assertEquals(2, _knowledgeMapB.getLocationKnowledgeAge(new Point(2, 4)));
		assertEquals(3, _knowledgeMapB.getLocationKnowledgeAge(new Point(2, 0)));
		assertEquals(4, _knowledgeMapB.getLocationKnowledgeAge(new Point(0, 2)));
		assertEquals(4, _knowledgeMapB.getLocationKnowledgeAge(new Point(3, 2)));
		assertEquals(4, _knowledgeMapB.getLocationKnowledgeAge(new Point(5, 2)));
		assertEquals(2, _knowledgeMapB.getLocationKnowledgeAge(new Point(6, 2)));
		assertEquals(6, _knowledgeMapB.getLocationKnowledgeAge(new Point(1, 2)));
	}
}
