package de.isibboi.agentsim.game;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

		/**
		 * Creates a new {@link CollisionExecutor} to evaluate the collision between the given entities.
		 * 
		 * @param a The first entity.
		 * @param b The second entity.
		 */
		public CollisionExecutor(final Entity a, final Entity b) {
			_a = a;
			_b = b;
		}

		@Override
		public void run() {
			synchronized (_a) {
				synchronized (_b) {
					_a.collideWith(_b);
				}
			}
		}
	}

	private final Logger _log = LogManager.getLogger(getClass());

	private final ThreadPoolExecutor _executor;
	private final Collection<Future<?>> _futures;

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
	public void startCollision() {
		// Do nothing.
	}

	@Override
	public void collide(final Entity a, final Entity b) {
		_futures.add(_executor.submit(new CollisionExecutor(a, b)));
	}

	@Override
	public void finishBlock() {
		// Do nothing.
	}

	@Override
	public void finishCollision() {
		try {
			for (Future<?> future : _futures) {
				future.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			_log.error("Could not finish collision evaluation", e);
		}
	}
}