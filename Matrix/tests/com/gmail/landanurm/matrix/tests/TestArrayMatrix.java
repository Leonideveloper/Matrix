package com.gmail.landanurm.matrix.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.gmail.landanurm.matrix.ArrayMatrix;
import com.gmail.landanurm.matrix.Dimension;
import com.gmail.landanurm.matrix.Matrix;
import com.gmail.landanurm.matrix.OnEachHandler;
import com.gmail.landanurm.matrix.Position;
import com.gmail.landanurm.matrix.ReadOnlyMatrix;

public class TestArrayMatrix {
	private static final Dimension DIM = Dimension.withRowsColumns(10, 18);
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
		Matrix<Integer> copy = ArrayMatrix.copyOf(matrix);
		assertEquals(matrix.getDimension(), copy.getDimension());
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				Position pos = Position.withRowColumn(row, column);
				assertEquals(matrix.get(pos), copy.get(pos));
			}
		}
	}
	
	private void fillMatrix(final Matrix<Integer> matrixToFill) {
		Counter counter = new Counter();
		final Dimension dim = matrixToFill.getDimension();
		for (int row = 0; row < dim.rows; ++row) {
			for (int column = 0; column < dim.columns; ++column) {
				matrixToFill.set(Position.withRowColumn(row, column), counter.getCount());
				counter.increaseByOne();
			}
		}
	}

	@Test
	public void testInitValueIsNull() {
		assertNull(matrix.get(Position.withRowColumn(0, 0)));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeDimension() {
		new ArrayMatrix<Integer>(Dimension.withRowsColumns(1, -1));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDimensionIsZero() {
		new ArrayMatrix<Integer>(Dimension.withRowsColumns(0, 1));
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
		matrix.set(Position.withRowColumn(0, 0), 2);
		assertTrue(matrix.contains(2));
	}

	@Test
	public void testNotContainsOldValue() {
		matrix.set(Position.withRowColumn(0, 0), 2);
		matrix.set(Position.withRowColumn(0, 0), 1);
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
		final Collection<Integer> addedElements = new ArrayList<Integer>();
		matrix.forEach(new OnEachHandler<Integer>() {
			@Override
			public void handle(Position pos, Integer elem) {
				int random = getRandom();
				matrix.set(pos, random);
				addedElements.add(random);
			}
		});
		assertTrue(matrix.containsAll(addedElements));
	}
	
	private int getRandom() {
		Random random = new Random(System.nanoTime());
		return random.nextInt();
	}
	
	@Test
	public void testContainsAll_MatrixDoesNotContainNotAddedElements() {
		Integer[] elements = {
			Integer.valueOf(0),
			Integer.valueOf(1),
			Integer.valueOf(2)
		};
		matrix.set(Position.withRowColumn(0, 0), elements[0]);
		matrix.set(Position.withRowColumn(0, 1), elements[1]);
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
		matrix.set(Position.withRowColumn(0, 0), one);
		matrix.set(Position.withRowColumn(0, 1), one);
		assertEquals(2, matrix.count(one));
	}

	@Test
	public void testGetDimension() {
		Matrix<Integer> matrix = new ArrayMatrix<Integer>(DIM);
		assertEquals(DIM, matrix.getDimension());
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetByOutOfBoundsPosition() {
		matrix.get(Position.withRowColumn(DIM.rows + 1, 0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSetByOutOfBoundsPosition() {
		matrix.set(Position.withRowColumn(0, DIM.columns + 1), Integer.valueOf(1));
	}

	@Test
	public void testGetSet() {
		Counter counter = new Counter(0);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				matrix.set(new Position(row, column), counter.getCount());
				counter.increaseByOne();
			}
		}
		counter.reset(0);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				Integer expected = counter.getCount();
				counter.increaseByOne();
				assertEquals(expected, matrix.get(new Position(row, column)));
			}
		}
	}

	@Test
	public void testFill() {
		final Integer ONE = 1;
		matrix.fill(ONE);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				assertEquals(ONE, matrix.get(Position.withRowColumn(row, column)));
			}
		}
	}

	@Test
	public void testFillByNullValues() {
		matrix.fill(null);
		for (int row = 0; row < DIM.rows; ++row) {
			for (int column = 0; column < DIM.columns; ++column) {
				assertNull(matrix.get(Position.withRowColumn(row, column)));
			}
		}
	}
	
	@Test
	public void testSwap() {
		Matrix<Integer> matrix = new ArrayMatrix<Integer>(Dimension.withRowsColumns(2, 2));
		Position pos1 = Position.withRowColumn(0, 0);
		Position pos2 = Position.withRowColumn(1, 1);
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
		Position pos1 = Position.withRowColumn(0, DIM.columns + 1);
		Position pos2 = Position.withRowColumn(0, 0);
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
				assertTrue(flags.get(Position.withRowColumn(row, column)));
			}
		}
	}
	
	private Matrix<Boolean> getMatrixInitializedByFalse(Dimension dim) {
		final Matrix<Boolean> flags = new ArrayMatrix<Boolean>(dim);
		for (int row = 0; row < dim.rows; ++row) {
			for (int column = 0; column < dim.columns; ++column) {
				flags.set(Position.withRowColumn(row, column), false);
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
				if (!pos.equals(Position.withRowColumn(0, 0))) {
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
			return Position.withRowColumn(pos.row - 1, dim.columns - 1);
		} else {
			return Position.withRowColumn(pos.row, pos.column - 1);
		}
	}

	@Test
	public void testEquals() {
		final Integer VALUE = Integer.valueOf(3);
		Matrix<Integer> matrix1 = new ArrayMatrix<Integer>(Dimension.withRowsColumns(1, 2));
		matrix1.set(Position.withRowColumn(0, 0), VALUE);
		matrix1.set(Position.withRowColumn(0, 1), VALUE);
		Matrix<Integer> matrix2 = new ArrayMatrix<Integer>(matrix1.getDimension());
		matrix2.set(Position.withRowColumn(0, 0), VALUE);
		matrix2.set(Position.withRowColumn(0, 1), VALUE);
		assertTrue(matrix1.equals(matrix2));
		matrix2.set(Position.withRowColumn(0, 0), VALUE + 1);
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
		Dimension dim = Dimension.withRowsColumns(2, 2);
		Matrix<Integer> integerMatrix = new ArrayMatrix<Integer>(dim);
		Matrix<Boolean> booleanMatrix = new ArrayMatrix<Boolean>(dim);
		assertTrue(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEquals_NotNullElements_DifferentTemplateParameters() {
		Dimension dim = Dimension.withRowsColumns(1, 1);
		Matrix<Integer> integerMatrix = new ArrayMatrix<Integer>(dim);
		Matrix<Boolean> booleanMatrix = new ArrayMatrix<Boolean>(dim);
		integerMatrix.set(Position.withRowColumn(0, 0), Integer.valueOf(0));
		booleanMatrix.set(Position.withRowColumn(0, 0), Boolean.valueOf(false));
		assertFalse(integerMatrix.equals(booleanMatrix));
	}

	@Test
	public void testEquals_NullElements_SameTemplateParameters() {
		assertEquals(matrix, new ArrayMatrix<Integer>(DIM));
	}

	@Test
	public void testEquals_DifferentDimensions_NullElements() {
		Matrix<Integer> matrix1 = new ArrayMatrix<Integer>(Dimension.withRowsColumns(2, 2));
		Matrix<Integer> matrix2 = new ArrayMatrix<Integer>(Dimension.withRowsColumns(2, 1));
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
