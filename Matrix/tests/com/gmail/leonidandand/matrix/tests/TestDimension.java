package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmail.leonidandand.matrix.Dimension;

public class TestDimension {
	
	@Test
	public void testCreation() {
		Dimension dim = Dimension.withRowsColumns(1, 2);
		assertEquals(1, dim.rows);
		assertEquals(2, dim.columns);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRowsIsZero() {
		Dimension.withRowsColumns(0, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRowsIsNegative() {
		Dimension.withRowsColumns(-1, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testColumnsIsZero() {
		Dimension.withRowsColumns(1, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testColumnsIsNegative() {
		Dimension.withRowsColumns(1, -2);
	}

	public void testEquals() {
		assertTrue(Dimension.withRowsColumns(33, 22).equals(Dimension.withRowsColumns(33, 22)));
		assertFalse(Dimension.withRowsColumns(1, 2).equals(Dimension.withRowsColumns(3, 1)));
		assertFalse(Dimension.withRowsColumns(2, 4).equals(null));
	}

	public void testHashCode_NotThrowsExceptions() {
		Dimension.withRowsColumns(2, 3).hashCode();
	}
}
