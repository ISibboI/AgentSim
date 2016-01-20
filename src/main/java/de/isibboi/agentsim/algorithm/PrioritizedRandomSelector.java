package de.isibboi.agentsim.algorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

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
public class PrioritizedRandomSelector<T extends PriorityOrdered> implements PrioritySelector<T> {
	private final SortedMultiset<T> _data = TreeMultiset.create(new PriorityComparator());
	private final Random _random = new Random();

	private int _prioritySum;

	@Override
	public void add(final T element) {
		if (element.getPriority() < 0) {
			throw new IllegalArgumentException("Priority is negative.");
		}

		int newPrioritySum = _prioritySum + element.getPriority();

		if (newPrioritySum < _prioritySum) {
			throw new IllegalArgumentException("Priority is too high. Overflow detected.");
		}

		_prioritySum = newPrioritySum;
		_data.add(element);
	}

	@Override
	public T select() {
		if (_data.isEmpty()) {
			return null;
		}

		int ticket = _random.nextInt(_prioritySum);
		int currentSum = 0;

		for (T o : _data) {
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
		return Collections.unmodifiableCollection(_data);
	}

	@Override
	public void updatePriority(final T element, final int priority) {
		if (!_data.contains(element)) {
			throw new IllegalArgumentException("The given element is not in this data structure.");
		}

		if (priority < 0) {
			throw new IllegalArgumentException("The priority must not be negative.");
		}

		int currentPriority = element.getPriority();
		_prioritySum += priority - currentPriority;
		element.setPriority(priority);
	}
}