package relativity.scene.objects;

import relativity.raytracing.relativistic.RelativisticRayData;
import relativity.toolkit.FourVector;
import relativity.toolkit.Vector3;

public interface RelativisticSceneObject {
	// Compute the ray data, given a ray in the rest frame of the scene.
	// The object knows its own velocity.
	public void computeRay(RelativisticRayData rayData);
	
	// A SceneObject has a velocity.
	public Vector3 getVelocity();
}
