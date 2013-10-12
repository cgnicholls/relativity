package relativity.toolkit;


public class HomCoords2D {
	private Complex _z1;
	private Complex _z2;
	
	public HomCoords2D(final double x1, final double y1, final double x2, final double y2) {
		_z1 = new Complex(x1, y1);
		_z2 = new Complex(x2, y2);
	}
	
	public HomCoords2D(final Complex z1, final Complex z2) {
		_z1 = z1;
		_z2 = z2;
	}
	
	public Complex getZ1() {
		return _z1;
	}
	
	public Complex getZ2() {
		return _z2;
	}
	
	/*
	 * Returns a new HomCoords2D in which |z1|^2 + |z2|^2 = 1.
	 */
	public HomCoords2D normalise() {
		double length = Math.sqrt(_z1.lengthSquared() + _z2.lengthSquared());
		return new HomCoords2D(_z1.scalarMultiply(1.0/length), _z2.scalarMultiply(1.0/length));
	}
	
	/*
	 * Two homogeneous coordinates [z1, z2], [w1, w2] are equal if and only if there is a complex scalar lambda,
	 * s.t. (z1, z2) = lambda * (w1, w2).
	 */
	public boolean equalTo(final HomCoords2D homCoords) {
		Complex w1 = homCoords.getZ1();
		Complex w2 = homCoords.getZ2();
		
		// if w1 = 0, then, if z1 = 0 we have equality, if z1 != 0, we have non-equality.
		double precision = 0.000001;
		if (w1.lengthSquared() < precision) {
			if (_z1.lengthSquared() < precision) {
				return true;
			} else {
				return false;
			}
			// Else w1 != 0, so we may write homCoords as [1, w2/w1].
		} else {
			// If z1 = 0, then we do not have equality
			if (_z1.lengthSquared() < precision) {
				return false;
			} else {
				// Else z1 != 0, so we may write this as [1, z2/z1].
				// Now compare z2/z1 and w2/w1.
				Complex z2OverZ1 = new Complex(1.0, 0.0);
				Complex w2OverW1 = new Complex(1.0, 0.0);
				try {
					z2OverZ1 = _z2.dividedBy(_z1);
					w2OverW1 = w2.dividedBy(w1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (z2OverZ1.minus(w2OverW1).lengthSquared() < precision) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public static HomCoords2D computeHomCoords(final double theta, final double phi) {
		return new HomCoords2D(Complex.polars(Math.cos(theta / 2.0), phi / 2.0), Complex.polars(Math.sin(theta / 2.0), -phi / 2.0));
	}

	/*
	 * Returns either [1, z] or [z, 1], depending on whether z1 or z2 is larger in magnitude respectively.
	 */
	public HomCoords2D bestCoordinate() throws Exception {
		double z1LengthSquared = _z1.lengthSquared();
		double z2LengthSquared = _z2.lengthSquared();
		if (z1LengthSquared > z2LengthSquared) {
			return new HomCoords2D(new Complex(1.0), _z2.dividedBy(_z1));
		} else {
			return new HomCoords2D(_z1.dividedBy(_z2), new Complex(1.0));
		}
	}
	
	public static HomCoords2D computeHomCoords(final Vector3 unitVector) {
		if (unitVector.getZ() >= 0.0) {
			return new HomCoords2D(new Complex(1.0), new Complex(unitVector.getX() / (1.0 + unitVector.getZ()), -unitVector.getY() / (1.0 + unitVector.getZ())));
		} else {
			return new HomCoords2D(new Complex(unitVector.getX() / (1.0 - unitVector.getZ()), unitVector.getY() / (1.0 - unitVector.getZ())), new Complex(1.0));
		}
	}
	
	public static Complex stereographicProjectionFromNorthPole(final Vector3 unitVector) throws Exception {
		if (Math.abs(unitVector.getZ() - 1.0) < 0.00001) {
			throw new Exception("Cannot project North Pole.");
		}
		return new Complex(unitVector.getX() / (1.0 - unitVector.getZ()), unitVector.getY() / (1.0 - unitVector.getZ()));
	}
	
	public static Complex stereographicProjectionFromSouthPole(final Vector3 unitVector) throws Exception {
		if (Math.abs(unitVector.getZ() + 1.0) < 0.00001) {
			throw new Exception("Cannot project North Pole.");
		}
		return new Complex(unitVector.getX() / (1.0 + unitVector.getZ()), -unitVector.getY() / (1.0 + unitVector.getZ()));
	}

	public boolean isUpperHemisphere() {
		return (_z1.lengthSquared() > _z2.lengthSquared());
	}

	public Complex stereographicProjectionFromNorthPole() throws Exception {
		return _z1.dividedBy(_z2);
	}

	public Complex stereographicProjectionFromSouthPole() throws Exception {
		return _z2.dividedBy(_z1);
	}

	/*
	 * Returns the unit vector associated with these coordinates.
	 */
	public Vector3 getUnitVector() {
		normalise();
		if (!isUpperHemisphere()) {
			try {
				Complex stereoNorthPole = stereographicProjectionFromNorthPole();
				double lengthSquared = stereoNorthPole.lengthSquared();
				return new Vector3(stereoNorthPole.getX() * 2.0 / (lengthSquared + 1.0), stereoNorthPole.getY() * 2.0 / (lengthSquared + 1.0), (lengthSquared - 1.0) / (lengthSquared + 1.0));
			} catch (Exception e) {
				System.out.println("Failed to stereographically project from north pole.");
			}
		} else {
			try {
				Complex stereoSouthPole = stereographicProjectionFromSouthPole();
				double lengthSquared = stereoSouthPole.lengthSquared();
				return new Vector3(stereoSouthPole.getX() * 2.0 / (lengthSquared + 1.0), -stereoSouthPole.getY() * 2.0 / (lengthSquared + 1.0), -(lengthSquared - 1.0) / (lengthSquared + 1.0));
			} catch (Exception e) {
				System.out.println("Failed to stereographically project from south pole.");
			}
		}
		return null;
	}

	/*
	 * Returns the homogeneous coordinates of the north pole of the Riemann sphere.
	 */
	public static HomCoords2D northPole() {
		return new HomCoords2D(new Complex(1.0), new Complex(0.0));
	}

	/*
	 * Returns the homogeneous coordinates of the south pole of the Riemann sphere.
	 */
	public static HomCoords2D southPole() {
		return new HomCoords2D(new Complex(0.0), new Complex(1.0));
	}

	/*
	 * Returns a point which is antipodal on the sphere, with the same norm.
	 */
	public HomCoords2D antipodal() {
		return new HomCoords2D(_z2.negate().conjugate(), _z1.conjugate());
	}
}
