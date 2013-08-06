package com.gmail.leonidandand.matrix;

public interface OnEachHandler<T> {
	void handle(Position pos, T elem);
}
