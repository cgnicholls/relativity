package relativity.toolkit;

public class Lorentz {
	public static final double SPEED_OF_LIGHT = 60.0;
	
	public static FourVector boostZ(final double velocity, final FourVector fourVector) {
		double c = SPEED_OF_LIGHT;
		double t = fourVector.getT();
		double x = fourVector.getSpatial().getX();
		double y = fourVector.getSpatial().getY();
		double z = fourVector.getSpatial().getZ();
		double gamma = gamma(velocity * velocity);
		double tPrime = gamma * (t - velocity * z / (c*c));
		double zPrime = gamma * (z - velocity * t);
		return new FourVector(tPrime, new Vector3(x, y, zPrime));
	}
	
	/*
	 * Applies a Lorentz boost to a four vector with the given velocity.
	 */
	public static FourVector boost(final Vector3 velocity, final FourVector fourVector) {
		double c = SPEED_OF_LIGHT;
		double t = fourVector.getT();
		Vector3 spatial = fourVector.getSpatial();
		
		// if the velocity is negligible, then ignore relativistic effects
		if (velocity.lengthSquared() / c < 0.001) {
			return fourVector;
		}
		
		double gamma = gamma(velocity.lengthSquared());
		double tPrime = gamma * (t - spatial.dotProduct(velocity)/(c*c));
		Vector3 spatialPrime = spatial.plus(velocity.scalarMultiply((gamma-1.0)/velocity.lengthSquared() * spatial.dotProduct(velocity) - gamma * t));
		return new FourVector(tPrime, spatialPrime);
	}

	public static double gamma(final double velocitySquared) {
		return 1.0 / Math.sqrt((1.0 - velocitySquared / (SPEED_OF_LIGHT * SPEED_OF_LIGHT)));
	}


}
