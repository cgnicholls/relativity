package mobiustransformations.toolkit;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGL2C {

	private static final double ERROR_MARGIN = 0.000001;

	@Test
	public void testEigenvectorQuarterRotation() {
		GL2C rotation = GL2C.diagonal(new Complex(0.0, 1.0), new Complex(0.0, -1.0));
		
		HomCoords2D eigenvector1 = rotation.eigenvector(new Complex(0.0, 1.0));
		assertEquals(0.0, eigenvector1.getZ1().minus(new Complex(0.0, -1.0)).lengthSquared(), ERROR_MARGIN);
		assertEquals(0.0, eigenvector1.getZ2().minus(new Complex(0.0)).lengthSquared(), ERROR_MARGIN);

		HomCoords2D eigenvector2 = rotation.eigenvector(new Complex(0.0, -1.0));
		assertEquals(0.0, eigenvector2.getZ1().minus(new Complex(0.0)).lengthSquared(), ERROR_MARGIN);
		assertEquals(0.0, eigenvector2.getZ2().minus(new Complex(0.0, 1.0)).lengthSquared(), ERROR_MARGIN);
	}
	
	@Test
	public void testEigenvectorRotation() {
		GL2C rotation = GL2C.diagonal(Complex.polars(1.0, Math.PI * 2.0 / 3.0), Complex.polars(1.0, -Math.PI * 2.0 / 3.0));
		
		HomCoords2D eigenvector1 = rotation.eigenvector(Complex.polars(1.0, Math.PI * 2.0 / 3.0));
		assertEquals(0.0, eigenvector1.getZ1().minus(new Complex(0.0, -1.0)).lengthSquared(), ERROR_MARGIN);
		assertEquals(0.0, eigenvector1.getZ2().minus(new Complex(0.0)).lengthSquared(), ERROR_MARGIN);

		HomCoords2D eigenvector2 = rotation.eigenvector(Complex.polars(1.0, -Math.PI * 2.0 / 3.0));
		assertEquals(0.0, eigenvector2.getZ1().minus(new Complex(0.0)).lengthSquared(), ERROR_MARGIN);
		assertEquals(0.0, eigenvector2.getZ2().minus(new Complex(0.0, 1.0)).lengthSquared(), ERROR_MARGIN);
	}

	@Test
	public void testEigenvalueOne() {
		GL2C rotation = GL2C.diagonal(Complex.polars(1.0, Math.PI * 2.0 / 3.0), Complex.polars(1.0, -Math.PI * 2.0 / 3.0));
		
		Complex eigenvalueOne = rotation.eigenvalueOne();
		
		assertEquals(0.0, eigenvalueOne.minus(new Complex(-0.5, Math.sqrt(3.0) / 2.0)).lengthSquared(), ERROR_MARGIN);
	}
}
