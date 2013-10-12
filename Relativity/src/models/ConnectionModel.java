package models;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import mobiustransformations.DisplayPanel;
import mobiustransformations.SplitScreen;
import mobiustransformations.classicalraytracer.CameraMotion.Frame;
import mobiustransformations.classicalraytracer.CameraMotion;
import mobiustransformations.classicalraytracer.ClassicalRayTracer;
import mobiustransformations.classicalraytracer.ClassicalScene;
import mobiustransformations.classicalraytracer.ClassicalSceneObject;
import mobiustransformations.classicalraytracer.ClassicalSphere;
import mobiustransformations.classicalraytracer.MobiusSphere;
import mobiustransformations.relativisticraytracer.Camera;
import mobiustransformations.scene.RelativisticScene;
import mobiustransformations.toolkit.Complex;
import mobiustransformations.toolkit.GL2C;
import mobiustransformations.toolkit.HomCoords2D;
import mobiustransformations.toolkit.Lorentz;
import mobiustransformations.toolkit.MobiusTransformation;
import mobiustransformations.toolkit.Quaternion;
import mobiustransformations.toolkit.Vector3;
import controller.KeyboardInput;


public class ConnectionModel {
	private ClassicalScene _sceneClassical;
	private Camera _cameraClassical;
	
	private ClassicalRayTracer _classicalRayTracer;
	private MobiusSphere _mobiusSphereBoost;
	
	private MobiusSphere _mobiusSphereAll;
	
	private Camera _cameraForRotationSphere;
	private ClassicalSphere _rotationSphere;
	
	private double _lambdaAlpha;
	private HomCoords2D _fixedPoint1;
	private HomCoords2D _fixedPoint2;
	private double _t;
	
	public ConnectionModel() {
		_sceneClassical = new ClassicalScene();
		_sceneClassical.init();
		_cameraClassical = new Camera();
		
		_classicalRayTracer = new ClassicalRayTracer(1);
		
		_lambdaAlpha = 0.0;
		_fixedPoint1 = HomCoords2D.northPole();
		_fixedPoint2 = HomCoords2D.southPole();
		GL2C matrix = GL2C.diagonal(computeLambda(_lambdaAlpha, 0.0), new Complex(1.0, 0.0));
		MobiusTransformation mobiusTransformation = new MobiusTransformation(matrix);
		_mobiusSphereBoost = new MobiusSphere(new Vector3(0.0, 300.0, 0.0), 100.0, mobiusTransformation, 700, 10, 150);
		_mobiusSphereAll = new MobiusSphere(new Vector3(0.0, 300.0, 0.0), 100.0, mobiusTransformation, 700, 10, 150);
		
		_t = 0.0;
		
		_cameraForRotationSphere = new Camera();
		_rotationSphere = new ClassicalSphere(new Vector3(0.0, 300.0, 0.0), 100.0);
		_rotationSphere.loadImage("testimage.png");
	}
	
	private Complex computeLambda(double lambdaAlpha, double lambdaBeta) {
		return Complex.polars(Math.exp(lambdaAlpha / 2.0), lambdaBeta / 2.0);
	}

	public void renderMethod(final DisplayPanel displayPanel) {
		int borderWidth = 1;
		final Rectangle topLeft = SplitScreen.getTopLeftQuarter(displayPanel.getBounds(), borderWidth);
		final Rectangle bottomLeft = SplitScreen.getBottomLeftQuarter(displayPanel.getBounds(), borderWidth);
		final Rectangle rightHalf = SplitScreen.getRightHalf(displayPanel.getBounds(), borderWidth);

		// Draw top left
		ClassicalScene sceneClassicalZeroVelocity = new ClassicalScene();
		sceneClassicalZeroVelocity.addObject(_rotationSphere);
		_classicalRayTracer.render(displayPanel, topLeft, sceneClassicalZeroVelocity, _cameraForRotationSphere);

		// Draw bottom left
		sceneClassicalZeroVelocity = new ClassicalScene();
		sceneClassicalZeroVelocity.addObject(_mobiusSphereBoost);
		_classicalRayTracer.render(displayPanel, bottomLeft, sceneClassicalZeroVelocity, _cameraClassical);
		
		// Draw right half
		sceneClassicalZeroVelocity = new ClassicalScene();
		sceneClassicalZeroVelocity.addObject(_mobiusSphereAll);
		_classicalRayTracer.render(displayPanel, rightHalf, sceneClassicalZeroVelocity, _cameraClassical);
	}

	public void update(double deltaT) {

	}
	
	public MobiusTransformation getMobiusTransformation(final Quaternion initialRotation, final Vector3 boostDirection, final double rapidity) {
		// First deal with their initial rotation
		GL2C rotation = GL2C.rotation(initialRotation);
		
		// Then boost in the given direction - note that this is equal to rotation to align boost axis with z-axis,
		// followed by boost in z-direction, followed by undoing the rotation.
		GL2C zBoost = GL2C.diagonal(new Complex(Math.exp(rapidity / 2.0)), new Complex(Math.exp(-rapidity / 2.0)));
		HomCoords2D homCoordsOfBoostDirection = HomCoords2D.computeHomCoords(boostDirection);
		GL2C fixedPoints = GL2C.setColumns(homCoordsOfBoostDirection, homCoordsOfBoostDirection.antipodal());
		GL2C fixedPointsInverse = GL2C.identity();
		try {
			fixedPointsInverse = fixedPoints.inverse();
		} catch (Exception e) {
			System.out.println("Failed to invert matrix.");
		}
		GL2C boost = fixedPoints.multiply(zBoost).multiply(fixedPointsInverse);
		
		GL2C matrix = boost.multiply(rotation);
		return new MobiusTransformation(matrix);
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
		
		// Change the initial rotation
		if (keyboardInput.keyDown(KeyEvent.VK_W)) {
			Quaternion qRotation = Quaternion.rotation(new Vector3(1.0, 0.0, 0.0), rotationSpeed * deltaT);
			_rotationSphere.rotate(qRotation);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_S)) {
			Quaternion qRotation = Quaternion.rotation(new Vector3(1.0, 0.0, 0.0), -rotationSpeed * deltaT);
			_rotationSphere.rotate(qRotation);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_D)) {
			Quaternion qRotation = Quaternion.rotation(new Vector3(0.0, 0.0, 1.0), rotationSpeed * deltaT);
			_rotationSphere.rotate(qRotation);
		}
		if (keyboardInput.keyDown(KeyEvent.VK_A)) {
			Quaternion qRotation = Quaternion.rotation(new Vector3(0.0, 0.0, 1.0), -rotationSpeed * deltaT);
			_rotationSphere.rotate(qRotation);
		}
		
		// Change the boost
		if (keyboardInput.keyDown(KeyEvent.VK_K)) {
			_lambdaAlpha += moveSpeed * deltaT;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, 0.0));
		}
		if (keyboardInput.keyDown(KeyEvent.VK_J)) {
			_lambdaAlpha -= moveSpeed * deltaT;
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, 0.0));
		}
		
		if (keyboardInput.keyDown(KeyEvent.VK_SPACE)) {
			_lambdaAlpha = 0.0;
			_rotationSphere.setRotation(Quaternion.identity());
			setFixedPointsAndLambda(_fixedPoint1, _fixedPoint2, computeLambda(_lambdaAlpha, 0.0));
		}
		
		_mobiusSphereAll.setMobiusTransformation(getMobiusTransformation(_rotationSphere.getRotation(), new Vector3(0.0, 0.0, 1.0), _lambdaAlpha));
	}

	private void setFixedPointsAndLambda(final HomCoords2D p1, final HomCoords2D p2, final Complex lambda) {
		try {
			MobiusTransformation mobiusTransformation = MobiusTransformation.defineByFixedPointsAndLambda(p1, p2, lambda);
			_mobiusSphereBoost.setMobiusTransformation(mobiusTransformation);
		} catch(Exception e) {
			System.out.println("Failed to define Mobius Transformation.");
		}
	}
	
	public void setMobiusTransformation(final MobiusTransformation mobiusTransformation) {
		_mobiusSphereBoost.setMobiusTransformation(mobiusTransformation);
	}
}
