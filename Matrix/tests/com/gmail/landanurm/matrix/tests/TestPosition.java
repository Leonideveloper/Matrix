package com.gmail.landanurm.matrix.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gmail.landanurm.matrix.Position;

public class TestPosition {

	@Test
	public void testCreation() {
		Position pos = Position.withRowColumn(1, 2);
		assertEquals(1, pos.row);
		assertEquals(2, pos.column);
	}

	@Test
	public void testRowsIsZero() {
		Position.withRowColumn(0, 2);
	}

	@Test
	public void testColumnsZero() {
		Position.withRowColumn(2, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNegativeRows() {
		Position.withRowColumn(-1, 2);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNegativeColumns() {
		Position.withRowColumn(1, -2);
	}
	
	@Test
	public void testEqualsIdentityTrue() {
	    Position position = Position.withRowColumn(1, 2);
	    assertTrue(position.equals(position));
	}

	@Test
	public void testValueEqualsTrue() {
	    assertTrue(Position.withRowColumn(1, 2).equals(
	    		   Position.withRowColumn(1, 2))
	    );
	}

	@Test
	public void testValueEqualsFalseIfColumnDiffers() {
	    assertFalse(Position.withRowColumn(1, 2).equals(Position.withRowColumn(1, 3)));
	}

	@Test
	public void testValueEqualsFalseIfRowDiffers() {
	    assertFalse(Position.withRowColumn(1, 2).equals(
	    			Position.withRowColumn(3, 2))
	    );
	}
	
	@Test
	public void testEqualsFalseForNull() {
	    assertFalse(Position.withRowColumn(1, 2).equals(null));
	}

	@Test
	public void testHashCode_NotThrowsExceptions() {
		Position.withRowColumn(1, 2).hashCode();
	}
}
