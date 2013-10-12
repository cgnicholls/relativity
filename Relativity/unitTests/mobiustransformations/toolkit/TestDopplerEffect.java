package mobiustransformations.toolkit;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import mobiustransformations.toolkit.DopplerEffect;
import mobiustransformations.toolkit.Lorentz;
import mobiustransformations.toolkit.Vector3;

import org.junit.Test;


public class TestDopplerEffect {
	private static final double ERROR_MARGIN = 0.000001;
	
	@Test
	public void testWavelengthRedGreenBlue() {
		float[] hsb = Color.RGBtoHSB(255, 0, 0, null);
		assertEquals(750.0, DopplerEffect.computeWavelength(hsb[0]), ERROR_MARGIN);

		hsb = Color.RGBtoHSB(0, 255, 0, null);
		assertEquals(564.9999944865704, DopplerEffect.computeWavelength(hsb[0]), ERROR_MARGIN);

		hsb = Color.RGBtoHSB(0, 0, 255, null);
		assertEquals(380.0, DopplerEffect.computeWavelength(hsb[0]), ERROR_MARGIN);
	}
	
	@Test
	public void testGreenDopplerShifted() {
		double c = Lorentz.SPEED_OF_LIGHT;
		float[] hsb = Color.RGBtoHSB(0, 255, 0, null);
		
		Vector3 rayDirection = new Vector3(0.0, 1.0, 0.0);
		Vector3 objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(c * 0.8);
		Vector3 cameraVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(c * 0.5);
		double dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		
		double originalHue = hsb[0];
		double originalWavelength = DopplerEffect.computeWavelength(originalHue);
		double shiftedWavelength = originalWavelength * dopplerFactor;
		double shiftedHue = DopplerEffect.computeHue(shiftedWavelength);
		
		// Assert that the object has been red shifted (increased wavelength)
		assertEquals(true, originalWavelength < shiftedWavelength);
		System.out.println("original hue = " + originalHue + ", shifted hue = " + shiftedHue);
	}

	@Test
	public void testDopplerFactorZeroRelativeSpeed() {
		double c = Lorentz.SPEED_OF_LIGHT;
		Vector3 rayDirection = new Vector3(0.0, 1.0, 0.0);
		Vector3 objectVelocityInScene = new Vector3(0.0, 0.0, 0.0);
		Vector3 cameraVelocityInScene = new Vector3(0.0, 0.0, 0.0);
		double dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(1.0, dopplerFactor, ERROR_MARGIN);
		
		objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.9 * c);
		cameraVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.9 * c);
		dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(1.0, dopplerFactor, ERROR_MARGIN);

		objectVelocityInScene = new Vector3(0.1, 0.9, 0.5).normalise().scalarMultiply(0.94 * c);
		cameraVelocityInScene = new Vector3(0.1, 0.9, 0.5).normalise().scalarMultiply(0.94 * c);
		dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(1.0, dopplerFactor, ERROR_MARGIN);
	}
	
	@Test
	public void testDopplerFactorStraightLineCameraZeroVelocity() {
		double c = Lorentz.SPEED_OF_LIGHT;
		Vector3 rayDirection = new Vector3(0.0, 1.0, 0.0);
		Vector3 objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.8 * c);
		Vector3 cameraVelocityInScene = new Vector3(0.0, 0.0, 0.0);
		double dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(Math.sqrt(1.0 + 0.8) / Math.sqrt(1.0 - 0.8), dopplerFactor, ERROR_MARGIN);
		
		rayDirection = new Vector3(0.0, -1.0, 0.0);
		objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.8 * c);
		cameraVelocityInScene = new Vector3(0.0, 0.0, 0.0);
		dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(Math.sqrt(1.0 - 0.8) / Math.sqrt(1.0 + 0.8), dopplerFactor, ERROR_MARGIN);
	}
	
	/*
	 * We choose beta(object) = 0.8, beta(camera) = 0.5 relative to the scene.
	 * Thus the camera sees the object move with beta: (b - b') / (1 - bb') = (0.8-0.5)/(1-0.8*0.5) = 0.5
	 */
	@Test
	public void testDopplerFactorStraightLine() {
		double c = Lorentz.SPEED_OF_LIGHT;
		Vector3 rayDirection = new Vector3(0.0, 1.0, 0.0);
		Vector3 objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.8 * c);
		Vector3 cameraVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.5 * c);
		
		double dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(Math.sqrt(1.0 + 0.5) / Math.sqrt(1.0 - 0.5), dopplerFactor, ERROR_MARGIN);
		
		rayDirection = new Vector3(0.0, -1.0, 0.0);
		objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.8 * c);
		cameraVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.5 * c);
		dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(Math.sqrt(1.0 - 0.5) / Math.sqrt(1.0 + 0.5), dopplerFactor, ERROR_MARGIN);
	}
	
	
	@Test
	public void testDopplerFactorTransverse() {
		double c = Lorentz.SPEED_OF_LIGHT;
		Vector3 rayDirection = new Vector3(1.0, 0.0, 0.0);
		Vector3 objectVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.8 * c);
		Vector3 cameraVelocityInScene = new Vector3(0.0, 1.0, 0.0).scalarMultiply(0.5 * c);
		
		double dopplerFactor = DopplerEffect.dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		assertEquals(1.0 / Lorentz.gamma((0.5 * c) * (0.5 * c)), dopplerFactor, ERROR_MARGIN);
	}
}
