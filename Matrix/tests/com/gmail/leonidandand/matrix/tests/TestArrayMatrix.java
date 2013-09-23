package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.gmail.leonidandand.matrix.ArrayMatrix;
import com.gmail.leonidandand.matrix.Counter;
import com.gmail.leonidandand.matrix.Dimension;
import com.gmail.leonidandand.matrix.Matrix;
import com.gmail.leonidandand.matrix.OnEachHandler;
import com.gmail.leonidandand.matrix.Position;
import com.gmail.leonidandand.matrix.ReadOnlyMatrix;

public class TestArrayMatrix {
	private static final Dimension DIM = new Dimension(10, 18);
	private static final int numberOfElements = DIM.rows * DIM.columns;
	
	private Matrix<Integer> matrix;

	@Before
	public void setUp() {
		matrix = new ArrayMatrix<Integer>(DIM);
	}
	
	@Test
	public void testMatrixIsInstanceOfReadOnlyMatrix() {
		assertTrue(matrix instanceof ReadOnlyMatrix<?>);
	}
	
	@Test
	public void testMatrixCopyConstructor() {
		fillMatrix(matrix);
		Matrix<Integer> copy = new ArrayMatrix<Integer>(matrix);
		assertEquals(matrix.getDimension(), copy.getDimension());
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				Position pos = new Position(row, column);
				assertEquals(matrix.get(pos), copy.get(pos));
			}
		}
	}
	
	private void fillMatrix(final Matrix<Integer> matrixToFill) {
		final Dimension dim = matrixToFill.getDimension();
		for (int row = 0; row < dim.rows; ++row) {
			for (int column = 0; column < dim.columns; ++column) {
				Position pos = new Position(row, column);
				matrixToFill.set(pos, elementForPosition(pos));
			}
		}
	}
	
	private Integer elementForPosition(Position pos) {
		return pos.row * pos.column;
	}

	@Test
	public void testInitValueIsNull() {
		assertNull(matrix.get(new Position(0, 0)));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeDimension() {
		new ArrayMatrix<Integer>(1, -1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDimensionIsZero() {
		new ArrayMatrix<Integer>(0, 1);
	}

	@Test
	public void testEmptyMatrixNotContainsNotNullElement() {
		assertFalse(matrix.contains(2));
	}

	@Test
	public void testEmptyMatrixContainsNull() {
		assertTrue(matrix.contains(null));
	}

	@Test
	public void testContainsSettedElement() {
		matrix.set(new Position(0, 0), 2);
		assertTrue(matrix.contains(2));
	}

	@Test
	public void testNotContainsOldValue() {
		matrix.set(new Position(0, 0), 2);
		matrix.set(new Position(0, 0), 1);
		assertFalse(matrix.contains(2));
	}

	@Test
	public void testJustCreatedMatrixContainsNull() {
		assertTrue(matrix.contains(null));
	}

	@Test
	public void testContainsAll_JustCreatedMatrixContainsNulls() {
		Iterable<Integer> iterable = Arrays.<Integer>asList(null, null);
		assertTrue(matrix.containsAll(iterable));
	}
	
	@Test
	public void testContainsAll_JustCreatedMatrixDoesNotContainValuesThatDifferFromNull() {
		Iterable<Integer> iterable = Arrays.<Integer>asList(1);
		assertFalse(matrix.containsAll(iterable));
	}

	@Test
	public void testContainsAll_AlwaysContainsEmptyIterable() {
		Iterable<Integer> iterable = new ArrayList<Integer>();
		assertTrue(matrix.containsAll(iterable));
	}

	@Test
	public void testContainsAllAddedElements() {
		final Counter counter = new Counter();
		final Collection<Integer> addedElements = new ArrayList<Integer>();
		matrix.forEach(new OnEachHandler<Integer>() {
			@Override
			public void handle(Position pos, Integer elem) {
				matrix.set(pos, counter.getCount());
				addedElements.add(counter.getCount());
				counter.increaseByOne();
			}
		});
		assertTrue(matrix.containsAll(addedElements));
	}
	
	@Test
	public void testContainsAll_MatrixDoesNotContainNotAddedElements() {
		Integer[] elements = {
			Integer.valueOf(0),
			Integer.valueOf(1),
			Integer.valueOf(2)
		};
		matrix.set(new Position(0, 0), elements[0]);
		matrix.set(new Position(0, 1), elements[1]);
		assertFalse(matrix.containsAll(Arrays.asList(elements)));
	}
	
	@Test
	public void testCountAtJustCreatedMatrix() {
		assertEquals(numberOfElements, matrix.count(null));
		assertEquals(0, matrix.count(Integer.valueOf(1)));
	}
	
	@Test
	public void testCountAtMatrixFilledWithSingleValue() {
		Integer two = Integer.valueOf(2);
		matrix.fill(two);
		assertEquals(numberOfElements, matrix.count(two));
		assertEquals(0, matrix.count(Integer.valueOf(two + 1)));
	}
	
	@Test
	public void testCount() {
		Integer one = Integer.valueOf(1);
		matrix.set(new Position(0, 0), one);
		matrix.set(new Position(0, 1), one);
		assertEquals(2, matrix.count(one));
	}

	@Test
	public void testGetDimension() {
		Matrix<Integer> matrix = new ArrayMatrix<Integer>(DIM);
		assertEquals(DIM, matrix.getDimension());
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetByOutOfBoundsPosition() {
		matrix.get(new Position(DIM.rows + 1, 0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSetByOutOfBoundsPosition() {
		matrix.set(new Position(0, DIM.columns + 1), Integer.valueOf(1));
	}

	@Test
	public void testGetSet() {
		fillMatrix(matrix);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				Position pos = new Position(row, column);
				assertEquals(elementForPosition(pos), matrix.get(pos));
			}
		}
	}

	@Test
	public void testFill() {
		final Integer ONE = 1;
		matrix.fill(ONE);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				assertEquals(ONE, matrix.get(new Position(row, column)));
			}
		}
	}

	@Test
	public void testFillByNullValues() {
		matrix.fill(null);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				assertNull(matrix.get(new Position(row, column)));
			}
		}
	}
	
	@Test
	public void testSwap() {
		Matrix<Integer> matrix = new ArrayMatrix<Integer>(2, 2);
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

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSwapPositionOutOfBounds() {
		Position pos1 = new Position(0, DIM.columns + 1);
		Position pos2 = new Position(0, 0);
		matrix.swap(pos1, pos2);
	}

	@Test
	public void testForEach_AllElementsAreProcessedExactlyOnce() {
		final Matrix<Boolean> flags = getMatrixInitializedByFalse(DIM);
		flags.forEach(new OnEachHandler<Boolean>() {
			@Override
			public void handle(Position pos, Boolean elem) {
				assertFalse(flags.get(pos));
				assertFalse(elem);
				flags.set(pos, true);
			}
		});
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				assertTrue(flags.get(new Position(row, column)));
			}
		}
	}
	
	private Matrix<Boolean> getMatrixInitializedByFalse(Dimension dim) {
		final Matrix<Boolean> flags = new ArrayMatrix<Boolean>(dim);
		for (int row = 0; row < dim.rows; ++row) {
			for (int column = 0; column < dim.columns; ++column) {
				flags.set(new Position(row, column), false);
			}
		}
		return flags;
	}

	@Test
	public void testForEach_Order_LeftToRight_UpToDown() {
		final Matrix<Boolean> flags = getMatrixInitializedByFalse(DIM);
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
		Position previousPosition = previousPosition(pos, matrix.getDimension());
		assertTrue(matrix.get(previousPosition));
	}
	
	private Position previousPosition(Position pos, Dimension dim) {
		if (pos.column - 1 < 0) {
			return new Position(pos.row - 1, dim.columns - 1);
		} else {
			return new Position(pos.row, pos.column - 1);
		}
	}

	@Test
	public void testEquals() {
		final Integer VALUE = Integer.valueOf(3);
		Matrix<Integer> matrix1 = new ArrayMatrix<Integer>(1, 2);
		matrix1.set(new Position(0, 0), VALUE);
		matrix1.set(new Position(0, 1), VALUE);
		Matrix<Integer> matrix2 = new ArrayMatrix<Integer>(1, 2);
		matrix2.set(new Position(0, 0), VALUE);
		matrix2.set(new Position(0, 1), VALUE);
		assertTrue(matrix1.equals(matrix2));
		matrix2.set(new Position(0, 0), VALUE + 1);
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
		Matrix<Integer> integerMatrix = new ArrayMatrix<Integer>(2, 2);
		Matrix<Boolean> booleanMatrix = new ArrayMatrix<Boolean>(2, 2);
		assertTrue(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEquals_NotNullElements_DifferentTemplateParameters() {
		Matrix<Integer> integerMatrix = new ArrayMatrix<Integer>(1, 1);
		Matrix<Boolean> booleanMatrix = new ArrayMatrix<Boolean>(1, 1);
		integerMatrix.set(new Position(0, 0), Integer.valueOf(0));
		booleanMatrix.set(new Position(0, 0), Boolean.valueOf(false));
		assertFalse(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEquals_NullElements_SameTemplateParameters() {
		assertEquals(matrix, new ArrayMatrix<Integer>(DIM));
	}

	@Test
	public void testEquals_DifferentDimensions_NullElements() {
		Matrix<Integer> matrix1 = new ArrayMatrix<Integer>(2, 2);
		Matrix<Integer> matrix2 = new ArrayMatrix<Integer>(2, 1);
		assertFalse(matrix1.equals(matrix2));
	}

	@Test
	public void testEquals_NotNullElements_With_NullElements() {		
		Matrix<Boolean> falseElements = getMatrixInitializedByFalse(DIM);
		Matrix<Boolean> nullElements = new ArrayMatrix<Boolean>(DIM);
		assertFalse(falseElements.equals(nullElements));
	}

	@Test
	public void testHashCode_NotThrowsExceptions() {
		matrix.hashCode();
	}
}
