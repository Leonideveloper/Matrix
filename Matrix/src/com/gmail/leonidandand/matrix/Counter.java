package com.gmail.leonidandand.matrix;

class Counter {
	private int count;

	Counter() {
		count = 0;
	}
	
	Counter(int initCount) {
		count = initCount;
	}
	
	void increaseByOne() {
		count++;
	}
	
	int getCount() {
		return count;
	}
}
