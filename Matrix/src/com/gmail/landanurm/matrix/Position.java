package com.gmail.landanurm.matrix;

import java.io.Serializable;

public final class Position implements Serializable {
	private static final long serialVersionUID = 3286630006743379302L;
	
	public final int row;
	public final int column;

	public static Position withRowColumn(int row, int column) {
		return new Position(row, column);
	}
	
	public Position(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException();
		}
		this.row = row;
		this.column = column;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		return (this.row == other.row) && (this.column == other.column);
	}

	@Override
	public int hashCode() {
		return row * column + row - column;
	}

	@Override
	public String toString() {
		return "[" + row + ", " + column + "]";  
	}
	
}