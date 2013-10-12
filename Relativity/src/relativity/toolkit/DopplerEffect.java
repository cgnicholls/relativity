package relativity.toolkit;

import java.awt.Color;


public class DopplerEffect {
	public static int dopplerShift(final int rgbObjectFrame, final Vector3 rayDirection, final Vector3 objectVelocityInScene, final Vector3 cameraVelocityInScene, final double c) {
		Color color = new Color(rgbObjectFrame);
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		double saturation = hsb[1];
		double brightness = hsb[2];
		
		double wavelengthObjectFrame = computeWavelength(hsb[0]);
		
		// Apply the Doppler effect.
		double dopplerFactor = dopplerFactor(rayDirection, objectVelocityInScene, cameraVelocityInScene, c);
		double wavelengthCameraFrame = wavelengthObjectFrame * dopplerFactor;
		
//		if (wavelengthCameraFrame > 750.0) {
//			return new Color(0, 0, 0).getRGB();
//		}
		
		double hue = computeHue(wavelengthCameraFrame);
		return Color.HSBtoRGB((float) hue, (float) saturation, (float) brightness);
	}

	protected static double computeWavelength(double hue) {
		if (hue < 0.0) {
			return 750.0;
		} else if (hue > 2.0 / 3.0) {
			return 380.0;
		}
		double t = hue / (2.0 / 3.0);
		return 750.0 - t * (750.0 - 380.0);
	}
	
	/*
	 * Wavelength of visible light is measured from 380nm to 750nm.
	 */
	protected static double computeHue(double wavelength) {
		if (wavelength < 380.0) {
			return 2.0 / 3.0;
		} else if (wavelength > 750.0) {
			return 0.0;
		} else {
			double t = (double) (750.0 - wavelength) / (750.0 - 380.0);
			return t * 2.0 / 3.0;
		}
	}
	
	public static double dopplerFactor(final Vector3 rayDirection, final Vector3 objectVelocityInScene, final Vector3 cameraVelocityInScene, final double c) {
		FourVector objectFourVelocityInScene = FourVector.fourVelocity(objectVelocityInScene);
		FourVector objectObservedFourVelocity = Lorentz.boost(cameraVelocityInScene, objectFourVelocityInScene);
		
		if (objectObservedFourVelocity.getSpatial().lengthSquared() < 0.00001)
			return 1.0;
		
		double cosThetaObserved = -rayDirection.dotProduct(objectObservedFourVelocity.getSpatial()) / (rayDirection.length() * objectObservedFourVelocity.getSpatial().length());
		double gamma = objectObservedFourVelocity.getT();
		double beta = Math.sqrt(1.0 - 1.0 / (gamma * gamma));
		return 1.0 / (gamma * (1.0 + beta * cosThetaObserved));
	}
}
