package relativity.classicalraytracer;


public interface ClassicalSceneObject {
	// Compute the ray data, given a ray in the rest frame of the scene.
	// The object knows its own velocity.
	public void computeRay(ClassicalRayData rayData);
}
