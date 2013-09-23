package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.gmail.leonidandand.matrix.ArrayMatrix;
import com.gmail.leonidandand.matrix.Counter;
import com.gmail.leonidandand.matrix.Dimension;
import com.gmail.leonidandand.matrix.Matrix;
import com.gmail.leonidandand.matrix.OnEachHandler;
import com.gmail.leonidandand.matrix.Position;

public class TestIterableArrayMatrix {
	
	private static final Dimension DIM = new Dimension(4, 5);
	private static final int NUMBER_OF_ELEMENTS = DIM.rows * DIM.columns;
	private Matrix<Integer> matrix;
	
	@Before
	public void setUp() {
		matrix = new ArrayMatrix<Integer>(DIM);
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
		final int INIT = 0;
		final Counter counter = new Counter(INIT);
		matrix.forEach(new OnEachHandler<Integer>() {
			@Override
			public void handle(Position pos, Integer elem) {
				matrix.set(pos, counter.getCount());
				counter.increaseByOne();
			}
		});
		counter.reset(INIT);
		for (Integer elem : matrix) {
			Integer expected = counter.getCount();
			assertEquals(expected, elem);
			counter.increaseByOne();
		}
	}

}
