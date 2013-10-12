package relativity.raytracing.classical;

import java.awt.Color;

import relativity.raytracing.relativistic.Camera;
import relativity.raytracing.relativistic.RelativisticRayTracerLibrary;
import relativity.scene.RelativisticScene;
import relativity.scene.objects.SphericalTexture;
import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.Vector3;



public class SphereOfSight implements ClassicalSceneObject {
	private Vector3 _position;
	private double _radius;
	private RelativisticScene _relativisticScene;
	private Camera _camera;
	
	public SphereOfSight(final Vector3 position, final double radius, final RelativisticScene relativisticScene, final Camera camera) {
		_position = position;
		_radius = radius;
		_relativisticScene = relativisticScene;
		_camera = camera;
	}

	@Override
	public void computeRay(ClassicalRayData rayData) {
		// Now calculate the nearest point of intersection
		Vector3 nearestPointOfIntersection = IntersectionFinder.nearestPointOfIntersection(rayData.getRayPosition(), rayData.getRayDirection(), _position, _radius);
		
		// If no point of intersection, then we just return null
		if (nearestPointOfIntersection == null) {
			return;
		}
		
		Vector3 directionOnUnitSphere = (nearestPointOfIntersection.minus(_position)).normalise();
		
		int rgb = RelativisticRayTracerLibrary.getColour(_camera.getPosition(), directionOnUnitSphere, _camera.getVelocity(), _relativisticScene, new Color(200, 200, 200));
		
		// Else, we set the intersection and best objects
		rayData.setBestIntersection(nearestPointOfIntersection);
		rayData.setBestObject(this);
		rayData.setBestColour(rgb);
	}
	
}
