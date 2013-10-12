package relativity.classicalraytracer;

import relativity.classicalraytracer.CameraMotion.Frame;
import relativity.toolkit.Vector3;

public class Camera {
	// Position of the camera in the frame of the scene.
	private Vector3 _position;
	private Vector3 _velocity;
	
	// Directions in the camera's rest frame
	private Vector3 _forward;
	private Vector3 _right;
	private Vector3 _up;
	
	// Used for stereographic projection
	private double _screenDistance;
	private double _horizontalFrustumAngle;
	private double _sphereOfSightRadius;
	
	public Camera() {
		_position = new Vector3(0.0, 0.0, 0.0);
		_velocity = new Vector3(0.0, 0.0, 0.0);
		
		_forward = new Vector3(0.0, 1.0, 0.0);
		_right = new Vector3(1.0, 0.0, 0.0);
		_up = new Vector3(0.0, 0.0, 1.0);
		
		_screenDistance = 500.0;
		_horizontalFrustumAngle = Math.PI / 3.0;
		_sphereOfSightRadius = 500.0;
	}
	
	public Camera(final Vector3 position, final Frame frame) {
		_forward = frame.getV1();
		_right = frame.getV2();
		_up = frame.getV3();
		
		_position = position;
		_velocity = new Vector3(0.0, 0.0, 0.0);
		
		_screenDistance = 500.0;
		_horizontalFrustumAngle = Math.PI / 3.0;
		_sphereOfSightRadius = 500.0;
	}
	
	public Frame getFrame() {
		return new Frame(_forward, _right, _up);
	}
	
	public Vector3 getPosition() {
		return _position;
	}
	
	public Vector3 getForward() {
		return _forward;
	}
	
	public Vector3 getRight() {
		return _right;
	}
	
	public Vector3 getUp() {
		return _up;
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
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		Vector3 newRight = _right.scalarMultiply(cosAngle).plus(_forward.scalarMultiply(sinAngle));
		Vector3 newForward = _right.scalarMultiply(-sinAngle).plus(_forward.scalarMultiply(cosAngle));
		_right = newRight.normalise();
		_forward = newForward.normalise();
	}
	
	public void rotateAboutRight(final double angle) {
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		Vector3 newForward = _forward.scalarMultiply(cosAngle).plus(_up.scalarMultiply(sinAngle));
		Vector3 newUp = _forward.scalarMultiply(-sinAngle).plus(_up.scalarMultiply(cosAngle));
		_forward = newForward.normalise();
		_up = newUp.normalise();
	}
	
	public void rotateAboutForward(final double angle) {
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		Vector3 newUp = _up.scalarMultiply(cosAngle).plus(_right.scalarMultiply(sinAngle));
		Vector3 newRight = _up.scalarMultiply(-sinAngle).plus(_right.scalarMultiply(cosAngle));
		_up = newUp.normalise();
		_right = newRight.normalise();
	}
	
	/*
	 * We add zoom to _distanceToScreen, but only if this will result in a still positive distance.
	 */
	public void zoomIn(double zoom) {
		if (_screenDistance + zoom > 0.0) {
			_screenDistance += zoom;			
		}
	}
	
	/*
	 * Move forward (as given by the StereographicViewer) by a given distance.
	 */	
	public void translateForward(double distance) {
		_position = _position.plus(_forward.scalarMultiply(distance));
	}

	/*
	 * Move right (as given by the StereographicViewer) by a given distance.
	 */
	public void translateRight(double distance) {
		_position = _position.plus(_right.scalarMultiply(distance));
	}

	public void translateUp(double distance) {
		_position = _position.plus(_up.scalarMultiply(distance));
	}
}
