package com.gmail.leonidandand.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

class MatrixIterator<T> implements Iterator<T> {

	static <ElemType> MatrixIterator<ElemType> of(Matrix<ElemType> matrix) {
		return new MatrixIterator<ElemType>(matrix);
	}

	private final Dimension dimension;
	private final Matrix<T> matrix;
	private Position nextPosition;

	private MatrixIterator(Matrix<T> matrix) {
		this.matrix = matrix;
		this.dimension = matrix.getDimension();
		this.nextPosition = Position.withRowColumn(0, 0);
	}

	@Override
	public boolean hasNext() {
		return insideMatrix(nextPosition);
	}

	private boolean insideMatrix(Position pos) {
		return (pos.row < dimension.rows) && (pos.column < dimension.columns);
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException(
				"Position " + nextPosition + " outside of matrix with dimension " + dimension
			);
		}
		T next = matrix.get(nextPosition);
		nextPosition = nextPosition();
		return next;
	}

	private Position nextPosition() {
		if (nextPosition.column + 1 < dimension.columns) {
			return Position.withRowColumn(nextPosition.row, nextPosition.column + 1);
		} else {
			return Position.withRowColumn(nextPosition.row + 1, 0);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
