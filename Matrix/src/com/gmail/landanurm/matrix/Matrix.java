package com.gmail.landanurm.matrix;

public interface Matrix<T> extends ReadOnlyMatrix<T> {
	void fill(T value);
	void set(Position pos, T value);
	void swap(Position pos1, Position pos2);
}
