package mobiustransformations.toolkit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestQuaternion {

	private static final double ERROR_MARGIN = 0.0000000001;

	@Test
	public void testRotationAroundZAxis() {
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 axis = new Vector3(0.0, 0.0, 1.0);
		double angle = Math.PI / 2.0;
		Vector3 rotatedV = Quaternion.rotateVectorAroundAxisByAngle(v, axis, angle);
		
		Vector3 expectedV = new Vector3(0.0, 1.0, 0.0);
		assertTrue(vectorEquals(expectedV , rotatedV, ERROR_MARGIN));
		
		v = new Vector3(1.0, 0.0, 0.0);
		axis = new Vector3(0.0, 0.0, 1.0);
		angle = Math.PI / 4.0;
		rotatedV = Quaternion.rotateVectorAroundAxisByAngle(v, axis, angle);
		
		expectedV = new Vector3(Math.cos(angle), Math.sin(angle), 0.0);
		assertTrue(vectorEquals(expectedV , rotatedV, ERROR_MARGIN));
	}
	
	@Test
	public void testRotateAroundArbitraryAxis() {
		Vector3 v = new Vector3(3.0, 2.0, 5.0);
		Vector3 axis = new Vector3(1.0, 2.0, 3.0).normalise();
		double angle = Math.PI / 5.0;
		Vector3 rotatedV = Quaternion.rotateVectorAroundAxisByAngle(v, axis, angle);
		
		Vector3 expectedV = new Vector3(3.3555360008433297, 2.8466351581648937, 4.3170645609422955);
		assertTrue(vectorEquals(expectedV , rotatedV, ERROR_MARGIN));
	}
	
	@Test
	public void testRotateVectorNotUnitLength() {
		Vector3 v = new Vector3(2.0, 0.0, 0.0);
		Vector3 axis = new Vector3(0.0, 0.0, 1.0);
		double angle = Math.PI / 2.0;
		Vector3 rotatedV = Quaternion.rotateVectorAroundAxisByAngle(v, axis, angle);
		
		Vector3 expectedV = new Vector3(0.0, 2.0, 0.0);
		assertTrue(vectorEquals(expectedV , rotatedV, ERROR_MARGIN));
	}
	
	@Test
	public void testBasisMultiplication() {
		Quaternion one = new Quaternion(1.0, 0.0, 0.0, 0.0);
		Quaternion i = new Quaternion(0.0, 1.0, 0.0, 0.0);
		Quaternion j = new Quaternion(0.0, 0.0, 1.0, 0.0);
		Quaternion k = new Quaternion(0.0, 0.0, 0.0, 1.0);
		
		assertTrue(quaternionEquals(i.multiply(j), k));
		assertTrue(quaternionEquals(j.multiply(k), i));
		assertTrue(quaternionEquals(k.multiply(i), j));

		assertTrue(quaternionEquals(i.multiply(k), j.negate()));
		assertTrue(quaternionEquals(j.multiply(i), k.negate()));
		assertTrue(quaternionEquals(k.multiply(j), i.negate()));

		assertTrue(quaternionEquals(one.multiply(i), i));
		assertTrue(quaternionEquals(one.multiply(j), j));
		assertTrue(quaternionEquals(one.multiply(k), k));

		assertTrue(quaternionEquals(i.multiply(i), one.negate()));
		assertTrue(quaternionEquals(j.multiply(j), one.negate()));
		assertTrue(quaternionEquals(k.multiply(k), one.negate()));
	}
	
	@Test
	public void testParallelToAxis() {
		Vector3 v = new Vector3(1.0, 2.0, 3.0);
		Vector3 axis = new Vector3(1.0, 2.0, 3.0).normalise();
		double angle = Math.PI / 2.0;
		Vector3 rotatedV = Quaternion.rotateVectorAroundAxisByAngle(v, axis, angle);
		
		Vector3 expectedV = new Vector3(1.0, 2.0, 3.0);
		assertTrue(vectorEquals(expectedV , rotatedV, ERROR_MARGIN));
	}

	protected boolean quaternionEquals(final Quaternion q1, final Quaternion q2) {
		if ((q1.getA() - q2.getA()) * (q1.getA() - q2.getA()) > ERROR_MARGIN)
			return false;
		if ((q1.getB() - q2.getB()) * (q1.getB() - q2.getB()) > ERROR_MARGIN)
			return false;
		if ((q1.getC() - q2.getC()) * (q1.getC() - q2.getC()) > ERROR_MARGIN)
			return false;
		if ((q1.getD() - q2.getD()) * (q1.getD() - q2.getD()) > ERROR_MARGIN)
			return false;
		return true;
	}
	
	public static boolean vectorEquals(final Vector3 u, final Vector3 v, double errorMargin) {
		return ((u.subtract(v)).lengthSquared() < errorMargin);
	}
}
