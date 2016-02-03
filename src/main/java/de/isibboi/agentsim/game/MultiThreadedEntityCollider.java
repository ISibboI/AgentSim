package de.isibboi.agentsim.game;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entity;

/**
 * @author Sebastian Schmidt
 * @since 
 *
 */
public class MultiThreadedEntityCollider implements EntityCollider {
	private static class CollisionExecutor implements Runnable {
		private final Entity _a;
		private final Entity _b;
		private final int _currentTick;

		/**
		 * Creates a new {@link CollisionExecutor} to evaluate the collision between the given entities.
		 * 
		 * @param a The first entity.
		 * @param b The second entity.
		 * @param currentTick The current tick.
		 */
		CollisionExecutor(final Entity a, final Entity b, final int currentTick) {
			_a = a;
			_b = b;
			_currentTick = currentTick;
		}

		@Override
		public void run() {
			synchronized (_a) {
				synchronized (_b) {
					_a.collideWith(_b, _currentTick);
				}
			}
		}
	}

	private final ThreadPoolExecutor _executor;
	private final Collection<Future<?>> _futures;

	private volatile boolean _shutdown = false;
	private volatile boolean _isColliding = false;
	private volatile int _currentTick;

	/**
	 * Creates a new {@link MultiThreadedEntityCollider}.
	 * The amount of threads is read from the settings.
	 * 
	 * @param settings The settings.
	 */
	public MultiThreadedEntityCollider(final Settings settings) {
		final int threadAmount = settings.getInt(Settings.CORE_UPDATE_COLLISION_THREAD_COUNT);
		_executor = new ThreadPoolExecutor(threadAmount, threadAmount, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		_executor.prestartAllCoreThreads();
		_futures = new LinkedList<>();

	}

	@Override
	public synchronized void startCollision(final int tick) {
		_isColliding = true;
		_currentTick = tick;
	}

	@Override
	public void collide(final Entity a, final Entity b) {
		_futures.add(_executor.submit(new CollisionExecutor(a, b, _currentTick)));
	}

	@Override
	public void finishBlock() {
		// Do nothing.
	}

	@Override
	public synchronized void finishCollision() {
		try {
			for (Future<?> future : _futures) {
				future.get();
			}
		} catch (InterruptedException | ExecutionException e) {
		}

		if (_shutdown) {
			shutdownExecutor();
		}

		_isColliding = false;
	}

	/**
	 * Shuts the pooled executor down.
	 */
	private void shutdownExecutor() {
		_executor.shutdown();
	}

	@Override
	public synchronized void shutdown() {
		_shutdown = true;

		if (!_isColliding) {
			shutdownExecutor();
		}
	}
}
