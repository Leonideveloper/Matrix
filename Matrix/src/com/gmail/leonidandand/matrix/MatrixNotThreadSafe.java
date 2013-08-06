package com.gmail.leonidandand.matrix;


public class MatrixNotThreadSafe<T> implements Matrix<T> {
	private final int rows;
	private final int columns;
	private final T[][] values;
	
	public MatrixNotThreadSafe(Dimension dim) {
		this(dim.rows, dim.columns);
	}
	
	@SuppressWarnings("unchecked")
	public MatrixNotThreadSafe(int rows, int columns) {
		if (rows <= 0 || columns <= 0) {
			throw new IllegalArgumentException();
		}
		this.rows = rows;
		this.columns = columns;
		this.values = (T[][]) new Object[rows][columns];
	}
	
	public MatrixNotThreadSafe(Matrix<T> other) {
		this(other.rows(), other.columns());
		other.forEach(new OnEachHandler<T>() {
			@Override
			public void handle(Position pos, T elem) {
				set(pos, elem);
			}
		});
	}

	@Override
    public boolean contains(T elem) {
        for (int row = 0; row < rows(); ++row) {
            for (int column = 0; column < columns(); ++column) {
                if (elementsAreEqual(elem, get(row, column))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean elementsAreEqual(Object e1, Object e2) {
        return (e1 == e2) ||
               ((e1 != null) && (e1.equals(e2))) ||
               ((e2 != null) && (e2.equals(e1)));
    }

	@Override
	public Dimension getDimension() {
		return new Dimension(rows(), columns());
	}
	
	@Override
	public int rows() {
		return rows;
	}

	@Override
	public int columns() {
		return columns;
	}

	@Override
	public T get(Position pos) {
		return get(pos.row, pos.column);
	}

	@Override
	public T get(int row, int column) {
		checkIndexes(row, column);
		return values[row][column];
	}

	private void checkIndexes(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(stringByRowColumn(row, column));
		}
		if (row >= rows() || column >= columns()) {
			throw new IndexOutOfBoundsException(stringByRowColumn(row, column));
		}
	}
	
	private String stringByRowColumn(int row, int column) {
		return "[" + row + ", " + column + "]";
	}
	
	@Override
	public void set(Position pos, T value) {
		set(pos.row, pos.column, value);
	}

	@Override
	public void set(int row, int column, T value) {
		checkIndexes(row, column);
		values[row][column] = value;
	}
	
	@Override
	public void fill(final T value) {
		forEach(new OnEachHandler<T>() {
			@Override
			public void handle(Position pos, T elem) {
				set(pos, value);
			}
		});
	}

	@Override
	public void forEach(OnEachHandler<T> onEachHandler) {
		for (int row = 0; row < rows(); ++row) {
			for (int column = 0; column < columns(); ++column) {
				Position pos = new Position(row, column);
				T elem = get(row, column);
				onEachHandler.handle(pos, elem);
			}
		}
	}

	@Override
	public void swap(Position pos1, Position pos2) {
		T temp = get(pos1);
		set(pos1, get(pos2));
		set(pos2, temp);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Matrix<?>)) {
			return false;
		}
		Matrix<?> other = (Matrix<?>) obj;
		if (this.rows() != other.rows() || this.columns() != other.columns()) {
			return false;
		}
		for (int row = 0; row < rows(); ++row) {
			for (int column = 0; column < columns(); ++column) {
				if (!elementsAreEqual(get(row, column), other.get(row, column))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int totalHashCode = 0;
		for (int row = 0; row < rows(); ++row) {
			for (int column = 0; column < columns(); ++column) {
				totalHashCode += hashCodeOfElement(row, column);
			}
		}
		return totalHashCode * (rows() + columns());
	}
	
	private int hashCodeOfElement(int row, int column) {
		T element = get(row, column);
		return element != null ? element.hashCode() : 0;
	}
}
