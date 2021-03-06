package org.github.tgn.expanse.util;

import java.util.ArrayList;

public class WeightedList<T> {
	private final ArrayList<T> values = new ArrayList<>();
	private final ArrayList<Integer> weights = new ArrayList<>();
	private int size;
	public void add(T obj, int weight) {
		this.size += weight;
		this.values.add(obj);
		this.weights.add(weight);
	}

	public T get(int index) {
		System.out.println(index);
		int counter = 0;
		for (int weight : this.weights) {
			System.out.println(index);
			if(index-weight < 0)
				return this.values.get(counter);
			counter++;
			index-=weight;
		}
		throw new ArrayIndexOutOfBoundsException("ono");
	}

	public int size() {
		return size;
	}
}
