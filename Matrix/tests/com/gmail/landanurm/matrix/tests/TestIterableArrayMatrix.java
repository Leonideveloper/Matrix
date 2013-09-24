package com.gmail.landanurm.matrix.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.gmail.landanurm.matrix.ArrayMatrix;
import com.gmail.landanurm.matrix.Dimension;
import com.gmail.landanurm.matrix.Matrix;
import com.gmail.landanurm.matrix.OnEachHandler;
import com.gmail.landanurm.matrix.Position;

public class TestIterableArrayMatrix {
	private static final Dimension DIM = Dimension.withRowsColumns(4, 5);
	private static final int NUMBER_OF_ELEMENTS = DIM.rows * DIM.columns;
	private Matrix<Integer> matrix;
	
	@Before
	public void setUp() {
		matrix = new ArrayMatrix<Integer>(DIM);
	}

	@Test
	public void testMatrixIsInstanceOfIterable() {
		assertTrue(matrix instanceof Iterable<?>);
	}

	@Test
	public void testNumberOfIteratedElementsMustBeEqualToNumberOfAllElements() {
		int count = 0;
		for (@SuppressWarnings("unused") Integer each : matrix) {
			++count;
		}
		assertEquals(NUMBER_OF_ELEMENTS, count);
	}
	
	@Test
	public void testIterable() {
		final Counter counter = new Counter(0);
		matrix.forEach(new OnEachHandler<Integer>() {
			@Override
			public void handle(Position pos, Integer elem) {
				matrix.set(pos, counter.getCount());
				counter.increaseByOne();
			}
		});
		counter.reset(0);
		for (Integer elem : matrix) {
			Integer expected = counter.getCount();
			assertEquals(expected, elem);
			counter.increaseByOne();
		}
	}

}
