package relativity.scene.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import relativity.scene.Texture;
import relativity.toolkit.SphericalPolars;
import relativity.toolkit.Vector3;



public class SphericalTexture {
	
	private Texture _texture;
	
	public SphericalTexture() {
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
	
	public Color getColourAtUnitVector(final Vector3 unitVector) {
		// We calculate theta and phi.
		SphericalPolars sphericalPolars = SphericalPolars.computeSphericalPolars(unitVector);
		double theta = sphericalPolars.getTheta();
		double phi = sphericalPolars.getPhi();
		try {
			return _texture.getColorUnitCoordinates(phi / (Math.PI * 2.0), theta / Math.PI);
		} catch (Exception e) {
			System.out.println("Failed to read texture at: (" + theta / Math.PI + ", " + phi / (Math.PI * 2.0) + ")");
		}
		return Color.white;
	}
}
