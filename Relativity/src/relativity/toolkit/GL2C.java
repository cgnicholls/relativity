package relativity.toolkit;

public class GL2C {
	private Complex _a;
	private Complex _b;
	private Complex _c;
	private Complex _d;
	
	public GL2C(final Complex a, final Complex b, final Complex c, final Complex d) {
		_a = a;
		_b = b;
		_c = c;
		_d = d;
	}
	
	public GL2C scalarMultiply(final Complex scalar) {
		return new GL2C(_a.multiply(scalar), _b.multiply(scalar), _c.multiply(scalar), _d.multiply(scalar));
	}
	
	public Complex determinant() {
		return _a.multiply(_d).minus(_b.multiply(_c));
	}
	
	public GL2C getSL2C() throws Exception {
		Complex determinant = determinant();
		if (determinant.lengthSquared() < 0.00001) {
			throw new Exception("Determinant zero");
		}
		determinant.squareRootPlus();
		return new GL2C(_a.dividedBy(determinant), _b.dividedBy(determinant), _c.dividedBy(determinant), _d.dividedBy(determinant));
	}
	
	public boolean isDiagonalisable() {
		return ((_a.plus(_d)).multiply(_a.plus(_d)).minus(determinant().scalarMultiply(4.0)).lengthSquared() > 0.00000000001);
	}
	
	public Complex eigenvalueOne() {
		try {
			Complex solvePlus = QuadraticEquation.solvePlus(new Complex(1.0), (_a.plus(_d)).negate(), determinant());
			return solvePlus;
		} catch(Exception e) {
			
		}
		return new Complex(0.0);
	}
	
	public Complex eigenvalueTwo() {
		try {
			Complex solveMinus = QuadraticEquation.solveMinus(new Complex(1.0), _a.plus(_d), determinant());
			return solveMinus;
		} catch(Exception e) {
			
		}
		return new Complex(0.0);
	}

	public GL2C inverse() throws Exception {
		Complex determinant = determinant();
		Complex determinantInverse = determinant.inverse();
		return new GL2C(_d, _b.negate(), _c.negate(), _a).scalarMultiply(determinantInverse);
	}

	public static GL2C diagonal(final Complex a, final Complex d) {
		return new GL2C(a, new Complex(0.0), new Complex(0.0), d);
	}

	public GL2C multiply(GL2C b) {
		Complex aPrime = _a.multiply(b.getA()).plus(_b.multiply(b.getC()));
		Complex bPrime = _a.multiply(b.getB()).plus(_b.multiply(b.getD()));
		Complex cPrime = _c.multiply(b.getA()).plus(_d.multiply(b.getC()));
		Complex dPrime = _c.multiply(b.getB()).plus(_d.multiply(b.getD()));
		return new GL2C(aPrime, bPrime, cPrime, dPrime);
	}

	public Complex getA() {
		return _a;
	}

	public Complex getB() {
		return _b;
	}

	public Complex getC() {
		return _c;
	}

	public Complex getD() {
		return _d;
	}

	/*
	 * Assuming we are given a correct eigenvalue, lambda, this method returns a corresponding normalised eigenvector.
	 */
	public HomCoords2D eigenvector(Complex lambda) {
		Complex z1 = _b.negate();
		Complex z2 = _a.minus(lambda);
		// For lambda:
		if ((_a.minus(lambda)).lengthSquared() < 0.00001 && _b.lengthSquared() < 0.00001) {
			// The first equation is identically zero, so use the second:
			z1 = _d.minus(lambda);
			z2 = _c.negate();
		}
		return new HomCoords2D(z1, z2).normalise();
	}

	public static GL2C identity() {
		return GL2C.diagonal(new Complex(1.0), new Complex(1.0));
	}

	public static GL2C setColumns(HomCoords2D col1,
			HomCoords2D col2) {
		return new GL2C(col1.getZ1(), col2.getZ1(), col1.getZ2(), col2.getZ2());
	}
	
	/*
	 * Returns a matrix which rotates about the axis through the given point by the angle given.
	 */
	public static GL2C rotation(final HomCoords2D axis, final double angle) {
		HomCoords2D axisNormalised = axis.normalise();
		
		GL2C axisMatrix = GL2C.setColumns(axisNormalised, axisNormalised.antipodal());
		GL2C axisMatrixInverse = GL2C.identity();
		try {
			axisMatrixInverse = axisMatrix.inverse();
		} catch (Exception e) {
			System.out.println("Failed to invert matrix.");
		}
		double cosAngleOver2 = Math.cos(angle / 2.0);
		double sinAngleOver2 = Math.sin(angle / 2.0);
		GL2C verticalRotation = GL2C.diagonal(new Complex(cosAngleOver2, sinAngleOver2), new Complex(cosAngleOver2, -sinAngleOver2));
		return axisMatrix.multiply(verticalRotation).multiply(axisMatrixInverse);
	}

	/*
	 * Returns a matrix which affects a rotation by 'angle' about 'axis' on the homogeneous coordinates of the sphere.
	 */
	public static GL2C rotation(final Quaternion q) {
		Vector3 axis = q.getImaginaryPart();
		if (axis.length() < 0.000000001)
			return GL2C.identity();
		axis = axis.normalise();
		double angle = 2.0 * Math.acos(q.getA());
		return rotation(HomCoords2D.computeHomCoords(axis), angle);
	}

	public Complex trace() {
		return _a.plus(_d);
	}
}
