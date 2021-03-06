package relativity.toolkit;

import static org.junit.Assert.*;


import org.junit.Test;

import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.Vector3;


public class TestIntersectionFinder {

	@Test
	public void testNearestPointOfIntersectionNoIntersection() {
		Vector3 rayPosition = new Vector3(0.0, 0.0, 0.0);
		Vector3 rayDirection = new Vector3(0.0, 1.0, 0.0);
		Vector3 spherePosition = new Vector3(2.0, 0.0, 0.0);
		double sphereRadius = 1.0;
		
		assertEquals(null, IntersectionFinder.nearestPointOfIntersection(rayPosition, rayDirection, spherePosition, sphereRadius));
	}

	@Test
	public void testNearestPointOfIntersectionIntersection() {
		Vector3 rayPosition = new Vector3(0.0, 0.0, 0.0);
		Vector3 rayDirection = new Vector3(0.0, 1.0, 0.0);
		Vector3 spherePosition = new Vector3(0.0, 2.0, 0.0);
		double sphereRadius = 1.0;
		
		assertNotNull(IntersectionFinder.nearestPointOfIntersection(rayPosition, rayDirection, spherePosition, sphereRadius));
	}
	
}
