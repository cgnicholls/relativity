package models;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import relativity.raytracing.classical.CameraMotion;
import relativity.raytracing.classical.ClassicalRayTracer;
import relativity.raytracing.classical.ClassicalScene;
import relativity.raytracing.classical.MobiusSphere;
import relativity.raytracing.classical.CameraMotion.Frame;
import relativity.raytracing.relativistic.Camera;
import relativity.toolkit.Complex;
import relativity.toolkit.GL2C;
import relativity.toolkit.HomCoords2D;
import relativity.toolkit.MobiusTransformation;
import relativity.toolkit.Vector3;

import controller.KeyboardInput;
import display.DisplayPanel;


public class MobiusModel implements Model {
	private ClassicalScene _sceneClassical;
	private Camera _cameraClassical;
	
	private ClassicalRayTracer _classicalRayTracer;
	private MobiusSphere _mobiusSphere;
	
	private double _lambdaAlpha;
	private double _lambdaBeta;
	private HomCoords2D _fixedPoint1;
	private HomCoords2D _fixedPoint2;
	private double _t;
	
	public MobiusModel() {
		_sceneClassical = new ClassicalScene();
		_sceneClassical.init();
		_cameraClassical = new Camera();
		
		_classicalRayTracer = new ClassicalRayTracer(8);
		
		_lambdaAlpha = 0.0;
		_lambdaBeta = 0.0;
		_fixedPoint1 = HomCoords2D.southPole();
		_fixedPoint2 = HomCoords2D.northPole();
		GL2C matrix = GL2C.diagonal(computeLambda(_lambdaAlpha, _lambdaBeta), new Complex(1.0, 0.0));
		MobiusTransformation mobiusTransformation = new MobiusTransformation(matrix);
		_mobiusSphere = new MobiusSphere(new Vector3(0.0, 300.0, 0.0), 100.0, mobiusTransformation, 1500, 10, 150);
		
		_t = 0.0;
	}
	
	private Complex computeLambda(double lambdaAlpha, double lambdaBeta) {
		return Complex.polars(Math.exp(lambdaAlpha), lambdaBeta);
	}

	public void renderMethod(final DisplayPanel displayPanel) {
		final Rectangle fullScreen = displayPanel.getBounds();
		
		ClassicalScene sceneClassicalZeroVelocity = new ClassicalScene();
		sceneClassicalZeroVelocity.addObject(_mobiusSphere);
		_classicalRayTracer.render(displayPanel, fullScreen, sceneClassicalZeroVelocity, _cameraClassical);
	}

	public void update(double deltaT) {

	}
	
	/*
	 * Processes the keyboard input. In particular, applies the moves to the viewer given by the key controls.
	 */
	public void processKeyboardInput(final double deltaT, final KeyboardInput keyboardInput) {
		if (keyboardInput.keyDown(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
		
		double rotationSpeed = 0.3;
		double moveSpeed = 0.1;
		if (keyboardInput.keyDown(KeyEvent.VK_SHIFT)) {
			double rotationMultiplier = 5.0;
			double moveMultiplier = 10.0;
			rotationSpeed *= rotationMultiplier;
			moveSpeed *= moveMultiplier;
		}
		
		// Get the StereographicViewer from the Camera
		Camera viewer = _cameraClassical;
		Frame frame = _cameraClassical.getFrame();
		Vector3 position = _cameraClassical.getPosition();
		double distance = 300.0;
		if (keyboardInput.keyDown(KeyEvent.VK_UP)) {
			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV2(frame, distance, -rotationSpeed * deltaT * distance);
			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
			frame = framePrime;
			_cameraClassical = new Camera(position, frame);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV2(frame, distance, rotationSpeed * deltaT * distance);
			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
			frame = framePrime;
			_cameraClassical = new Camera(position, frame);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV3(frame, distance, -rotationSpeed * deltaT * distance);
			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
			frame = framePrime;
			_cameraClassical = new Camera(position, frame);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			Frame framePrime = CameraMotion.rotateFrameAboutDistancePerpV3(frame, distance, rotationSpeed * deltaT * distance);
			position = position.plus(frame.getV1().scalarMultiply(distance)).minus(framePrime.getV1().scalarMultiply(distance));
			frame = framePrime;
			_cameraClassical = new Camera(position, frame);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_Q)) {
			viewer.rotateAboutForward(-rotationSpeed * deltaT);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_E)) {
			viewer.rotateAboutForward(rotationSpeed * deltaT);
		}
		
		// Change the Mobius transformation
		if (keyboardInput.keyDown(KeyEvent.VK_W)) {
			_lambdaAlpha += moveSpeed * deltaT;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
		if (keyboardInput.keyDown(KeyEvent.VK_S)) {
			_lambdaAlpha -= moveSpeed * deltaT;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
		if (keyboardInput.keyDown(KeyEvent.VK_D)) {
			_lambdaBeta += moveSpeed * deltaT;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
		if (keyboardInput.keyDown(KeyEvent.VK_A)) {
			_lambdaBeta -= moveSpeed * deltaT;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
		
		if (keyboardInput.keyDown(KeyEvent.VK_SPACE)) {
			_lambdaAlpha = 0.0;
			_lambdaBeta = 0.0;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
		
		if (keyboardInput.keyDown(KeyEvent.VK_K)) {
			_t += moveSpeed * deltaT;
			_fixedPoint1 = new HomCoords2D(new Complex(_t), new Complex(1.0 - _t));
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
		if (keyboardInput.keyDown(KeyEvent.VK_J)) {
			_t -= moveSpeed * deltaT;
			_fixedPoint1 = new HomCoords2D(new Complex(_t), new Complex(1.0 - _t));
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, _lambdaBeta));
		}
	}

	private void setFixedPointsAndLambda(final HomCoords2D p1, final HomCoords2D p2, final Complex lambda) {
		try {
			MobiusTransformation mobiusTransformation = MobiusTransformation.defineByFixedPointsAndLambda(p1, p2, lambda);
			_mobiusSphere.setMobiusTransformation(mobiusTransformation);
		} catch(Exception e) {
			System.out.println("Failed to define Mobius Transformation.");
		}
	}
	
	public void setMobiusTransformation(final MobiusTransformation mobiusTransformation) {
		_mobiusSphere.setMobiusTransformation(mobiusTransformation);
	}
}
