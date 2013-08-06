package com.gmail.leonidandand.matrix;

public interface Matrix<T> {
    boolean contains(T elem);
    Dimension getDimension();
	int rows();
	int columns();
	T get(Position pos);
	T get(int row, int column);
	void set(Position pos, T value);
	void set(int row, int column, T value);
	void fill(T value);
	void forEach(OnEachHandler<T> onEachHandler);
	void swap(Position pos1, Position pos2);
}
