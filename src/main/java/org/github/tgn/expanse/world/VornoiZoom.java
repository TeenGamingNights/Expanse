package org.github.tgn.expanse.world;

import java.awt.Point;

/**
 * my version of a vornoi zoom, idk if this is actually how you implement it
 */
public class VornoiZoom {
	private static final long GOLDEN_GAMMA = 0x9e3779b97f4a7c15L;
	public final long seed;
	public final int scale;

	public VornoiZoom(long seed, int scale) {
		this.seed = seed;
		this.scale = scale;
	}

	/**
	 * get a random int value for the given coordinates using the ghetto voronio zoom algorithm I made
	 * @return 0-intmax
	 */
	public int zoom(int x, int y) {
		SampleResult[] results = this.results(x, y);
		Point current = new Point(x, y);
		double closest = Double.POSITIVE_INFINITY;
		long seed = 0;
		for (SampleResult result : results) {
			double distance = result.distanceSq(current);
			if(distance < closest) {
				closest = distance;
				seed = result.seed;
			}
		}

		return next(seed) & Integer.MAX_VALUE;
	}

	private SampleResult[] results(int x, int y) {
		return new SampleResult[]{ // samples
		this.sample(x - this.scale, y - this.scale), // north west
		this.sample(x - this.scale, y), // west
		this.sample(x - this.scale, y + this.scale), // south west
		this.sample(x, y - this.scale), // north
		this.sample(x, y), // mid
		this.sample(x, y + this.scale), // south
		this.sample(x + this.scale, y - this.scale), // north east
		this.sample(x + this.scale, y), // east
		this.sample(x + this.scale, y + this.scale) // south east
		};
	}

	private SampleResult sample(int x, int y) {
		long seed = this.seed(Math.floorDiv(x, this.scale), Math.floorDiv(y, this.scale));
		int nextX = next(seed) & Integer.MAX_VALUE; // remove negative
		seed = nextSeed(seed);
		int nextY = next(seed) & Integer.MAX_VALUE; // remove negative
		seed = nextSeed(seed);
		return new SampleResult(nextX % this.scale + floor(x, this.scale), nextY % this.scale + floor(y, this.scale), seed);
	}

	private long seed(int x, int y) {
		return ((long) x << 32 | y) ^ this.seed;
	}

	private static int next(long seed) {
		long z = seed + GOLDEN_GAMMA;
		z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L;
		return (int) (((z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
	}

	private static long nextSeed(long seed) {
		return seed + GOLDEN_GAMMA;
	}

	private static int floor(int val, int scale) {
		return Math.floorDiv(val, scale) * scale;
	}

	private static class SampleResult extends Point {
		public long seed;
		public SampleResult(int x, int y, long seed) {
			super(x, y);
			this.seed = seed;
		}
	}
}
