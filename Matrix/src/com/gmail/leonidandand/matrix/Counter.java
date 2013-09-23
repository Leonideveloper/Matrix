package com.gmail.leonidandand.matrix;

public class Counter {
	private int count;

	public Counter() {
		count = 0;
	}
	
	public Counter(int initCount) {
		count = initCount;
	}
	
	public void increaseByOne() {
		count++;
	}
	
	public int getCount() {
		return count;
	}
}
