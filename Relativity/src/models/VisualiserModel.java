package models;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import relativity.raytracing.relativistic.Camera;
import relativity.raytracing.relativistic.RelativisticFrame;
import relativity.raytracing.relativistic.RelativisticRayTracer;
import relativity.scene.RelativisticScene;
import relativity.toolkit.FourVector;
import relativity.toolkit.Lorentz;
import relativity.toolkit.Vector3;

import controller.KeyboardInput;
import display.DisplayPanel;


public class VisualiserModel implements Model {
	
	private RelativisticRayTracer _relativisticRayTracer;
	private Camera _relativisticCamera;
	private RelativisticScene _relativisticScene;
	
	private double _beta;
	private boolean _updatePosition;
	
	public VisualiserModel() {
		_relativisticRayTracer = new RelativisticRayTracer(8);
		setUpScene();
		initCamera();
		
		_updatePosition = true;
	}
	
	private void setUpScene() {
		_relativisticScene = new RelativisticScene();
		_relativisticScene.init();
	}
	
	private void initCamera() {
		_relativisticCamera = new Camera();
		_relativisticCamera.setPosition(new Vector3(0.0, 0.0, 0.0));
		_beta = 0.0;		
	}
	
	public void renderMethod(final DisplayPanel displayPanel) {
		final Rectangle fullScreen = displayPanel.getBounds();
		
		_relativisticRayTracer.render(displayPanel, fullScreen, _relativisticScene, _relativisticCamera);
	}

	public void update(double deltaT) {
		if (_updatePosition)
			_relativisticCamera.updatePosition(deltaT);
	}
	
	/*
	 * Processes the keyboard input. In particular, applies the moves to the viewer given by the key controls.
	 */
	public void processKeyboardInput(final double deltaT, final KeyboardInput keyboardInput) {
		if (keyboardInput.keyDown(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
		
		if (keyboardInput.keyDown(KeyEvent.VK_SPACE)) {
			initCamera();
		}
		
		if (keyboardInput.keyDownOnce(KeyEvent.VK_ENTER)) {
			_updatePosition = !_updatePosition;
		}
		
		double moveSpeed = 0.2;
		double rotationSpeed = 0.3;
		if (keyboardInput.keyDown(KeyEvent.VK_SHIFT)) {
			double moveMultiplier = 10.0;
			moveSpeed *= moveMultiplier;
			
			double rotationMultiplier = 10.0;
			rotationSpeed *= rotationMultiplier;
		}

		Vector3 accelerationDirection = new Vector3(0.0, 0.0, 0.0);
		RelativisticFrame frameInScene = _relativisticCamera.getFrameInScene();
		if (keyboardInput.keyDown(KeyEvent.VK_W)) {
			double tanhA = _beta;
			double tanhB = Math.tanh(deltaT * moveSpeed);
			_beta = (tanhA + tanhB) / (1.0 + tanhA * tanhB);
			
			accelerationDirection = accelerationDirection.plus(frameInScene.getV1());
		}
		if (keyboardInput.keyDown(KeyEvent.VK_S)) {
			double tanhA = _beta;
			double tanhB = Math.tanh(deltaT * moveSpeed);
			_beta = (tanhA - tanhB) / (1.0 - tanhA * tanhB);
			
			accelerationDirection = accelerationDirection.minus(frameInScene.getV1());
		}
		if (keyboardInput.keyDown(KeyEvent.VK_D)) {		
			accelerationDirection = accelerationDirection.plus(frameInScene.getV2());
		}
		if (keyboardInput.keyDown(KeyEvent.VK_A)) {
			accelerationDirection = accelerationDirection.minus(frameInScene.getV2());
		}
		if (keyboardInput.keyDown(KeyEvent.VK_SPACE)) {
			accelerationDirection = accelerationDirection.plus(frameInScene.getV3());
		}
		if (keyboardInput.keyDown(KeyEvent.VK_ALT)) {
			accelerationDirection = accelerationDirection.minus(frameInScene.getV3());
		}
		
		if (keyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			_relativisticCamera.rotateAboutUp(-deltaT * rotationSpeed);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			_relativisticCamera.rotateAboutUp(deltaT * rotationSpeed);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_UP)) {
			_relativisticCamera.rotateAboutRight(-deltaT * rotationSpeed);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			_relativisticCamera.rotateAboutRight(deltaT * rotationSpeed);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_E)) {
			_relativisticCamera.rotateAboutForward(-deltaT * rotationSpeed);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_Q)) {
			_relativisticCamera.rotateAboutForward(deltaT * rotationSpeed);
		}
		
		double MAX_SPEED = 0.9999999999;
		if (_beta < -MAX_SPEED) _beta = -MAX_SPEED;
		if (_beta > MAX_SPEED) _beta = MAX_SPEED;
		Vector3 currentCameraVelocity = _relativisticCamera.getVelocity();
		
		// If the camera had been accelerating uniformly in the given direction for time deltaT, then the current velocity would be:
		FourVector deltaV = FourVector.fourVelocity(accelerationDirection.scalarMultiply(deltaT));
		FourVector boostedV = Lorentz.boost(currentCameraVelocity, deltaV);
		
		_relativisticCamera.setVelocity(_relativisticCamera.getFrameAtRest().getV1().normalise().scalarMultiply(_beta * Lorentz.SPEED_OF_LIGHT));
		
//		
//		// Get the StereographicViewer from the Camera
//		Camera viewer = _cameraClassical;
//		Frame frame = _cameraClassical.getFrame();
//		Vector3 position = _cameraClassical.getPosition();
//		double distance = 300.0;
//		if (keyboardInput.keyDown(KeyEvent.VK_UP)) {
//			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV2(frame, distance, -rotationSpeed * deltaT * distance);
//			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
//			frame = framePrime;
//			_cameraClassical = new Camera(position, frame);
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_DOWN)) {
//			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV2(frame, distance, rotationSpeed * deltaT * distance);
//			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
//			frame = framePrime;
//			_cameraClassical = new Camera(position, frame);
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
//			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV3(frame, distance, -rotationSpeed * deltaT * distance);
//			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
//			frame = framePrime;
//			_cameraClassical = new Camera(position, frame);
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_LEFT)) {
//			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV3(frame, distance, rotationSpeed * deltaT * distance);
//			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
//			frame = framePrime;
//			_cameraClassical = new Camera(position, frame);
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_Q)) {
//			viewer.rotateAboutForward(-rotationSpeed * deltaT);
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_E)) {
//			viewer.rotateAboutForward(rotationSpeed * deltaT);
//		}
//		
//		// Change the Mobius transformation
//		if (keyboardInput.keyDown(KeyEvent.VK_W)) {
//			_lambdaAlpha += moveSpeed * deltaT;
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_S)) {
//			_lambdaAlpha -= moveSpeed * deltaT;
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_D)) {
//			_lambdaBeta += moveSpeed * deltaT;
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_A)) {
//			_lambdaBeta -= moveSpeed * deltaT;
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
//		
//		if (keyboardInput.keyDown(KeyEvent.VK_SPACE)) {
//			_lambdaAlpha = 0.0;
//			_lambdaBeta = 0.0;
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
//		
//		if (keyboardInput.keyDown(KeyEvent.VK_K)) {
//			_t += moveSpeed * deltaT;
//			_fixedPoint1 = new HomCoords2D(new Complex(_t), new Complex(1.0 - _t));
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
//		if (keyboardInput.keyDown(KeyEvent.VK_J)) {
//			_t -= moveSpeed * deltaT;
//			_fixedPoint1 = new HomCoords2D(new Complex(_t), new Complex(1.0 - _t));
//			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
//		}
	}
}
