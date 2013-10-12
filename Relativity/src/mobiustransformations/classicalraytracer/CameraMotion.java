package mobiustransformations.classicalraytracer;

import mobiustransformations.classicalraytracer.CameraMotion.Frame;
import mobiustransformations.toolkit.Vector3;

/*
 * This class provides easy to use functions to update the view of a Camera.
 */
public class CameraMotion {
	public static class Frame {
		private Vector3 _v1;
		private Vector3 _v2;
		private Vector3 _v3;
		
		public Frame(final Vector3 v1, final Vector3 v2, final Vector3 v3) {
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
	}

	/*
	 * Rotate the frame at a given position, about a point a given distance away in the look direction, perpendicular to the v3 direction (up direction).
	 */
	public static Frame rotateFrameAboutDistancePerpV3(final Frame frame, final double distanceToCentre, final double arcLength) {
		Vector3 v1 = frame.getV1().normalise();
		Vector3 v2 = frame.getV2().normalise();
		Vector3 v3 = frame.getV3().normalise();
		
		Vector3 v1Prime = Vector3.rotateVectorAroundAxisByAngle(v1, v3, arcLength / distanceToCentre);
		Vector3 v2Prime = Vector3.rotateVectorAroundAxisByAngle(v2, v3, arcLength / distanceToCentre);
		
		return new Frame(v1Prime, v2Prime, v3);
	}

	/*
	 * Move in the given direction by the given distance. Assumes direction is unit length.
	 */	
	public static Vector3 translateByDirection(Vector3 position, Vector3 direction, double distance) {
		return position.plus(direction.scalarMultiply(distance));
	}

	public static Frame rotateFrameAboutDistancePerpV2(Frame frame, double distanceToCentre, double arcLength) {
		Vector3 v1 = frame.getV1().normalise();
		Vector3 v2 = frame.getV2().normalise();
		Vector3 v3 = frame.getV3().normalise();
		
		Vector3 v1Prime = Vector3.rotateVectorAroundAxisByAngle(v1, v2, arcLength / distanceToCentre);
		Vector3 v3Prime = Vector3.rotateVectorAroundAxisByAngle(v3, v2, arcLength / distanceToCentre);
		
		return new Frame(v1Prime, v2, v3Prime);
	}
}
