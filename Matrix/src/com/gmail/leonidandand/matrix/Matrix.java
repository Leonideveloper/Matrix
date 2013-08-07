package com.gmail.leonidandand.matrix;

public interface Matrix<T> {
    boolean contains(T elem);
    Dimension getDimension();
	T get(Position pos);
	void set(Position pos, T value);
	void swap(Position pos1, Position pos2);
	void fill(T value);
	void forEach(OnEachHandler<T> onEachHandler);
}
