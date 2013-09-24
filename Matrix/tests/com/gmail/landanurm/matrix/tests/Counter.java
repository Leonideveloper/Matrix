package com.gmail.landanurm.matrix.tests;

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

	void reset(int initCount) {
		count = initCount;
	}
}
