package relativity.scene.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import relativity.relativisticraytracer.RelativisticRayData;
import relativity.toolkit.FourVector;
import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.Lorentz;
import relativity.toolkit.Vector3;



public class RelativisticCylinder implements RelativisticSceneObject {
	private Vector3 _u; // one end of the cylinder
	private Vector3 _v; // the other end of the cylinder
	private Vector3 _zeroTheta; // the direction of zero theta
	private double _radius;
	private Vector3 _velocity;
	
	private CylindricalTexture _cylindricalTexture;
	
	public RelativisticCylinder(final Vector3 u, final Vector3 v, final Vector3 zeroTheta, final double radius) {
		_u = u;
		_v = v;
		_zeroTheta = zeroTheta;
		_radius = radius;
		_velocity = new Vector3(0.0, 0.0, 0.0);
		
		_cylindricalTexture = new CylindricalTexture();
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
		FourVector rayDirectionAtRestWithObject = Lorentz.boost(_velocity, rayDirectionInScene);
		
		// Also calculate the transformed sphere position
		FourVector uScene = new FourVector(0.0, _u);
		FourVector vScene = new FourVector(0.0, _v);
		FourVector uTransformed = Lorentz.boost(_velocity, uScene);
		FourVector vTransformed = Lorentz.boost(_velocity, vScene);
		
		// Now calculate the nearest point of intersection
		Vector3 nearestPointOfIntersection = IntersectionFinder.nearestPointOfIntersectionCylinder(rayPositionInScene.getSpatial(), rayDirectionAtRestWithObject.getSpatial(), uTransformed.getSpatial(), vTransformed.getSpatial(), _radius);
		
		// If no point of intersection, then we just return null
		if (nearestPointOfIntersection == null) {
			return;
		}
		
		// We now transform the point of intersection to standard cylindrical coordinates.
		Vector3 pMinusU = nearestPointOfIntersection.subtract(_u);
		Vector3 vMinusU = _v.subtract(_u);
		Vector3 pProjectedToBase = pMinusU.subtract(vMinusU.scalarMultiply(pMinusU.dotProduct(vMinusU) / vMinusU.lengthSquared()));
		double cosTheta = pProjectedToBase.dotProduct(_zeroTheta) / (pProjectedToBase.lengthSquared() * _zeroTheta.lengthSquared());
		double theta = Math.acos(cosTheta);
		if ((pProjectedToBase.dotProduct(_zeroTheta)) < 0.0) theta = 2.0 * Math.PI - theta;
		double z = pMinusU.dotProduct(vMinusU) / vMinusU.lengthSquared();
		Color colour = _cylindricalTexture.getColourAtThetaZ(theta, z);
		
		// Else, we set the intersection and best objects
		rayData.setBestIntersection(new FourVector(1.0, nearestPointOfIntersection));
		rayData.setBestObject(this);
		rayData.setBestColour(colour.getRGB());
	}
	
	public void setImage(final BufferedImage image) {
		_cylindricalTexture.setImage(image);
	}
	
	public void loadImage(final String imageName) {
		_cylindricalTexture.loadImage(imageName);
	}

}
