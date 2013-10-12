package relativity.toolkit;

/*
 * z = _x + i * _y
 */
public class Complex {
	private double _x;
	private double _y;
	
	public Complex(final double realPart) {
		_x = realPart;
		_y = 0.0;
	}
	
	public Complex(final double x, final double y) {
		_x = x;
		_y = y;
	}
	
	public double getX() {
		return _x;
	}
	
	public double getY() {
		return _y;
	}
	
	public double lengthSquared() {
		return _x * _x + _y * _y;
	}
	
	/*
	 * Return a new Complex with this + z
	 */
	public Complex plus(final Complex z) {
		return new Complex(_x + z.getX(), _y + z.getY());
	}
	
	/*
	 * Return a new Complex with this * scalar
	 */
	public Complex scalarMultiply(final double scalar) {
		return new Complex(_x * scalar, _y * scalar);
	}
	
	/*
	 * Multiply this by z, and return a new Complex with the result
	 */
	public Complex multiply(final Complex z) {
		return new Complex(_x * z.getX() - _y * z.getY(), _y * z.getX() + _x * z.getY());
	}
	
	/*
	 * Return this divided by the complex number z.
	 */
	public Complex dividedBy(final Complex z) throws Exception {
		double lengthZSquared = z.lengthSquared();
		if (lengthZSquared == 0.0) {
			throw new Exception("Division by zero.");
		}
		return new Complex((_x * z.getX() + _y * z.getY())/lengthZSquared, (_y * z.getX() - _x * z.getY())/lengthZSquared);
	}

	/*
	 * Return a new Complex equal to -x -iy
	 */
	public Complex negate() {
		return new Complex(-_x, -_y);
	}

	/*
	 * Returns this minus z.
	 */
	public Complex minus(Complex z) {
		return new Complex(_x - z.getX(), _y - z.getY());
	}

	public double length() {
		return Math.sqrt(_x*_x + _y*_y);
	}

	/*
	 * Returns the polar angle in the range -pi to pi.
	 */
	public double polarAngle() {
		double atan2 = Math.atan2(_y, _x);
		if (_y < 0.0)
			atan2 += Math.PI * 2.0;
		return atan2;
	}

	public Complex squareRootPlus() {
		double polarAngle = polarAngle();
		double sqrtLength = Math.sqrt(length());
		
		return new Complex(sqrtLength * Math.cos(polarAngle / 2.0), sqrtLength * Math.sin(polarAngle / 2.0));
	}

	public Complex squareRootMinus() {
		return squareRootPlus().negate();
	}

	public Complex inverse() throws Exception {
		double lengthSquared = lengthSquared();
		if (lengthSquared < 0.000001)
			throw new Exception();
		return new Complex(_x / lengthSquared, -_y / lengthSquared);
	}

	public static Complex polars(final double r, final double theta) {
		return new Complex(r * Math.cos(theta), r * Math.sin(theta));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(_x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(_y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		if (Double.doubleToLongBits(_x) != Double.doubleToLongBits(other._x))
			return false;
		if (Double.doubleToLongBits(_y) != Double.doubleToLongBits(other._y))
			return false;
		return true;
	}

	/*
	 * Returns x-iy.
	 */
	public Complex conjugate() {
		return new Complex(_x, -_y);
	}
}
