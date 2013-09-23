package com.gmail.leonidandand.matrix;

public interface ReadOnlyMatrix<T> {
    Dimension getDimension();
    boolean contains(T elem);
	boolean containsAll(Iterable<T> elements);
	int count(T elem);
	void forEach(OnEachHandler<T> onEachHandler);
	T get(Position pos);
}
