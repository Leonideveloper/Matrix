package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.gmail.leonidandand.matrix.Matrix;
import com.gmail.leonidandand.matrix.OnEachHandler;
import com.gmail.leonidandand.matrix.Position;

public class TestMatrix {
	private static final Integer VALUE = 5;
	private static final int ROWS = 10;
	private static final int COLUMNS = 20;
	
	private Matrix<Integer> matrix;

	@Before
	public void setUp() {
		this.matrix = new Matrix<Integer>(ROWS, COLUMNS);
	}

	@Test
	public void testMatrix() {
		fillMatrix(matrix);
		for (int row = 0; row < ROWS; ++row) {
			for (int column = 0; column < COLUMNS; ++column) {
				assertEquals(elementForPosition(row, column), matrix.get(row, column));
			}
		}
	}
	
	private void fillMatrix(final Matrix<Integer> matrixToFill) {
		for (int row = 0; row < matrixToFill.rows; ++row) {
			for (int column = 0; column < matrixToFill.columns; ++column) {
				matrixToFill.set(row, column, elementForPosition(row, column));
			}
		}
	}
	
	private Integer elementForPosition(int row, int column) {
		return row * column;
	}
	
	@Test
	public void testMatrixCopyConstructor() {
		fillMatrix(matrix);
		Matrix<Integer> copy = new Matrix<Integer>(matrix);
		assertEquals(matrix.rows, copy.rows);
		assertEquals(matrix.columns, copy.columns);
		for (int row = 0; row < copy.rows; ++row) {
			for (int column = 0; column < copy.columns; ++column) {
				assertEquals(matrix.get(row, column), copy.get(row, column));
			}
		}
	}

	@Test
	public void testInitValueIsNull() {
		assertNull(matrix.get(0, 0));
	}

	@Test
	public void testDimensionOfMatrix() {
		assertEquals(ROWS, matrix.rows);
		assertEquals(COLUMNS, matrix.columns);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeDimension() {
		new Matrix<Integer>(1, -1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDimensionIsZero() {
		new Matrix<Integer>(0, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetByNegativePosition() {
		matrix.set(-1, 0, VALUE);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSetByOutOfBoundsPosition() {
		matrix.set(0, COLUMNS + 1, VALUE);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetByNegativePosition() {
		matrix.get(1, -1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetByOutOfBoundsPosition() {
		matrix.get(ROWS + 1, 0);
	}

	@Test
	public void testSwap() {
		Matrix<Integer> matrix = new Matrix<Integer>(2, 2);
		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(1, 1);
		Integer val1 = 1;
		Integer val2 = 2;
		matrix.set(pos1, val2);
		matrix.set(pos2, val1);
		matrix.swap(pos1, pos2);
		assertEquals(val2, matrix.get(pos2));
		assertEquals(val1, matrix.get(pos1));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSwapIllegalArguments() {
		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(0, -1);
		matrix.swap(pos1, pos2);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSwapPositionOutOfBounds() {
		Position pos1 = new Position(0, COLUMNS + 1);
		Position pos2 = new Position(0, 0);
		matrix.swap(pos1, pos2);
	}

	@Test
	public void testEquals() {
		Matrix<Integer> matrix1 = new Matrix<Integer>(1, 2);
		matrix1.set(0, 0, VALUE);
		matrix1.set(0, 1, VALUE);
		Matrix<Integer> matrix2 = new Matrix<Integer>(1, 2);
		matrix2.set(0, 0, VALUE);
		matrix2.set(0, 1, VALUE);
		assertTrue(matrix1.equals(matrix2));
		matrix2.set(0, 0, VALUE + 1);
		assertFalse(matrix1.equals(matrix2));
	}

	@Test
	public void testEqualityWithNull() {
		assertFalse(matrix.equals(null));
	}

	@Test
	public void testEqualsDifferentClass() {
		assertFalse(matrix.equals(Integer.valueOf(1)));
	}

	@Test
	public void testEqualsSameClassDifferentParameter() {
		Matrix<Integer> integerMatrix = new Matrix<Integer>(1, 1);
		integerMatrix.set(0, 0, Integer.valueOf(0));
		Matrix<Boolean> booleanMatrix = new Matrix<Boolean>(1, 1);
		booleanMatrix.set(0, 0, Boolean.valueOf(false));
		assertFalse(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEqualityWithNullElements() {
		assertEquals(matrix, new Matrix<Integer>(ROWS, COLUMNS));
		
		Matrix<Boolean> falseElements = getMatrixInitializedByFalse();
		Matrix<Boolean> nullElements = new Matrix<Boolean>(falseElements.rows, falseElements.columns);
		assertFalse(falseElements.equals(nullElements));
	}

	@Test
	public void testForEach_allElementsAreProcessedExactlyOnce() {
		final Matrix<Boolean> flags = getMatrixInitializedByFalse();
		flags.forEach(new OnEachHandler<Boolean>() {
			@Override
			public void handle(Position pos, Boolean elem) {
				assertFalse(elem);
				assertFalse(flags.get(pos));
				flags.set(pos, true);
			}
		});
		for (int row = 0; row < flags.rows; ++row) {
			for (int column = 0; column < flags.columns; ++column) {
				assertTrue(flags.get(row, column));
			}
		}
	}
	
	private Matrix<Boolean> getMatrixInitializedByFalse() {
		final Matrix<Boolean> flags = new Matrix<Boolean>(5, 8);
		for (int row = 0; row < flags.rows; ++row) {
			for (int column = 0; column < flags.columns; ++column) {
				flags.set(row, column, false);
			}
		}
		return flags;
	}

	@Test
	public void testForEach_Order_LeftToRight_UpToDown() {
		final Matrix<Boolean> flags = getMatrixInitializedByFalse();
		flags.forEach(new OnEachHandler<Boolean>() {
			@Override
			public void handle(Position pos, Boolean elem) {
				if (!pos.equals(new Position(0, 0))) {
					assertPreviousElementWasProcessed(flags, pos);
				}
				flags.set(pos, true);
			}
		});
	}

	private void assertPreviousElementWasProcessed(Matrix<Boolean> matrix, Position pos) {
		Position positionBefore = positionBefore(matrix.rows, matrix.columns, pos);
		assertTrue(matrix.get(positionBefore));
	}

	private Position positionBefore(int rows, int columns, Position pos) {
		if (pos.column - 1 < 0) {
			return new Position(pos.row - 1, columns - 1);
		} else {
			return new Position(pos.row, pos.column - 1);
		}
	}
}
