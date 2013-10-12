package mobiustransformations.toolkit;

public class Vector3 {
	private double _x;
	private double _y;
	private double _z;
	
	public Vector3(final double x, final double y, final double z) {
		_x = x;
		_y = y;
		_z = z;
	}
	
	public double getX() {
		return _x;
	}
	
	public double getY() {
		return _y;
	}
	
	public double getZ() {
		return _z;
	}
	
	public double length() {
		return Math.sqrt(_x*_x + _y*_y + _z*_z);
	}

	public double dotProduct(final Vector3 v) {
		return _x * v.getX() + _y * v.getY() + _z * v.getZ();
	}

	public Vector3 scalarMultiply(final double scalar) {
		return new Vector3(_x * scalar, _y * scalar, _z * scalar);
	}
	
	public Vector3 plus(final Vector3 v) {
		return new Vector3(_x + v.getX(), _y + v.getY(), _z + v.getZ());
	}
	
	public Vector3 subtract(final Vector3 v) {
		return new Vector3(_x - v.getX(), _y - v.getY(), _z - v.getZ());
	}

	public Vector3 normalise() {
		double length = this.length();
		return new Vector3(_x / length, _y / length, _z / length);
	}

	public double lengthSquared() {
		return _x*_x + _y*_y + _z*_z;
	}

	public Vector3 vectorProduct(final Vector3 v) {
		return new Vector3(_y * v.getZ() - _z * v.getY(), _z * v.getX() - _x * v.getZ(), _x * v.getY() - _y * v.getX());
	}

	public Vector3 negate() {
		return new Vector3(-_x, -_y, -_z);
	}

	public Vector3 minus(final Vector3 v) {
		return new Vector3(_x - v.getX(), _y - v.getY(), _z - v.getZ());
	}

	/*
	 * Rotates the vector 'v' about the axis 'axis' by angle 'theta'.
	 */
	public static Vector3 rotateVectorAroundAxisByAngle(Vector3 v, Vector3 axis, double theta) {
		axis = axis.normalise();
		Vector3 perpAxis1 = v.vectorProduct(axis).normalise();
		Vector3 perpAxis2 = axis.vectorProduct(perpAxis1).normalise();
		
		Vector3 vParallelToAxis = axis.scalarMultiply(v.dotProduct(axis));
		
		double component1 = v.dotProduct(perpAxis1);
		double component2 = v.dotProduct(perpAxis2);
		
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		return vParallelToAxis.plus(perpAxis1.scalarMultiply(component1 * cosTheta - component2 * sinTheta).plus(perpAxis2.scalarMultiply(component1 * sinTheta + component2 * cosTheta)));
	}
}
