package mobiustransformations.toolkit;

public class IntersectionFinder {

	/*
	 * Returns the nearest point of intersection of a ray with a sphere, or null, if there is none.
	 */
	public static Vector3 nearestPointOfIntersection(Vector3 rayPosition,
			Vector3 rayDirection, Vector3 spherePosition, double sphereRadius) {
		double a = rayDirection.lengthSquared();
		double b = 2 * rayDirection.dotProduct((rayPosition.minus(spherePosition)));
		double c = (rayPosition.minus(spherePosition)).lengthSquared() - sphereRadius * sphereRadius;
		
		// If the discriminant is less than zero, then no solution
		if (b * b - 4 * a * c < 0.0) {
			return null;
		}
		
		// Else, we need to find the two solutions, t0 and t1 and then return the minimum which is positive.
		double sqrtDiscriminant = Math.sqrt(b * b - 4 * a * c);
		
		double t0 = (-b - sqrtDiscriminant) / (2.0 * a);
		double t1 = (-b + sqrtDiscriminant) / (2.0 * a);
		double t;
		
		// t1 is the larger of the two solutions, so if it is negative, then there is no solution.
		if (t1 < 0.0) {
			return null;
		}
		
		// If t0 is positive, then this is the earliest point of intersection, else t1 is.
		if (t0 < 0.0) {
			t = t1;
		} else {
			t = t0;
		}
		
		return rayPosition.plus(rayDirection.scalarMultiply(t));
	}

	public static Vector3 nearestPointOfIntersectionCylinder(Vector3 rayPosition,
			Vector3 rayDirection, Vector3 u, Vector3 v, double radius) {
		double uvLengthSquared = (v.subtract(u)).lengthSquared();
		double a = rayDirection.lengthSquared() * uvLengthSquared - rayDirection.dotProduct(v.subtract(u)) * rayDirection.dotProduct(v.subtract(u));
		double b = 2.0 * rayDirection.dotProduct(rayPosition.subtract(u)) * uvLengthSquared - 2.0 * rayDirection.dotProduct(v.subtract(u)) * (rayPosition.subtract(u)).dotProduct(v.subtract(u));
		double c = uvLengthSquared * (rayPosition.subtract(u)).lengthSquared() - ((rayPosition.subtract(u)).dotProduct(v.subtract(u))) * ((rayPosition.subtract(u)).dotProduct(v.subtract(u))) - radius * radius * uvLengthSquared;
		
		// If the discriminant is less than zero, then no solution
		if (b * b - 4 * a * c < 0.0) {
			return null;
		}
		
		// Else, we need to find the two solutions, t0 and t1 and then return the minimum which is positive.
		double sqrtDiscriminant = Math.sqrt(b * b - 4 * a * c);
		
		double t0 = (-b - sqrtDiscriminant) / (2.0 * a);
		double t1 = (-b + sqrtDiscriminant) / (2.0 * a);
		double t;
		
		// t1 is the larger of the two solutions, so if it is negative, then there is no solution.
		if (t1 < 0.0) {
			return null;
		}
		
		// Now consider the points of intersection of the cylinder - we want them to lie on the cylindrical segment.
		Vector3 p0 = rayPosition.plus(rayDirection.scalarMultiply(t0));
		Vector3 p1 = rayPosition.plus(rayDirection.scalarMultiply(t1));
		double lambda0 = (p0.subtract(u)).dotProduct(v.subtract(u));
		double lambda1 = (p1.subtract(u)).dotProduct(v.subtract(u));
		
		// If t0 is positive, then this is the earliest point of intersection, else t1 is.
		if (t0 < 0.0 && lambda0 >= 0.0 && lambda0 <= uvLengthSquared) {
			t = t1;
		} else if (lambda1 >= 0.0 && lambda1 <= uvLengthSquared){
			t = t0;
		} else {
			return null;
		}
		
		return rayPosition.plus(rayDirection.scalarMultiply(t));	
	}
}
