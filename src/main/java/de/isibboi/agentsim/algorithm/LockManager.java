package de.isibboi.agentsim.algorithm;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Allows objects to lock other objects.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * 
 * @param <Lock> The type of the objects to lock.
 * @param <Holder> The type of the lock holders.
 */
public class LockManager<Lock, Holder> {
	private static final Logger LOG = LogManager.getLogger(LockManager.class);

	private final Multimap<Holder, Lock> _lockHolders = HashMultimap.create();
	private final Set<Lock> _locks = new HashSet<>();

	/**
	 * Tries to lock the given object.
	 * @param lock The object to lock.
	 * @param holder The lock holder.
	 * @return True if the object was locked, false if it is already locked.
	 */
	public boolean tryLock(final Lock lock, final Holder holder) {
		if (_locks.contains(lock)) {
			LOG.trace("Lock " + lock + " could not be locked by " + holder + ".");
			return false;
		}

		_locks.add(lock);
		_lockHolders.put(holder, lock);

		LOG.trace("Lock " + lock + " was locked by " + holder + ".");

		return true;
	}

	/**
	 * Unlocks the given object. Throws an @{link IllegalArgumentException} if the object is not locked or was locked by another holder.
	 * 
	 * @param lock The object to unlock.
	 * @param holder The lock holder.
	 */
	public void unlock(final Lock lock, final Holder holder) {
		if (!_lockHolders.containsEntry(holder, lock)) {
			if (_locks.contains(lock)) {
				throw new IllegalArgumentException("Object was not locked by this holder!");
			} else {
				throw new IllegalArgumentException("Object is not locked!");
			}
		}

		_lockHolders.remove(holder, lock);
		_locks.remove(lock);

		LOG.trace("Lock " + lock + " was unlocked by " + holder + ".");

	}

	/**
	 * Removes all locks held by the given holder.
	 * @param holder The lock holder.
	 */
	public void unlockAll(final Holder holder) {
		for (Lock lock : _lockHolders.removeAll(holder)) {
			_locks.remove(lock);

			LOG.trace("Lock " + lock + " was bulk-unlocked by " + holder + ".");
		}
	}

	/**
	 * Checks if the given object is locked.
	 * @param lock The object to check.
	 * @return True if the given object is locked, <code>false</code> if it is not.
	 */
	public boolean isLocked(final Lock lock) {
		return _locks.contains(lock);
	}
}
