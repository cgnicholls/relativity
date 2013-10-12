package relativity.raytracing.relativistic;

import relativity.scene.objects.RelativisticSceneObject;
import relativity.toolkit.FourVector;

public class RelativisticRayData {
	private FourVector _rayDirection;
	private FourVector _rayPosition;
	
	private FourVector _bestIntersection;
	private RelativisticSceneObject _bestObject;
	private int _bestColour;
	
	public RelativisticRayData(final FourVector rayDirection, final FourVector rayPosition) {
		_rayDirection = rayDirection;
		_rayPosition = rayPosition;
		
		_bestIntersection = null;
		_bestObject = null;
		_bestColour = 0;
	}
	
	public void setBestIntersection(final FourVector bestIntersection) {
		_bestIntersection = bestIntersection;
	}
	
	public void setBestObject(final RelativisticSceneObject bestObject) {
		_bestObject = bestObject;
	}
	
	public void setBestColour(final int bestColour) {
		_bestColour = bestColour;
	}

	public boolean hasHit() {
		return (_bestObject != null);
	}

	public RelativisticSceneObject getBestObject() {
		return _bestObject;
	}
	
	public FourVector getBestIntersection() {
		return _bestIntersection;
	}
	
	public int getBestColour() {
		return _bestColour;
	}

	public FourVector getRayDirection() {
		return _rayDirection;
	}
	
	public FourVector getRayPosition() {
		return _rayPosition;
	}
}
