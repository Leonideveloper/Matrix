package com.gmail.landanurm.matrix;

public interface OnEachHandler<T> {
	void handle(Position pos, T elem);
}
