package relativity.toolkit;

public class SphericalPolars {
	private double _theta;
	private double _phi;
	
	public SphericalPolars(final double theta, final double phi) {
		_theta = theta;
		_phi = phi;
	}
	
	public double getTheta() {
		return _theta;
	}
	
	public double getPhi() {
		return _phi;
	}
	
	public Vector3 computeVector() {
		return new Vector3(Math.sin(_theta) * Math.cos(_phi), Math.sin(_theta) * Math.cos(_phi), Math.cos(_theta));
	}
	
	/*
	 * Given a unit vector, returns the spherical polars corresponding to it.
	 */
	public static SphericalPolars computeSphericalPolars(final Vector3 unitV) {
		double theta = Math.acos(unitV.getZ());
		double phi = 0.0;
		double ERROR_MARGIN = 0.01;
		if (unitV.getZ() < -1.0 + ERROR_MARGIN  || unitV.getZ() > 1.0 - ERROR_MARGIN) {
			phi = 0.0;
		} else {
			// unitVector = (sin(theta) cos(phi), sin(theta) sin(phi), cos(theta)).
			// Thus, to find phi, it suffices to take acos(unitVector.getX() / sin(theta)).
			double sinTheta = Math.sqrt(1.0 - unitV.getZ() * unitV.getZ());
			phi = Math.acos(unitV.getX() / sinTheta);
			
			if (unitV.getY() < 0.0) {
				phi = Math.PI * 2.0 - phi;
			}
		}
		
		return new SphericalPolars(theta, phi);
	}
}
