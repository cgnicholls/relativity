package mobiustransformations.scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class Texture {

	private BufferedImage _image;
	private int _width;
	private int _height;
	
	public Texture() {
		_image = null;
		_width = 0;
		_height = 0;
	}
	
	public void loadImage(final String imageName) throws Exception {
		URL url = this.getClass().getResource("/resources/" + imageName);
		_image = ImageIO.read(url);
		_width = _image.getWidth();
		_height = _image.getHeight();
		System.out.println("Loaded image: " + imageName + ", width: " + _width + ", height: " + _height);
	}
	
	public void setImage(final BufferedImage image) {
		_image = image;
		_width = image.getWidth();
		_height = image.getHeight();
	}
	
	public boolean isLoaded() {
		return (_image != null);
	}
	
	/*
	 * Return the colour at (fractionX * (width-1), fractionY * (height-1))
	 * fractionX and fractionY should be in the closed interval [0, 1].
	 */
	public Color getColorUnitCoordinates(final double fractionX, final double fractionY) throws Exception {
//		System.out.println(fractionX + ", " + fractionY);
		return getColor((int) (fractionX * (_width-0.001)), (int) (fractionY * (_height-0.001)));
	}
	
	// Returns the color at pixel (x,y) in the texture
	public Color getColor(final int x, final int y) throws Exception {
		if (_image == null) {
			throw new Exception("No image in texture");
		}
		return new Color(_image.getRGB(x, y));
	}
}
