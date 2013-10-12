package mobiustransformations.toolkit;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestComplex {

	private static final double ERROR_MARGIN = 0.000001;

	@Test
	public void testSquareRootOfOne() {
		Complex z = new Complex(1.0, 0.0);
		
		Complex squareRootPlus = z.squareRootPlus();
		assertEquals(1.0, squareRootPlus.getX(), ERROR_MARGIN);
		assertEquals(0.0, squareRootPlus.getY(), ERROR_MARGIN);

		Complex squareRootMinus = z.squareRootMinus();
		assertEquals(-1.0, squareRootMinus.getX(), ERROR_MARGIN);
		assertEquals(0.0, squareRootMinus.getY(), ERROR_MARGIN);
	}

	@Test
	public void testSquareRootOfI() {
		Complex z = new Complex(0.0, 1.0);
		
		Complex squareRootPlus = z.squareRootPlus();
		assertEquals(Math.sqrt(1.0 / 2.0), squareRootPlus.getX(), ERROR_MARGIN);
		assertEquals(Math.sqrt(1.0 / 2.0), squareRootPlus.getY(), ERROR_MARGIN);

		Complex squareRootMinus = z.squareRootMinus();
		assertEquals(-Math.sqrt(1.0 / 2.0), squareRootMinus.getX(), ERROR_MARGIN);
		assertEquals(-Math.sqrt(1.0 / 2.0), squareRootMinus.getY(), ERROR_MARGIN);
	}

	@Test
	public void testSquareRootOf2PiOver3() {
		Complex z = new Complex(-0.5, Math.sqrt(3.0) / 2.0);
		
		Complex squareRootPlus = z.squareRootPlus();
		assertEquals(0.5, squareRootPlus.getX(), ERROR_MARGIN);
		assertEquals(Math.sqrt(3.0) / 2.0, squareRootPlus.getY(), ERROR_MARGIN);

		Complex squareRootMinus = z.squareRootMinus();
		assertEquals(-0.5, squareRootMinus.getX(), ERROR_MARGIN);
		assertEquals(-Math.sqrt(3.0) / 2.0, squareRootMinus.getY(), ERROR_MARGIN);
	}

	@Test
	public void testSquareRootOf2PiOver3WithRadius2() {
		Complex z = new Complex(-1.0, Math.sqrt(3.0));
		
		Complex squareRootPlus = z.squareRootPlus();
		assertEquals(0.5 * Math.sqrt(2.0), squareRootPlus.getX(), ERROR_MARGIN);
		assertEquals(Math.sqrt(3.0) / 2.0 * Math.sqrt(2.0), squareRootPlus.getY(), ERROR_MARGIN);

		Complex squareRootMinus = z.squareRootMinus();
		assertEquals(-0.5 * Math.sqrt(2.0), squareRootMinus.getX(), ERROR_MARGIN);
		assertEquals(-Math.sqrt(3.0) / 2.0 * Math.sqrt(2.0), squareRootMinus.getY(), ERROR_MARGIN);
	}
	
	@Test
	public void testSquareRootOfNegativeRealNumbers() {
		Complex z = new Complex(-1.0, 0.0);

		Complex squareRootPlus = z.squareRootPlus();
		assertEquals(0.0, squareRootPlus.getX(), ERROR_MARGIN);
		assertEquals(1.0, squareRootPlus.getY(), ERROR_MARGIN);

		Complex squareRootMinus = z.squareRootMinus();
		assertEquals(0.0, squareRootMinus.getX(), ERROR_MARGIN);
		assertEquals(-1.0, squareRootMinus.getY(), ERROR_MARGIN);
	}
	
	@Test
	public void testSquareRootOfJustBelowNegativeRealAxis() {
		Complex z = new Complex(-1.0, -0.0001);

		Complex squareRootPlus = z.squareRootPlus();
		assertEquals(-0.00004999999994, squareRootPlus.getX(), ERROR_MARGIN);
		assertEquals(1.000000001, squareRootPlus.getY(), ERROR_MARGIN);
		
		Complex squareRootMinus = z.squareRootMinus();
		assertEquals(0.00004999999994, squareRootMinus.getX(), ERROR_MARGIN);
		assertEquals(-1.000000001, squareRootMinus.getY(), ERROR_MARGIN);
	}
	
	@Test
	public void testPolarAngle() {
		Complex z = new Complex(0.0, 1.0);
		double polarAngle = z.polarAngle();
		assertEquals(Math.PI / 2.0, polarAngle, ERROR_MARGIN);
		
		z = new Complex(0.0, -1.0);
		polarAngle = z.polarAngle();
		assertEquals(Math.PI * 3.0 / 2.0, polarAngle, ERROR_MARGIN);
	}
}
