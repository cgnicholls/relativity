package mobiustransformations.toolkit;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOneParameterSubgroup {

	private static final double ERROR_MARGIN = 0.00000001;

	@Test
	public void testElliptic() {
		// The following is an elliptic transformation:
		// [i  0]
		// [0 -i]
		// with eigenvalues: lambda = i, lambda' = 1/i = -i
		// and eigenvectors (1, 0), (0, 1).
		
		GL2C matrixOfEVectors = GL2C.diagonal(new Complex(1.0), new Complex(1.0));
		GL2C matrixOfEVectorsInverse = GL2C.diagonal(new Complex(1.0), new Complex(1.0));
		
		OneParameterSubgroup oneParameterSubgroup = new OneParameterSubgroup(0.0, Math.PI / 2.0, matrixOfEVectors, matrixOfEVectorsInverse);
		
		GL2C parameter1 = oneParameterSubgroup.mobiusAtParameter(1.0);
		
		assertEquals(0.0, parameter1.getA().getX(), ERROR_MARGIN);
		assertEquals(1.0, parameter1.getA().getY(), ERROR_MARGIN);
		assertEquals(0.0, parameter1.getB().getX(), ERROR_MARGIN);
		assertEquals(0.0, parameter1.getB().getY(), ERROR_MARGIN);
		assertEquals(0.0, parameter1.getC().getX(), ERROR_MARGIN);
		assertEquals(0.0, parameter1.getC().getY(), ERROR_MARGIN);
		assertEquals(0.0, parameter1.getD().getX(), ERROR_MARGIN);
		assertEquals(-1.0, parameter1.getD().getY(), ERROR_MARGIN);
	}

}
