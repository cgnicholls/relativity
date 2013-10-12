package relativity.scene.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import relativity.scene.Texture;



public class CylindricalTexture {
	private Texture _texture;
	
	public CylindricalTexture() {
		_texture = new Texture();
	}
	
	public void loadImage(final String imageName) {
		try {
			_texture.loadImage(imageName);
		} catch (Exception e) {
			System.out.println("Failed to load image.");
		}
	}
	
	public void setImage(final BufferedImage image) {
		_texture.setImage(image);
	}
	
	/*
	 * We assume v is a vector on the cylinder, where the cylinder is assume unit height, based at the origin and the top at (0, 0, 1), with radius 1.
	 */
	public Color getColourAtThetaZ(final double theta, final double z) {
		// We calculate theta and z.
		try {
			return _texture.getColorUnitCoordinates(z, theta / (2.0 * Math.PI));
		} catch (Exception e) {
			System.out.println("Failed to read texture at: (" + z + ", " + theta / (2.0 * Math.PI) + ")");
		}
		return Color.white;
	}
}
