package com.gmail.landanurm.matrix.tests;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import org.junit.Test;

import com.gmail.landanurm.matrix.ArrayMatrix;
import com.gmail.landanurm.matrix.Dimension;
import com.gmail.landanurm.matrix.Matrix;
import com.gmail.landanurm.matrix.OnEachHandler;
import com.gmail.landanurm.matrix.Position;

public class TestArrayMatrixSerialization {

	@Test
	public void test() throws Exception {
		Matrix<Integer> matrix = new ArrayMatrix<Integer>(new Dimension(11, 7));
		fillByRandomNumbers(matrix);
		final String filename = "TestArrayMatrixSerializable";
		serialize(matrix, filename);
		assertEquals(matrix, deserialize(filename));
	}

	private void fillByRandomNumbers(final Matrix<Integer> matrix) {
		final Random random = new Random();
		matrix.forEach(new OnEachHandler<Integer>() {
			@Override
			public void handle(Position pos, Integer elem) {
				random.setSeed(System.nanoTime());
				Integer randomValue = random.nextInt() * random.nextInt();
				matrix.set(pos, randomValue);
			}
		});
	}

	private <T> void serialize(Matrix<T> matrix, String filename) throws Exception {
    	FileOutputStream fos = new FileOutputStream(filename);
    	ObjectOutputStream out = new ObjectOutputStream(fos);
    	out.writeObject(matrix);
	    out.close();
	}

	private <T> Matrix<T> deserialize(String filename) throws Exception {
		FileInputStream fis = new FileInputStream(filename);
	    ObjectInputStream in = new ObjectInputStream(fis);
    	@SuppressWarnings("unchecked")
		Matrix<T> readedMatrix = (Matrix<T>) in.readObject();
    	in.close();
	    return readedMatrix;
	}

}
