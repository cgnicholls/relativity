package mobiustransformations.relativisticraytracer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mobiustransformations.scene.RelativisticScene;
import mobiustransformations.toolkit.Vector3;


public class SphereOfSightRenderer {

	public static BufferedImage renderSphereOfSightToTexture(final Camera camera, final RelativisticScene scene, final int imageWidth, final int imageHeight) {
		BufferedImage texture = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				double phi = (double) x * Math.PI * 2.0 / (double) imageWidth;
				double theta = (double) y * Math.PI / (double) imageHeight;
				Vector3 rayDirection = new Vector3(Math.cos(phi) * Math.sin(theta), Math.sin(phi) * Math.sin(theta), Math.cos(theta));
				int rgb = RelativisticRayTracerLibrary.getColour(camera.getPosition(), rayDirection, camera.getVelocity(), scene, new Color(100, 100, 100));
				texture.setRGB(x, y, rgb);
			}
		}
		
		return texture;
	}
}
