package relativity.raytracing.relativistic;

import relativity.toolkit.Quaternion;
import relativity.toolkit.Vector3;

public class RelativisticCameraMotion {
	
	public static RelativisticFrame rotateRestFrameAroundAxis(final RelativisticFrame frameAtRest, final Vector3 axis, final double angle) {
		Vector3 v1 = frameAtRest.getV1();
		Vector3 v2 = frameAtRest.getV2();
		Vector3 v3 = frameAtRest.getV3();
		
		Vector3 v1Prime = Quaternion.rotateVectorAroundAxisByAngle(v1, axis, angle);
		Vector3 v2Prime = Quaternion.rotateVectorAroundAxisByAngle(v2, axis, angle);
		Vector3 v3Prime = Quaternion.rotateVectorAroundAxisByAngle(v3, axis, angle);
		
		return new RelativisticFrame(v1Prime, v2Prime, v3Prime);
	}
}
