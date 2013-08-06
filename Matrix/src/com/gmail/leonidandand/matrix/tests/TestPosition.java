package com.gmail.leonidandand.matrix.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gmail.leonidandand.matrix.Position;

public class TestPosition {

	@Test
	public void testCreation() {
		Position pos = new Position(1, 2);
		assertEquals(1, pos.row);
		assertEquals(2, pos.column);
	}

	@Test
	public void testRowsIsZero() {
		new Position(0, 2);
	}

	@Test
	public void testColumnsZero() {
		new Position(2, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalRows() {
		new Position(-1, 2);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalColumns() {
		new Position(1, -2);
	}

	@Test
	public void testEquals() {
		assertTrue(new Position(1, 2).equals(new Position(1, 2)));
		assertFalse(new Position(1, 2).equals(new Position(0, 0)));
		assertFalse(new Position(1, 2).equals(null));
	}
}
