package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmail.leonidandand.matrix.Dimension;

public class TestDimension {
	
	@Test
	public void testCreation() {
		Dimension dim = new Dimension(1, 2);
		assertEquals(1, dim.rows);
		assertEquals(2, dim.columns);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRowsIsZero() {
		new Dimension(0, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRowsIsNegative() {
		new Dimension(-1, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testColumnsIsZero() {
		new Dimension(1, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testColumnsIsNegative() {
		new Dimension(1, -2);
	}

	public void testEquals() {
		assertTrue(new Dimension(33, 22).equals(new Dimension(33, 22)));
		assertFalse(new Dimension(1, 2).equals(new Dimension(3, 1)));
		assertFalse(new Dimension(2, 4).equals(null));
	}
}
