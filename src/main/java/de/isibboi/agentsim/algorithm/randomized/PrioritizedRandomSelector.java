package de.isibboi.agentsim.algorithm.randomized;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import de.isibboi.agentsim.algorithm.PriorityOrdered;
import de.isibboi.agentsim.algorithm.Selector;

/**
 * A priority based random selector.
 * Elements with higher priority have a higher chance of being returned.
 * If the sum of all priorities of the elements in the set is {@code N} and the priority of one element is {@code n},
 * the probability of returning this element is {@code n/N}.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type.
 */
public class PrioritizedRandomSelector<T extends PriorityOrdered> implements Selector<T> {
	private final Map<T, T> _data = new HashMap<>();
	private final Random _random = new Random();

	private int _prioritySum;

	@Override
	public void add(final T element) {
		Objects.requireNonNull(element);

		if (element.getPriority() < 0) {
			throw new IllegalArgumentException("Priority is negative.");
		}

		int newPrioritySum = _prioritySum + element.getPriority();

		if (newPrioritySum < _prioritySum) {
			throw new IllegalArgumentException("Priority is too high. Overflow detected.");
		}

		_prioritySum = newPrioritySum;
		_data.put(element, element);
	}

	@Override
	public T select() {
		if (_data.isEmpty()) {
			return null;
		}

		int ticket = _random.nextInt(_prioritySum);
		int currentSum = 0;

		for (T o : _data.keySet()) {
			currentSum += o.getPriority();

			if (currentSum > ticket) {
				_data.remove(o);
				_prioritySum -= o.getPriority();
				return o;
			}
		}

		// If this is thrown, the priorities changed while an element was in the selector.
		throw new RuntimeException("Error selecting element.");
	}

	@Override
	public boolean isEmpty() {
		return _data.isEmpty();
	}

	@Override
	public Collection<T> getData() {
		return Collections.unmodifiableCollection(_data.keySet());
	}

	@Override
	public void update(final Iterable<? extends T> elements) {
		for (T element : elements) {
			final T oldElement = _data.get(element);

			if (oldElement == null) {
				_data.put(element, element);
				_prioritySum += element.getPriority();
			} else if (element.getLastUpdateTime() > oldElement.getLastUpdateTime()) {
				_prioritySum -= _data.remove(oldElement).getPriority();
				_data.put(element, element);
				_prioritySum += element.getPriority();
			}
		}
	}
}