package com.gmail.leonidandand.matrix;

public class Position {
	public final int row;
	public final int column;

	public Position(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException();
		}
		this.row = row;
		this.column = column;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		return (this.row == other.row) && (this.column == other.column);
	}
}