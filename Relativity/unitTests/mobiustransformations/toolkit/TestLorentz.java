package mobiustransformations.toolkit;

import static org.junit.Assert.assertEquals;

import mobiustransformations.toolkit.FourVector;
import mobiustransformations.toolkit.Lorentz;
import mobiustransformations.toolkit.Vector3;

import org.junit.Test;

public class TestLorentz {

	private static final double ERROR_MARGIN = 0.000001;

	@Test
	public void testBoostZ() {
		double c = Lorentz.SPEED_OF_LIGHT;
		Vector3 spatial = new Vector3(1.0, 2.0, 3.0);
		FourVector fourVector = new FourVector(spatial.length() / c, spatial);
		assertEquals(0.0, fourVector.innerProduct(), ERROR_MARGIN);
		
		FourVector fourVectorBoosted = Lorentz.boostZ(0.9 * c, fourVector);
		assertEquals(0.0, fourVectorBoosted.innerProduct(), ERROR_MARGIN);
	}
	
	@Test
	public void testBoostPreservesInnerProduct() {
		double c = Lorentz.SPEED_OF_LIGHT;
		Vector3 spatial = new Vector3(1.0, 2.0, 3.0);
		FourVector fourVector = new FourVector(spatial.length() / c, spatial);
		assertEquals(0.0, fourVector.innerProduct(), ERROR_MARGIN);
		
		Vector3 velocity = new Vector3(0.0, 0.0, 50.0);
		FourVector fourVectorBoosted = Lorentz.boost(velocity, fourVector);
		assertEquals(0.0, fourVectorBoosted.innerProduct(), ERROR_MARGIN);
	}
	
	@Test
	public void testBoostZeroVelocity() {
		Vector3 spatial = new Vector3(1.0, 2.0, 3.0);
		FourVector fourVector = FourVector.lightRay(spatial);
		assertEquals(0.0, fourVector.innerProduct(), ERROR_MARGIN);
		
		FourVector fourVectorBoosted = Lorentz.boost(new Vector3(0.0, 0.0, 0.0), fourVector);
		assertEquals(fourVector.getT(), fourVectorBoosted.getT(), ERROR_MARGIN);
		assertEquals(fourVector.getSpatial().getX(), fourVectorBoosted.getSpatial().getX(), ERROR_MARGIN);
		assertEquals(fourVector.getSpatial().getY(), fourVectorBoosted.getSpatial().getY(), ERROR_MARGIN);
		assertEquals(fourVector.getSpatial().getZ(), fourVectorBoosted.getSpatial().getZ(), ERROR_MARGIN);
	}
	
	@Test
	public void testRelativeVelocity() {
		double c = Lorentz.SPEED_OF_LIGHT;
		
		Vector3 velocity = new Vector3(1.0, 2.0, 3.0);
		FourVector fourVelocity = FourVector.fourVelocity(velocity);
		FourVector boostedFourVelocity = Lorentz.boost(velocity, fourVelocity);
		double tPrime = boostedFourVelocity.getT();
		double xPrime = boostedFourVelocity.getSpatial().getX();
		double yPrime = boostedFourVelocity.getSpatial().getY();
		double zPrime = boostedFourVelocity.getSpatial().getZ();
		assertEquals(1.0, tPrime, ERROR_MARGIN);
		assertEquals(0.0, xPrime, ERROR_MARGIN);
		assertEquals(0.0, yPrime, ERROR_MARGIN);
		assertEquals(0.0, zPrime, ERROR_MARGIN);
	}

	@Test
	public void testGamma() {
		double c = Lorentz.SPEED_OF_LIGHT;
		assertEquals(1.0, Lorentz.gamma(0.0), ERROR_MARGIN);
		assertEquals(2.0, Lorentz.gamma(3.0 / 4.0 * (c*c)), ERROR_MARGIN);
	}
}
