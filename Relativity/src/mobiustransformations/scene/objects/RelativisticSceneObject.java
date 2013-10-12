package mobiustransformations.scene.objects;

import mobiustransformations.relativisticraytracer.RelativisticRayData;
import mobiustransformations.toolkit.FourVector;
import mobiustransformations.toolkit.Vector3;

public interface RelativisticSceneObject {
	// Compute the ray data, given a ray in the rest frame of the scene.
	// The object knows its own velocity.
	public void computeRay(RelativisticRayData rayData);
	
	// A SceneObject has a velocity.
	public Vector3 getVelocity();
}
