package relativity.raytracing.relativistic;

import relativity.toolkit.Vector3;

public class RelativisticCamera {
	// Position of the camera in the frame of the scene.
	private Vector3 _position;
	private Vector3 _velocity;
	
	// Directions in the camera's rest frame
	private RelativisticFrame _frameAtRest;
	
	// Used for stereographic projection
	private double _screenDistance;
	private double _horizontalFrustumAngle;
	private double _sphereOfSightRadius;
	
	public RelativisticCamera() {
		_position = new Vector3(0.0, 0.0, 0.0);
		_velocity = new Vector3(0.0, 0.0, 0.0);
		
		Vector3 forward = new Vector3(0.0, 1.0, 0.0);
		Vector3 right = new Vector3(1.0, 0.0, 0.0);
		Vector3 up = new Vector3(0.0, 0.0, 1.0);
		_frameAtRest = new RelativisticFrame(forward, right, up);
		
		_screenDistance = 500.0;
		_horizontalFrustumAngle = Math.PI / 3.0;
		_sphereOfSightRadius = 500.0;
	}
	
	public RelativisticCamera(final Vector3 position, final RelativisticFrame frameAtRest) {
		_frameAtRest = frameAtRest;
		
		_position = position;
		_velocity = new Vector3(0.0, 0.0, 0.0);
		
		_screenDistance = 500.0;
		_horizontalFrustumAngle = Math.PI / 3.0;
		_sphereOfSightRadius = 500.0;
	}
	
	public RelativisticFrame getFrameInScene() {
		return _frameAtRest.boost(_velocity.negate());
	}
	
	public RelativisticFrame getFrameAtRest() {
		return _frameAtRest;
	}
	
	public Vector3 getPosition() {
		return _position;
	}
	
	public double getScreenDistance() {
		return _screenDistance;
	}
	
	public double getHorizontalFrustumAngle() {
		return _horizontalFrustumAngle;
	}
	
	public double getSphereOfSightRadius() {
		return _sphereOfSightRadius;
	}

	public Vector3 getVelocity() {
		return _velocity;
	}

	public void setVelocity(Vector3 velocity) {
		_velocity = velocity;
	}

	public void setPosition(Vector3 position) {
		_position = position;
	}

	public void rotateAboutUp(final double angle) {
		_frameAtRest = RelativisticCameraMotion.rotateRestFrameAroundAxis(_frameAtRest, _frameAtRest.getV3(), angle);
	}
	
	public void rotateAboutRight(final double angle) {
		_frameAtRest = RelativisticCameraMotion.rotateRestFrameAroundAxis(_frameAtRest, _frameAtRest.getV2(), angle);
	}
	
	public void rotateAboutForward(final double angle) {
		_frameAtRest = RelativisticCameraMotion.rotateRestFrameAroundAxis(_frameAtRest, _frameAtRest.getV1(), angle);
	}
	
	/*
	 * Given a time in the scene of the frame, update the position of the camera.
	 */
	public void updatePosition(double deltaT) {
		_position = _position.plus(_velocity.scalarMultiply(deltaT));
	}

	public void setFrame(RelativisticFrame frameAtRest) {
		_frameAtRest = frameAtRest;
	}
}
