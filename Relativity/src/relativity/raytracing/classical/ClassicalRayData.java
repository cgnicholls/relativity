package relativity.raytracing.classical;

import relativity.scene.objects.RelativisticSceneObject;
import relativity.toolkit.Vector3;

public class ClassicalRayData {
	private Vector3 _rayDirection;
	private Vector3 _rayPosition;
	
	private Vector3 _bestIntersection;
	private ClassicalSceneObject _bestObject;
	private int _bestColour;
	
	public ClassicalRayData(final Vector3 rayDirection, final Vector3 rayPosition) {
		_rayDirection = rayDirection;
		_rayPosition = rayPosition;
		
		_bestIntersection = null;
		_bestObject = null;
		_bestColour = 0;
	}
	
	public void setBestIntersection(final Vector3 bestIntersection) {
		_bestIntersection = bestIntersection;
	}
	
	public void setBestObject(final ClassicalSceneObject bestObject) {
		_bestObject = bestObject;
	}
	
	public void setBestColour(final int bestColour) {
		_bestColour = bestColour;
	}

	public boolean hasHit() {
		return (_bestObject != null);
	}

	public ClassicalSceneObject getBestObject() {
		return _bestObject;
	}
	
	public Vector3 getBestIntersection() {
		return _bestIntersection;
	}
	
	public int getBestColour() {
		return _bestColour;
	}

	public Vector3 getRayDirection() {
		return _rayDirection;
	}
	
	public Vector3 getRayPosition() {
		return _rayPosition;
	}

}
