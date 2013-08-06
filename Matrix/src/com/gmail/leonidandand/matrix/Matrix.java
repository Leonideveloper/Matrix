package com.gmail.leonidandand.matrix;


public class Matrix<T> {
	public final int rows;
	public final int columns;
	private final T[][] values;

	
	public Matrix(Dimension dim) {
		this(dim.rows, dim.columns);
	}
	
	@SuppressWarnings("unchecked")
	public Matrix(int rows, int columns) {
		if (rows <= 0 || columns <= 0) {
			throw new IllegalArgumentException();
		}
		this.rows = rows;
		this.columns = columns;
		this.values = (T[][]) new Object[rows][columns];
	}
	
	public Matrix(Matrix<T> other) {
		this(other.rows, other.columns);
		other.forEach(new OnEachHandler<T>() {
			@Override
			public void handle(Position pos, T elem) {
				set(pos, elem);
			}
		});
	}

	public void set(Position pos, T value) {
		set(pos.row, pos.column, value);
	}

	public void set(int row, int column, T value) {
		checkIndexes(row, column);
		values[row][column] = value;
	}
	
	private void checkIndexes(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(stringByRowColumn(row, column));
		}
		if (row >= rows || column >= columns) {
			throw new IndexOutOfBoundsException(stringByRowColumn(row, column));
		}
	}
	
	private String stringByRowColumn(int row, int column) {
		return "[" + row + ", " + column + "]";
	}

	public T get(Position pos) {
		return get(pos.row, pos.column);
	}

	public T get(int row, int column) {
		checkIndexes(row, column);
		return values[row][column];
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Matrix<?>)) {
			return false;
		}
		Matrix<?> other = (Matrix<?>) obj;
		if (other.rows != rows || other.columns != columns) {
			return false;
		}
		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				if (!elementsEqual(get(row, column), other.get(row, column))) {
					return false;
				}
			}
		}
		return true;
	}

    private boolean elementsEqual(Object e1, Object e2) {
        return (e1 == e2) ||
               ((e1 != null) && (e1.equals(e2))) ||
               ((e2 != null) && (e2.equals(e1)));
    }

	@Override
	public int hashCode() {
		int hashCode1 = get(0, 0).hashCode();
		int hashCode2 = get(rows - 1, columns - 1).hashCode();
		return hashCode1 * hashCode2 + rows * hashCode1 + columns * hashCode2;
	}

	public void swap(Position pos1, Position pos2) {
		T temp = get(pos1);
		set(pos1, get(pos2));
		set(pos2, temp);
	}

	public void forEach(OnEachHandler<T> onEachHandler) {
		for (int row = 0; row < this.rows; ++row) {
			for (int column = 0; column < this.columns; ++column) {
				Position pos = new Position(row, column);
				T elem = get(row, column);
				onEachHandler.handle(pos, elem);
			}
		}
	}

    public boolean contains(T elem) {
        for (int row = 0; row < this.rows; ++row) {
            for (int column = 0; column < this.columns; ++column) {
                if (elementsEqual(elem, get(row, column))) {
                    return true;
                }
            }
        }
        return false;
    }
}
