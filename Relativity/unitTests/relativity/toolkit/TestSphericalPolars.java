package relativity.toolkit;

import static org.junit.Assert.*;


import org.junit.Test;

import relativity.toolkit.SphericalPolars;
import relativity.toolkit.Vector3;

public class TestSphericalPolars {

	private static final double ERROR_MARGIN = 0.00001;

	@Test
	public void testEquators() {
		Vector3 unitV = new Vector3(1.0, 0.0, 0.0);
		SphericalPolars sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 2.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(0.0, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(-1.0, 0.0, 0.0);
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 2.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(Math.PI, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(0.0, 1.0, 0.0);
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 2.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(Math.PI / 2.0, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(0.0, -1.0, 0.0);
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 2.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(3.0 * Math.PI / 2.0, sphericalPolars.getPhi(), ERROR_MARGIN);
	}
	
	@Test
	public void testPoles() {
		Vector3 unitV = new Vector3(0.0, 0.0, 1.0);
		SphericalPolars sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(0.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(0.0, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(0.0, 0.0, -1.0);
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(0.0, sphericalPolars.getPhi(), ERROR_MARGIN);
	}
	
	@Test
	public void testQuarters() {
		Vector3 unitV = new Vector3(1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0), 0.0);
		SphericalPolars sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 2.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(Math.PI / 4.0, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(1.0 / Math.sqrt(2.0), 0.0, 1.0 / Math.sqrt(2.0));
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 4.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(0.0, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(0.0, 1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0));
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 4.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(Math.PI / 2.0, sphericalPolars.getPhi(), ERROR_MARGIN);
		
		unitV = new Vector3(0.0, -1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0));
		sphericalPolars = SphericalPolars.computeSphericalPolars(unitV);
		assertEquals(Math.PI / 4.0, sphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(2.0 * Math.PI - Math.PI / 2.0, sphericalPolars.getPhi(), ERROR_MARGIN);
	}
	
	@Test
	public void testExplicit() {
		double theta = 0.555;
		double phi = 0.234;
		SphericalPolars originalSphericalPolars = new SphericalPolars(theta, phi);
		Vector3 vector = originalSphericalPolars.computeVector();
		SphericalPolars computedSphericalPolars = SphericalPolars.computeSphericalPolars(vector);
		assertEquals(theta, computedSphericalPolars.getTheta(), ERROR_MARGIN);
		assertEquals(phi, computedSphericalPolars.getPhi(), ERROR_MARGIN);
	}

}
