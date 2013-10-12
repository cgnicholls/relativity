package mobiustransformations.classicalraytracer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mobiustransformations.scene.objects.CylindricalTexture;
import mobiustransformations.toolkit.IntersectionFinder;
import mobiustransformations.toolkit.Vector3;


public class ClassicalCylinder implements ClassicalSceneObject {
	private Vector3 _u; // one end of the cylinder
	private Vector3 _v; // the other end of the cylinder
	private Vector3 _zeroTheta; // the direction of zero theta
	private double _radius;
	
	private CylindricalTexture _cylindricalTexture;
	
	public ClassicalCylinder(final Vector3 u, final Vector3 v, final Vector3 zeroTheta, final double radius) {
		_u = u;
		_v = v;
		_zeroTheta = zeroTheta;
		_radius = radius;
		
		_cylindricalTexture = new CylindricalTexture();
	}
	
	@Override
	public void computeRay(ClassicalRayData rayData) {
		// Now calculate the nearest point of intersection
		Vector3 nearestPointOfIntersection = IntersectionFinder.nearestPointOfIntersectionCylinder(rayData.getRayPosition(), rayData.getRayDirection(), _u, _v, _radius);
		
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
		rayData.setBestIntersection(nearestPointOfIntersection);
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
