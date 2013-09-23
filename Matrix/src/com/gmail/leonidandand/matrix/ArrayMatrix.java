package com.gmail.leonidandand.matrix;

import java.util.Iterator;



public class ArrayMatrix<T> implements Matrix<T> {
	private final Dimension dim;
	private final int numberOfElements;
	private final T[] values;

	
	public static <ElemType> ArrayMatrix<ElemType> copyOf(Matrix<ElemType> other) {
		return new ArrayMatrix<ElemType>(other);
	}
	
	private ArrayMatrix(Matrix<T> other) {
		this(other.getDimension());
		other.forEach(new OnEachHandler<T>() {
			@Override
			public void handle(Position pos, T elem) {
				set(pos, elem);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public ArrayMatrix(Dimension dim) {
		this.dim = dim;
		this.numberOfElements = dim.rows * dim.columns;
		this.values = (T[]) new Object[numberOfElements];
	}

	@Override
    public boolean contains(T elem) {
		for (int i = 0; i < numberOfElements; ++i) {
			if (elementsAreEqual(elem, values[i])) {
				return true;
			}
		}
		return false;
    }

    private static boolean elementsAreEqual(Object e1, Object e2) {
        return (e1 == e2) ||
               ((e1 != null) && (e1.equals(e2))) ||
               ((e2 != null) && (e2.equals(e1)));
    }

	@Override
    public boolean containsAll(Iterable<T> elements) {
		for (T each : elements) {
			if (!contains(each)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int count(final T toCalculateCount) {
		final Counter counter = new Counter(0);
		forEach(new OnEachHandler<T>() {
			@Override
			public void handle(Position pos, T elem) {
				if (elementsAreEqual(elem, toCalculateCount)) {
					counter.increaseByOne();
				}
			}
		});
		return counter.getCount();
	}

	@Override
	public Dimension getDimension() {
		return dim;
	}

	@Override
	public T get(Position pos) {
		checkPosition(pos);
		return values[indexByPosition(pos)];
	}
	
	@Override
	public void set(Position pos, T value) {
		checkPosition(pos);
		values[indexByPosition(pos)] = value;
	}

	private int indexByPosition(Position pos) {
		return pos.row * dim.columns + pos.column;
	}
	
	private void checkPosition(Position pos) {
		checkPosition(pos.row, pos.column);
	}

	private void checkPosition(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(stringByRowColumn(row, column));
		}
		if (row >= dim.rows || column >= dim.columns) {
			throw new IndexOutOfBoundsException(stringByRowColumn(row, column));
		}
	}
	
	private String stringByRowColumn(int row, int column) {
		return "[" + row + ", " + column + "]";
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
		for (int i = 0; i < numberOfElements; ++i) {
			onEachHandler.handle(positionByIndex(i), values[i]);
		}
	}
	
	private Position positionByIndex(int index) {
		int row = index / dim.columns;
		int column = index % dim.columns;
		return Position.withRowColumn(row, column);
	}

	@Override
	public void swap(Position pos1, Position pos2) {
		T temp = get(pos1);
		set(pos1, get(pos2));
		set(pos2, temp);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if ((obj == null) || !(obj instanceof Matrix<?>)) {
			return false;
		}
		Matrix<?> other = (Matrix<?>) obj;
		if (!dim.equals(other.getDimension())) {
			return false;
		}
		for (int i = 0; i < numberOfElements; ++i) {
			Position pos = positionByIndex(i);
			if (!elementsAreEqual(get(pos), other.get(pos))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int totalHashCode = 0;
		for (int i = 0; i < numberOfElements; ++i) {
			totalHashCode += hashCodeOfElementByIndex(i);
		}
		return totalHashCode + dim.hashCode();
	}
	
	private int hashCodeOfElementByIndex(int index) {
		T element = values[index];
		return element != null ? element.hashCode() : 0;
	}

	@Override
	public Iterator<T> iterator() {
		return MatrixIterator.of(this);
	}
}
