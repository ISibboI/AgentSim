package de.isibboi.agentsim.noise;

import java.util.Random;

/*
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 *
 * This could be speeded up even further, but it's useful as it is.
 *
 * Version 2012-03-09
 *
 * This code was placed in the public domain by its original author,
 * Stefan Gustavson. You may use it as you see fit, but
 * attribution is appreciated.
 *
 */

/**
 * A simplex noise function.
 * @author Stefan Gustavson (stegu@itn.liu.se)
 * @author Peter Eastman (peastman@drizzle.stanford.edu)
 * @since 0.0.0
 */
public class SimplexNoise implements Noise { // Simplex noise in 2D, 3D and 4D
	private static Grad[] grad3 = { new Grad(1, 1), new Grad(-1, 1), new Grad(1, -1), new Grad(-1, -1),
			new Grad(1, 0), new Grad(-1, 0), new Grad(1, 0), new Grad(-1, 0), new Grad(0, 1),
			new Grad(0, -1), new Grad(0, 1), new Grad(0, -1) };

	private final short[] _p;
	// To remove the need for index wrapping, double the permutation table
	// length
	private short[] _perm = new short[512];
	private short[] _permMod12 = new short[512];

	/**
	 * Creates a new simplex noise function with the given seed.
	 * @param seed The random seed.
	 */
	public SimplexNoise(final long seed) {
		_p = new short[256];
		boolean[] used = new boolean[256];
		int count = 0;
		Random r = new Random(seed);

		while (count < 256) {
			int index = r.nextInt(256);

			if (!used[index]) {
				used[index] = true;
				_p[count++] = (short) index;
			}
		}

		for (int i = 0; i < 512; i++) {
			_perm[i] = _p[i & 255];
			_permMod12[i] = (short) (_perm[i] % 12);
		}
	}

	// Skewing and unskewing factors for 2, 3, and 4 dimensions
	private static final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
	private static final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;

	/**
	 * A floor function.
	 * @param x The parameter.
	 * @return {@code floor(x)}
	 */
	// This method is a *lot* faster than using (int)Math.floor(x)
	private int fastfloor(final double x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	/**
	 * Calculates the dot product.
	 * @param g Vector A.
	 * @param x Vector B.x.
	 * @param y Vector B.y.
	 * @return A dot B.
	 */
	private double dot(final Grad g, final double x, final double y) {
		return g._x * x + g._y * y;
	}

	@Override
	public double noise(final double xin, final double yin) {
		double n0, n1, n2; // Noise contributions from the three corners
		// Skew the input space to determine which simplex cell we're in
		double s = (xin + yin) * F2; // Hairy factor for 2D
		int i = fastfloor(xin + s);
		int j = fastfloor(yin + s);
		double t = (i + j) * G2;
		double X0 = i - t; // Unskew the cell origin back to (x,y) space
		double Y0 = j - t;
		double x0 = xin - X0; // The x,y distances from the cell origin
		double y0 = yin - Y0;
		// For the 2D case, the simplex shape is an equilateral triangle.
		// Determine which simplex we are in.
		int i1, j1; // Offsets for second (middle) corner of simplex in (i,j)
					// coords
		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} else {
			// lower triangle, XY order: (0,0)->(1,0)->(1,1)
			i1 = 0;
			j1 = 1;
		} // upper triangle, YX order: (0,0)->(0,1)->(1,1)
			// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
			// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
			// c = (3-sqrt(3))/6
		double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed
									// coords
		double y1 = y0 - j1 + G2;
		double x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y)
											// unskewed coords
		double y2 = y0 - 1.0 + 2.0 * G2;
		// Work out the hashed gradient indices of the three simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int gi0 = _permMod12[ii + _perm[jj]];
		int gi1 = _permMod12[ii + i1 + _perm[jj + j1]];
		int gi2 = _permMod12[ii + 1 + _perm[jj + 1]];
		// Calculate the contribution from the three corners
		double t0 = 0.5 - x0 * x0 - y0 * y0;
		if (t0 < 0) {
			n0 = 0.0;
		} else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad3[gi0], x0, y0); // (x,y) of grad3 used for
													// 2D gradient
		}
		double t1 = 0.5 - x1 * x1 - y1 * y1;
		if (t1 < 0) {
			n1 = 0.0;
		} else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
		}
		double t2 = 0.5 - x2 * x2 - y2 * y2;
		if (t2 < 0) {
			n2 = 0.0;
		} else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
		}
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to return values in the interval [-1,1].
		return 70.0 * (n0 + n1 + n2);
	}

	// Inner class to speed upp gradient computations
	// (array access is a lot slower than member access)
	private static class Grad {
		double _x;
		double _y;

		/**
		 * Creates a new gradient with the given values.
		 * @param x The x value.
		 * @param y The y value.
		 */
		Grad(final double x, final double y) {
			this._x = x;
			this._y = y;
		}
	}
}
