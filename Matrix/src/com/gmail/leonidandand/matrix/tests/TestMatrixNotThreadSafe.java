package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.gmail.leonidandand.matrix.Dimension;
import com.gmail.leonidandand.matrix.Matrix;
import com.gmail.leonidandand.matrix.MatrixNotThreadSafe;
import com.gmail.leonidandand.matrix.OnEachHandler;
import com.gmail.leonidandand.matrix.Position;

public class TestMatrixNotThreadSafe {
	private static final Integer VALUE = 5;
	private static final int ROWS = 10;
	private static final int COLUMNS = 20;
	
	private Matrix<Integer> matrix;

	@Before
	public void setUp() {
		this.matrix = new MatrixNotThreadSafe<Integer>(ROWS, COLUMNS);
	}
	
	@Test
	public void testMatrixCopyConstructor() {
		fillMatrix(matrix);
		Matrix<Integer> copy = new MatrixNotThreadSafe<Integer>(matrix);
		assertEquals(matrix.rows(), copy.rows());
		assertEquals(matrix.columns(), copy.columns());
		for (int row = 0; row < copy.rows(); ++row) {
			for (int column = 0; column < copy.columns(); ++column) {
				assertEquals(matrix.get(row, column), copy.get(row, column));
			}
		}
	}
	
	private void fillMatrix(final Matrix<Integer> matrixToFill) {
		for (int row = 0; row < matrixToFill.rows(); ++row) {
			for (int column = 0; column < matrixToFill.columns(); ++column) {
				matrixToFill.set(row, column, elementForPosition(row, column));
			}
		}
	}
	
	private Integer elementForPosition(int row, int column) {
		return row * column;
	}

	@Test
	public void testInitValueIsNull() {
		assertNull(matrix.get(0, 0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeDimension() {
		new MatrixNotThreadSafe<Integer>(1, -1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDimensionIsZero() {
		new MatrixNotThreadSafe<Integer>(0, 1);
	}

	@Test
	public void testContains() {
		assertFalse(matrix.contains(2));
		matrix.set(new Position(0, 0), 2);
		assertTrue(matrix.contains(2));
		matrix.set(new Position(0, 0), 1);
		assertFalse(matrix.contains(2));
	}

	@Test
	public void testJustCreatedMatrixContainsNull() {
		assertTrue(matrix.contains(null));
	}

	@Test
	public void testGetDimension() {
		assertEquals(new Dimension(ROWS, COLUMNS), matrix.getDimension());
	}

	@Test
	public void testDimensionOfMatrix() {
		assertEquals(ROWS, matrix.rows());
		assertEquals(COLUMNS, matrix.columns());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetByNegativePosition() {
		matrix.get(1, -1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetByOutOfBoundsPosition() {
		matrix.get(ROWS + 1, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetByNegativePosition() {
		matrix.set(-1, 0, VALUE);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSetByOutOfBoundsPosition() {
		matrix.set(0, COLUMNS + 1, VALUE);
	}

	@Test
	public void testGetSet() {
		fillMatrix(matrix);
		for (int row = 0; row < ROWS; ++row) {
			for (int column = 0; column < COLUMNS; ++column) {
				assertEquals(elementForPosition(row, column), matrix.get(row, column));
			}
		}
	}

	@Test
	public void testFill() {
		final Integer one = 1;
		matrix.fill(one);
		for (int row = 0; row < ROWS; ++row) {
			for (int column = 0; column < COLUMNS; ++column) {
				assertTrue(one.equals(matrix.get(row, column)));
			}
		}
	}

	@Test
	public void testFillByNullValues() {
		matrix.fill(null);
		for (int row = 0; row < ROWS; ++row) {
			for (int column = 0; column < COLUMNS; ++column) {
				assertNull(matrix.get(row, column));
			}
		}
	}
	
	@Test
	public void testSwap() {
		Matrix<Integer> matrix = new MatrixNotThreadSafe<Integer>(2, 2);
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
	public void testForEach_AllElementsAreProcessedExactlyOnce() {
		final Matrix<Boolean> flags = getMatrixInitializedByFalse(ROWS, COLUMNS);
		flags.forEach(new OnEachHandler<Boolean>() {
			@Override
			public void handle(Position pos, Boolean elem) {
				assertFalse(flags.get(pos));
				assertFalse(elem);
				flags.set(pos, true);
			}
		});
		for (int row = 0; row < flags.rows(); ++row) {
			for (int column = 0; column < flags.columns(); ++column) {
				assertTrue(flags.get(row, column));
			}
		}
	}
	
	private Matrix<Boolean> getMatrixInitializedByFalse(int rows, int columns) {
		final Matrix<Boolean> flags = new MatrixNotThreadSafe<Boolean>(rows, columns);
		for (int row = 0; row < flags.rows(); ++row) {
			for (int column = 0; column < flags.columns(); ++column) {
				flags.set(row, column, false);
			}
		}
		return flags;
	}

	@Test
	public void testForEach_Order_LeftToRight_UpToDown() {
		final Matrix<Boolean> flags = getMatrixInitializedByFalse(ROWS, COLUMNS);
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
		Position positionBefore = positionBefore(matrix.rows(), matrix.columns(), pos);
		assertTrue(matrix.get(positionBefore));
	}

	private Position positionBefore(int rows, int columns, Position pos) {
		if (pos.column - 1 < 0) {
			return new Position(pos.row - 1, columns - 1);
		} else {
			return new Position(pos.row, pos.column - 1);
		}
	}

	@Test
	public void testEquals() {
		Matrix<Integer> matrix1 = new MatrixNotThreadSafe<Integer>(1, 2);
		matrix1.set(0, 0, VALUE);
		matrix1.set(0, 1, VALUE);
		Matrix<Integer> matrix2 = new MatrixNotThreadSafe<Integer>(1, 2);
		matrix2.set(0, 0, VALUE);
		matrix2.set(0, 1, VALUE);
		assertTrue(matrix1.equals(matrix2));
		matrix2.set(0, 0, VALUE + 1);
		assertFalse(matrix1.equals(matrix2));
	}

	@Test
	public void testEquals_WithNull() {
		assertFalse(matrix.equals(null));
	}

	@Test
	public void testEquals_DifferentClass() {
		assertFalse(matrix.equals(Integer.valueOf(1)));
	}

	@Test
	public void testEquals_NullElements_DifferentTemplateParameters() {
		Matrix<Integer> integerMatrix = new MatrixNotThreadSafe<Integer>(2, 2);
		Matrix<Boolean> booleanMatrix = new MatrixNotThreadSafe<Boolean>(2, 2);
		assertTrue(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEquals_NotNullElements_DifferentTemplateParameters() {
		Matrix<Integer> integerMatrix = new MatrixNotThreadSafe<Integer>(1, 1);
		Matrix<Boolean> booleanMatrix = new MatrixNotThreadSafe<Boolean>(1, 1);
		integerMatrix.set(0, 0, Integer.valueOf(0));
		booleanMatrix.set(0, 0, Boolean.valueOf(false));
		assertFalse(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEquals_NullElements_SameTemplateParameters() {
		assertEquals(matrix, new MatrixNotThreadSafe<Integer>(ROWS, COLUMNS));
	}

	@Test
	public void testEquals_DifferentDimensions_NullElements() {
		Matrix<Integer> matrix1 = new MatrixNotThreadSafe<Integer>(2, 2);
		Matrix<Integer> matrix2 = new MatrixNotThreadSafe<Integer>(2, 1);
		assertFalse(matrix1.equals(matrix2));
	}

	@Test
	public void testEquals_NotNullElements_With_NullElements() {		
		Matrix<Boolean> falseElements = getMatrixInitializedByFalse(ROWS, COLUMNS);
		Matrix<Boolean> nullElements = new MatrixNotThreadSafe<Boolean>(ROWS, COLUMNS);
		assertFalse(falseElements.equals(nullElements));
	}

	@Test
	public void testHashCode_NotThrowsExceptions() {
		matrix.hashCode();
	}
}
