package relativity.toolkit;

public class Quaternion {

	private double _a;
	private double _b;
	private double _c;
	private double _d;
	
	public Quaternion(final double a, final double b, final double c, final double d) {
		_a = a;
		_b = b;
		_c = c;
		_d = d;
	}
	
	public Quaternion(final double a, final Vector3 imaginaryPart) {
		_a = a;
		_b = imaginaryPart.getX();
		_c = imaginaryPart.getY();
		_d = imaginaryPart.getZ();
	}
	
	public Quaternion plus(final Quaternion q) {
		return new Quaternion(_a + q.getA(), _b + q.getB(), _c + q.getC(), _d + q.getD());
	}
	
	public Quaternion multiply(final Quaternion q) {
		double a = _a*q.getA()-_b*q.getB()-_c*q.getC()-_d*q.getD();
		double b = _a*q.getB()+_b*q.getA()+_c*q.getD()-_d*q.getC();
		double c = _a*q.getC()-_b*q.getD()+_c*q.getA()+_d*q.getB();
		double d = _a*q.getD()+_b*q.getC()-_c*q.getB()+_d*q.getA();
		return new Quaternion(a, b, c, d);
	}
	
	public Quaternion inverse() {
		return new Quaternion(_a, -_b, -_c, -_d);
	}
	
	/*
	 * Assuming the axis is normalised, returns the quaternion: cos(angle/2) + u1 i + u2 j + u3 k, where axis = (u1, u2, u3).
	 */
	public static Quaternion rotation(final Vector3 axis, final double angle) {
		return new Quaternion(Math.cos(angle / 2.0), axis.scalarMultiply(Math.sin(angle / 2.0)));
	}
	
	/*
	 * Creates the quaternion v1 i + v2 j + v3 k.
	 */
	public static Quaternion vectorAsQuaternion(final Vector3 v) {
		return new Quaternion(0.0, v);
	}
	
	/*
	 * Returns q * this * q^(-1). That is, the conjugate by q.
	 */
	public Quaternion conjugate(final Quaternion q) {
		return (q.multiply(this)).multiply(q.inverse());
	}
	
	/*
	 * Returns -a -bi -cj -dk.
	 */
	public Quaternion negate() {
		return new Quaternion(-_a, -_b, -_c, -_d);
	}
	
	/*
	 * Rotate the given vector about the axis by the given angle. In fact, this rotates around the axis clockwise.
	 */
	public static Vector3 rotateVectorAroundAxisByAngle(final Vector3 v, final Vector3 axis, final double angle) {
		double length = v.length();
		Vector3 vToRotate = v.normalise();
		Quaternion q = rotation(axis, angle);
		Quaternion vQuat = vectorAsQuaternion(vToRotate);
		Quaternion conjugatedQuat = vQuat.conjugate(q);
		return conjugatedQuat.getImaginaryPart().normalise().scalarMultiply(length);
	}
	
	public Vector3 applyRotation(final Vector3 vToRotate) {
		double length = vToRotate.length();
		Quaternion vQuat = vectorAsQuaternion(vToRotate);
		Quaternion conjugatedQuat = vQuat.conjugate(this);
		return conjugatedQuat.getImaginaryPart().normalise().scalarMultiply(length);
	}
	
	public double getA() {
		return _a;
	}
	
	public double getB() {
		return _b;
	}
	
	public double getC() {
		return _c;
	}
	
	public double getD() {
		return _d;
	}
	
	public Vector3 getImaginaryPart() {
		return new Vector3(_b, _c, _d);
	}
	
	public Quaternion normalise() {
		double norm = Math.sqrt(_a*_a+_b*_b+_c*_c+_d*_d);
		return new Quaternion(_a / norm, _b / norm, _c / norm, _d / norm);
	}

	public static Quaternion identity() {
		return new Quaternion(1.0, 0.0, 0.0, 0.0);
	}
	
	public static Vector3 applyRotation(Vector3 vToRotate, Quaternion q) {
		return q.applyRotation(vToRotate);
	}

	public Quaternion preMultiply(Quaternion qRotation) {
		return qRotation.multiply(this);
	}
}
