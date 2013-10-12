package relativity.raytracing.classical;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import relativity.toolkit.Complex;
import relativity.toolkit.GL2C;
import relativity.toolkit.HomCoords2D;
import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.MobiusTransformation;
import relativity.toolkit.OneParameterSubgroup;
import relativity.toolkit.Vector3;


public class MobiusSphere implements ClassicalSceneObject {
	private Vector3 _position;
	private double _radius;
	private MobiusTransformation _mobiusTransformation;
	private BufferedImage _textureTop;
	private BufferedImage _textureBottom;
	private int _textureWidth;
	private int _gridDensity;
	private int _lineDensity;

	public MobiusSphere(final Vector3 position, final double radius, final MobiusTransformation mobiusTransformation, final int textureWidth, final int gridDensity, final int lineDensity) {
		_position = position;
		_radius = radius;
		_mobiusTransformation = mobiusTransformation;
		_textureTop = null;
		_textureBottom = null;
		_textureWidth = textureWidth;
		_gridDensity = gridDensity;
		_lineDensity = lineDensity;
		renderTexture(textureWidth, textureWidth, gridDensity, lineDensity);
	}
	
	public void setMobiusTransformation(final MobiusTransformation mobiusTransformation) {
		_mobiusTransformation = mobiusTransformation;
		renderTexture(_textureWidth, _textureWidth, _gridDensity, _lineDensity);
	}

	private void renderTexture(int width, int height, int gridPointDensity, int lineDensity) {
		_textureTop = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		_textureBottom = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		OneParameterSubgroup oneParameterSubgroup = null;
		if (_mobiusTransformation.isIdentity()) {
			drawGrid(gridPointDensity);
			return;
		} else if (_mobiusTransformation.isParabolic()) {
			drawGrid(gridPointDensity);
			return;
		}
		try {
			oneParameterSubgroup = _mobiusTransformation.oneParameterSubgroup();
		} catch (Exception e) {
			System.out.println("Mobius Transformation is not loxodromic, or elliptic.");
			return;
		}
		
		// Draw lines of motion, for each point on the grid
		for (int i = 0; i < gridPointDensity; i++) {
			for (int j = 0; j < gridPointDensity; j++) {
				double theta = (double) i * Math.PI / (double) (gridPointDensity - 1);
				double phi = (double) j * Math.PI * 2.0 / (double) (gridPointDensity - 1);
				HomCoords2D homCoordsOfGridPoint = HomCoords2D.computeHomCoords(theta, phi);
				HomCoords2D previousHomCoords = homCoordsOfGridPoint;
				Color colour = Color.cyan;
				for (int k = 1; k <= lineDensity; k++) {
					double t = (double) k / (double) lineDensity;
					
					GL2C mobiusTMatrix = oneParameterSubgroup.mobiusAtParameter(t);
					MobiusTransformation mobiusTransformationAtT = new MobiusTransformation(mobiusTMatrix);
					HomCoords2D homCoordsPrime = mobiusTransformationAtT.evaluate(homCoordsOfGridPoint);
					
					// Draw a line between the previous point and the evaluated point.
					drawLine(previousHomCoords, homCoordsPrime, _textureTop, _textureBottom, colour);
					
					double r = (double) colour.getRed();
					double g = (double) colour.getGreen();
					double b = (double) colour.getBlue();
//					colour = new Color((int) (r * (1.0 - t / 2.0)), (int) (g * (1.0 - t / 2.0)), (int) (b * (1.0 - t / 2.0)));
					
					previousHomCoords = homCoordsPrime;
				}
				
				// Draw an arrowhead:
				double t0 = 0.4;
				GL2C mobiusTMatrix = oneParameterSubgroup.mobiusAtParameter(t0);
				MobiusTransformation mobiusTransformationAtT = new MobiusTransformation(mobiusTMatrix);
				HomCoords2D homCoordsStart = mobiusTransformationAtT.evaluate(homCoordsOfGridPoint);
				double t1 = 0.5;
				mobiusTMatrix = oneParameterSubgroup.mobiusAtParameter(t1);
				mobiusTransformationAtT = new MobiusTransformation(mobiusTMatrix);
				HomCoords2D homCoordsEnd = mobiusTransformationAtT.evaluate(homCoordsOfGridPoint);
				drawArrowHead(homCoordsStart, homCoordsEnd, _textureTop, _textureBottom, Color.orange, Math.PI / 100.0);
				
				// If at the north or south poles, then just do iteration of j.
				if (i == 0 || i == gridPointDensity - 1)
					break;
			}
		}
		
		// Draw the fixed points of the transformation:
		HomCoords2D fixedPoint1 = oneParameterSubgroup.fixedPoint1();
		HomCoords2D fixedPoint2 = oneParameterSubgroup.fixedPoint2();
		drawCircle(fixedPoint1, 12, Color.green);
		drawCircle(fixedPoint2, 12, Color.green);

		drawGrid(gridPointDensity);
	}

	private void drawGrid(int gridPointDensity) {
		// Draw the grid
		for (int i = 0; i < gridPointDensity; i++) {
			for (int j = 0; j < gridPointDensity; j++) {
				double theta = (double) i * Math.PI / (double) (gridPointDensity - 1);
				double phi = (double) j * Math.PI * 2.0 / (double) (gridPointDensity - 1);
				HomCoords2D homCoordsOfGridPoint = HomCoords2D.computeHomCoords(theta, phi);
				drawCircle(homCoordsOfGridPoint, 5, Color.red);
			}
		}
	}

	private void drawCircle(HomCoords2D homCoordsOfCentre, int radius, Color colour) {
		try {
			if (homCoordsOfCentre.isUpperHemisphere()) {
				Complex z = homCoordsOfCentre.stereographicProjectionFromSouthPole();
				int[] textureCoords = mapToTextureCoords(z, _textureTop.getWidth(), _textureTop.getHeight());
				Graphics2D graphics = (Graphics2D) _textureTop.getGraphics();
				graphics.setColor(colour);
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.fillOval(textureCoords[0] - radius / 2, textureCoords[1] - radius / 2, radius, radius);
			} else {
				Complex z = homCoordsOfCentre.stereographicProjectionFromNorthPole();
				int[] textureCoords = mapToTextureCoords(z, _textureBottom.getWidth(), _textureBottom.getHeight());
				Graphics2D graphics = (Graphics2D) _textureBottom.getGraphics();
				graphics.setColor(colour);
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.fillOval(textureCoords[0] - radius / 2, textureCoords[1] - radius / 2, radius, radius);
			}
		} catch (Exception e) {
			System.out.println("Failed to draw circle.");
		}
	}
	
	/*
	 * Draws an arrowhead with the tip at 'end' and the lines level with start.
	 */
	private void drawArrowHead(HomCoords2D startHomCoords, HomCoords2D endDirectionHomCoords, BufferedImage textureTop, BufferedImage textureBottom, Color colour, double angularLength) {
		Vector3 endVDirection = endDirectionHomCoords.getUnitVector();
		Vector3 startV = startHomCoords.getUnitVector();
		
		// We first rotate startV about endV x startV by angular length
		Vector3 endV = Vector3.rotateVectorAroundAxisByAngle(startV, endVDirection.vectorProduct(startV), -angularLength);
		HomCoords2D endHomCoords = HomCoords2D.computeHomCoords(endV);
		
		// We first rotate endV around startV by +pi/2 and then -pi/2, and use these two points, along with endV as a triangle for an arrowhead.
		Vector3 leftPoint = Vector3.rotateVectorAroundAxisByAngle(endV, startV, Math.PI / 2.0);
		Vector3 rightPoint = Vector3.rotateVectorAroundAxisByAngle(endV, startV, -Math.PI / 2.0);
		
		HomCoords2D leftHomCoords = HomCoords2D.computeHomCoords(leftPoint);
		HomCoords2D rightHomCoords = HomCoords2D.computeHomCoords(rightPoint);

		drawLine(endHomCoords, leftHomCoords, textureTop, textureBottom, colour);
		drawLine(endHomCoords, rightHomCoords, textureTop, textureBottom, colour);
	}

	/*
	 * Draw a line between two points, given as homogenous coordinates.
	 */
	private void drawLine(HomCoords2D homCoords1, HomCoords2D homCoords2, BufferedImage textureTop, BufferedImage textureBottom, Color colour) {
		// We note that we speak about points in the textured image as (x, y) where (0, 0) represents the middle of the image,
		// and the coordinates are scaled so that (+-1, +-1) are the four corners of the texture.
		// Now, in textureTop, we have (x, y) representing the point [x+iy, 1]
		// In textureBottom we have (x, y) representing the point [1, x-iy].
		
		// So the first thing to decide is whether or not we have to draw to both textures.
		
		try {
			boolean homCoords1UpperHemisphere = homCoords1.isUpperHemisphere();
			boolean homCoords2UpperHemisphere = homCoords2.isUpperHemisphere();
			
			// if both points are in the top half of the sphere, then we can draw the line purely in textureTop.
			if (homCoords1UpperHemisphere && homCoords2UpperHemisphere) {
				drawLineGivenComplexCoordinates(homCoords1.stereographicProjectionFromSouthPole(), homCoords2.stereographicProjectionFromSouthPole(), textureTop, colour);
			}
			if (!homCoords1UpperHemisphere && !homCoords2UpperHemisphere) {
				drawLineGivenComplexCoordinates(homCoords1.stereographicProjectionFromNorthPole(), homCoords2.stereographicProjectionFromNorthPole(), textureBottom, colour);
			}
			
			// The harder situation is when the line crosses the top and bottom.
			// We shall assume that neither point is one of the poles.
			if (homCoords1UpperHemisphere && !homCoords2UpperHemisphere || !homCoords1UpperHemisphere && homCoords2UpperHemisphere) {
				// We first project from the north pole
				Complex homCoords1FromNorth = homCoords1.stereographicProjectionFromNorthPole();
				Complex homCoords2FromNorth = homCoords2.stereographicProjectionFromNorthPole();
				drawLineGivenComplexCoordinates(homCoords1FromNorth, homCoords2FromNorth, _textureBottom, colour);
				
				// Then we project from the south pole.
				Complex homCoords1FromSouth = homCoords1.stereographicProjectionFromSouthPole();
				Complex homCoords2FromSouth = homCoords2.stereographicProjectionFromSouthPole();
				drawLineGivenComplexCoordinates(homCoords1FromSouth, homCoords2FromSouth, _textureTop, colour);
			}
		} catch (Exception e) {
			System.out.println("Failed to draw line");
		}
	}
	
	/*
	 * Given 
	 */
	protected void drawLineGivenComplexCoordinates(final Complex z1, final Complex z2, final BufferedImage image, final Color colour) {
		double point1X = z1.getX();
		double point1Y = z1.getY();
		double point2X = z2.getX();
		double point2Y = z2.getY();
		int width = image.getWidth();
		int height = image.getHeight();
		
		// We have to convert the coordinates to image coordinates.
		// point1X, point1Y are in the range [-1, 1].
		// We have to map that into the range [0, width-1].
		int x1 = (int) ((point1X + 1.0) * (double) width / 2.0);
		int y1 = (int) ((point1Y + 1.0) * (double) width / 2.0);
		int x2 = (int) ((point2X + 1.0) * (double) height / 2.0);
		int y2 = (int) ((point2Y + 1.0) * (double) height / 2.0);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(colour);
		graphics.setStroke(new BasicStroke((float) 3.5));
		graphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void computeRay(ClassicalRayData rayData) {
		Vector3 rayPosition = rayData.getRayPosition();
		Vector3 rayDirection = rayData.getRayDirection();
		
		Vector3 nearestPointOfIntersection = IntersectionFinder.nearestPointOfIntersection(rayPosition, rayDirection, _position, _radius);
		
		if (nearestPointOfIntersection == null) {
			return;
		}
		
		Vector3 unitDirection = nearestPointOfIntersection.minus(_position).normalise();
		
		Color colour = getColourInUnitDirection(unitDirection);
		
		rayData.setBestObject(this);
		rayData.setBestIntersection(nearestPointOfIntersection);
		rayData.setBestColour(colour.getRGB());
	}
	
	private Color getColourInUnitDirection(final Vector3 unitVector) {
		try {
			if (unitVector.getZ() >= 0.0) {
				Complex z = HomCoords2D.stereographicProjectionFromSouthPole(unitVector);
				int[] textureCoords = mapToTextureCoords(z, _textureBottom.getWidth(), _textureBottom.getHeight());
				return new Color(_textureBottom.getRGB(textureCoords[0], textureCoords[1]));
			} else {
				Complex z = HomCoords2D.stereographicProjectionFromNorthPole(unitVector);
				int[] textureCoords = mapToTextureCoords(z, _textureTop.getWidth(), _textureTop.getHeight());
				return new Color(_textureTop.getRGB(textureCoords[0], textureCoords[1]));
			}
		} catch(Exception e) {
			
		}
		return Color.gray;
	}
	
	private int[] mapToTextureCoords(final Complex z, final int width, final int height) {
		int[] coords = new int[2];
		coords[0] = (int) ((z.getX() + 1.0) * (double) width / 2.0);
		coords[1] = (int) ((z.getY() + 1.0) * (double) height / 2.0);
		return coords;
	}
}
