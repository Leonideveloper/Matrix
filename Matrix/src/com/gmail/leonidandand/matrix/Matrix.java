package com.gmail.leonidandand.matrix;

public interface Matrix<T> {
    Dimension getDimension();
    boolean contains(T elem);
	int count(T elem);
	void fill(T value);
	void forEach(OnEachHandler<T> onEachHandler);
	T get(Position pos);
	void set(Position pos, T value);
	void swap(Position pos1, Position pos2);
}
