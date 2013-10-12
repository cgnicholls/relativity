package mobiustransformations.classicalraytracer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mobiustransformations.scene.objects.SphericalTexture;
import mobiustransformations.toolkit.IntersectionFinder;
import mobiustransformations.toolkit.Quaternion;
import mobiustransformations.toolkit.Vector3;


public class ClassicalSphere implements ClassicalSceneObject {
	private Vector3 _position;
	private double _radius;
	private SphericalTexture _sphericalTexture;
	private Quaternion _rotation;
	
	public ClassicalSphere(final Vector3 position, final double radius) {
		_position = position;
		_radius = radius;
		_sphericalTexture = new SphericalTexture();
		_rotation = Quaternion.identity();
	}

	@Override
	public void computeRay(ClassicalRayData rayData) {
		// Now calculate the nearest point of intersection
		Vector3 nearestPointOfIntersection = IntersectionFinder.nearestPointOfIntersection(rayData.getRayPosition(), rayData.getRayDirection(), _position, _radius);
		
		// If no point of intersection, then we just return null
		if (nearestPointOfIntersection == null) {
			return;
		}
		
		Vector3 directionOnUnitSphere = (nearestPointOfIntersection.minus(_position)).normalise();
		
		Vector3 directionOnSphereAfterRotation = Quaternion.applyRotation(directionOnUnitSphere, _rotation.inverse());
		
		Color colour = _sphericalTexture.getColourAtUnitVector(directionOnSphereAfterRotation);
		
		// Else, we set the intersection and best objects
		rayData.setBestIntersection(nearestPointOfIntersection);
		rayData.setBestObject(this);
		rayData.setBestColour(colour.getRGB());
	}
	
	/*
	 * Rotate the sphere by a quaternion.
	 */
	public void rotate(final Quaternion qRotation) {
		_rotation = _rotation.preMultiply(qRotation);
	}
	
	public Quaternion getRotation() {
		return _rotation;
	}
	
	public void loadImage(final String imageName) {
		_sphericalTexture.loadImage(imageName);
	}
	
	public void setImage(final BufferedImage image) {
		_sphericalTexture.setImage(image);
	}

	public void setRotation(Quaternion rotation) {
		_rotation = rotation;
	}
}
