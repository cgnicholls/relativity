package relativity.scene.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import relativity.relativisticraytracer.RelativisticRayData;
import relativity.toolkit.FourVector;
import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.Lorentz;
import relativity.toolkit.Vector3;



public class RelativisticSphere implements RelativisticSceneObject {
	private Vector3 _position;
	private double _radius;
	private Vector3 _velocity;
	private SphericalTexture _sphericalTexture;
	
	public RelativisticSphere(final Vector3 position, final double radius) {
		_position = position;
		_radius = radius;
		_velocity = new Vector3(0.0, 0.0, 0.0).scalarMultiply(Lorentz.SPEED_OF_LIGHT * 0.8);
		_sphericalTexture = new SphericalTexture();
	}

	@Override
	public Vector3 getVelocity() {
		return _velocity;
	}
	
	@Override
	public void computeRay(RelativisticRayData rayData) {
		FourVector rayDirectionInScene = rayData.getRayDirection();
		FourVector rayPositionInScene = rayData.getRayPosition();
		
		// Now transform the ray to a frame in which the sphere is at rest.
		FourVector rayDirectionAtRestWithSphere = Lorentz.boost(_velocity, rayDirectionInScene);
		
		// Also calculate the transformed sphere position
		FourVector sphereCentreFourVectorInScene = new FourVector(0.0, _position);
		FourVector transformedSphereCentre = Lorentz.boost(_velocity, sphereCentreFourVectorInScene);
		
		// Now calculate the nearest point of intersection
		Vector3 nearestPointOfIntersection = IntersectionFinder.nearestPointOfIntersection(rayPositionInScene.getSpatial(), rayDirectionAtRestWithSphere.getSpatial(), transformedSphereCentre.getSpatial(), _radius);
		
		// If no point of intersection, then we just return null
		if (nearestPointOfIntersection == null) {
			return;
		}
		
		Vector3 directionOnUnitSphere = (nearestPointOfIntersection.minus(_position)).normalise();
		
		Color colour = _sphericalTexture.getColourAtUnitVector(directionOnUnitSphere);
		
		// Else, we set the intersection and best objects
		rayData.setBestIntersection(new FourVector(1.0, nearestPointOfIntersection));
		rayData.setBestObject(this);
		rayData.setBestColour(colour.getRGB());
	}
	
	public void loadImage(final String imageName) {
		_sphericalTexture.loadImage(imageName);
	}
	
	public void setImage(final BufferedImage image) {
		_sphericalTexture.setImage(image);
	}
}
