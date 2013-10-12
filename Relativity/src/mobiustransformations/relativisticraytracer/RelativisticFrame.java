package mobiustransformations.relativisticraytracer;

import mobiustransformations.toolkit.FourVector;
import mobiustransformations.toolkit.Lorentz;
import mobiustransformations.toolkit.Vector3;

public class RelativisticFrame {

	private Vector3 _v1;
	private Vector3 _v2;
	private Vector3 _v3;
	
	public RelativisticFrame(final Vector3 v1, final Vector3 v2, final Vector3 v3) {
		_v1 = v1;
		_v2 = v2;
		_v3 = v3;
	}
	
	public Vector3 getV1() {
		return _v1;
	}
	
	public Vector3 getV2() {
		return _v2;
	}
	
	public Vector3 getV3() {
		return _v3;
	}
	
	public void setV1(final Vector3 v1) {
		_v1 = v1;
	}
	public void setV2(final Vector3 v2) {
		_v2 = v2;
	}
	public void setV3(final Vector3 v3) {
		_v3 = v3;
	}
	
	/*
	 * Return the RelativisticFrame as seen by an observer moving with given velocity.
	 */
	public RelativisticFrame boost(final Vector3 velocity) {
		Vector3 v1Prime = Lorentz.boost(velocity, FourVector.lightRay(_v1)).getSpatial().normalise();
		Vector3 v2Prime = Lorentz.boost(velocity, FourVector.lightRay(_v2)).getSpatial().normalise();
		Vector3 v3Prime = Lorentz.boost(velocity, FourVector.lightRay(_v3)).getSpatial().normalise();
		return new RelativisticFrame(v1Prime, v2Prime, v3Prime);
	}
}
