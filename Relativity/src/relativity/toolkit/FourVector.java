package relativity.toolkit;

/**
 * A FourVector has components (t, x, y, z), and inner product c^2*t^2 - x^2 - y^2 - z^2.
 * @author chris
 *
 */
public class FourVector {
	private double _t;
	private Vector3 _spatial;
	
	public FourVector(final double t, final Vector3 spatial) {
		_t = t;
		_spatial = spatial;
	}
	
	public double getT() {
		return _t;
	}
	
	public Vector3 getSpatial() {
		return _spatial;
	}

	/*
	 * Returns c^2 * t^2 - x^2 - y^2 - z^2.
	 */
	public double innerProduct() {
		double c = Lorentz.SPEED_OF_LIGHT;
		return _t * _t * c * c - _spatial.lengthSquared();
	}
	
	/*
	 * If the FourVector represents a four velocity, then it has components (gamma, v * gamma), where v is the observed velocity.
	 * This method returns v.
	 */
	public Vector3 observedVelocity() {
		double gamma = _t;
		return _spatial.scalarMultiply(1.0 / gamma);
	}

	/*
	 * Returns a FourVector given the desired spatial component of the four velocity.
	 * Recall that a FourVelocity always has magnitude c.
	 */
	public static FourVector fourVelocity(Vector3 velocity) {
		double velocitySquared = velocity.lengthSquared();
		double c = Lorentz.SPEED_OF_LIGHT;
		double gamma = Lorentz.gamma(velocitySquared);
		return new FourVector(gamma, velocity.scalarMultiply(gamma));
	}
	
	/*
	 * Returns a FourVector with given spatial component, and such that the inner product is zero.
	 */
	public static FourVector lightRay(Vector3 spatial) {
		double c = Lorentz.SPEED_OF_LIGHT;
		
		// Return the FourVector given by (|spatial|/c, spatial), so that the inner product is c^2 * |spatial|^2 / c^2 - |spatial|^2 = 0 
		return new FourVector(spatial.length() / c, spatial);
	}
	
	public FourVector makePastPointing() {
		return new FourVector(-Math.abs(_t), _spatial);
	}
}
