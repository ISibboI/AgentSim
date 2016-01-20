package de.isibboi.agentsim.game.entities.ai.intends;

import de.isibboi.agentsim.algorithm.PriorityOrdered;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;

/**
 * Intends are the layer above the tasks.
 * While a task is an algorithm that controls the goblin, the intend is like an idea what needs to be achieved.
 * Tasks focus on how should something be achieved, intends focus on what should be achieved.
 * 
 * Intends must define the equals relation in a way that two intends are equal if the thing they try to achieve is equal.
 * Two intends must not be equal if they only differ in priority or age.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface Intend extends PriorityOrdered {
	/**
	 * Generates a task sequence for executing this intend.
	 * The task sequence is only valid if it is executed immediately.
	 * @param goblin The goblin that should execute the intend.
	 * @return A task sequence.
	 */
	Iterable<Task> execute(Goblin goblin);
}