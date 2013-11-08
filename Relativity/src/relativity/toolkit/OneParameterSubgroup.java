package relativity.toolkit;

public class OneParameterSubgroup {
	private double _logMagnitude;
	private double _halfPolarAngle;
	private GL2C _matrixOfEVectors;
	private GL2C _matrixOfEVectorsInverse;
	
	public OneParameterSubgroup(final double logMagnitude, final double halfPolarAngle, final GL2C matrixOfEVectors, final GL2C matrixOfEVectorsInverse) {
		_logMagnitude = logMagnitude;
		_halfPolarAngle = halfPolarAngle;
		_matrixOfEVectors = matrixOfEVectors;
		_matrixOfEVectorsInverse = matrixOfEVectorsInverse;
	}
	
	public GL2C mobiusAtParameter(final double t) {
		double polarAngleAtT = 0.0;
//		if (_halfPolarAngle < Math.PI)
//			polarAngleAtT = 2.0 * _halfPolarAngle * t;
//		else
//			polarAngleAtT = 2.0 * _halfPolarAngle * t;
		polarAngleAtT = _halfPolarAngle * t;

		return _matrixOfEVectors.multiply(GL2C.diagonal(Complex.polars(Math.exp(_logMagnitude * t), polarAngleAtT), Complex.polars(Math.exp(-_logMagnitude * t), -polarAngleAtT))).multiply(_matrixOfEVectorsInverse);
	}

	public HomCoords2D fixedPoint1() {
		return new HomCoords2D(_matrixOfEVectors.getA(), _matrixOfEVectors.getC());
	}

	public HomCoords2D fixedPoint2() {
		return new HomCoords2D(_matrixOfEVectors.getB(), _matrixOfEVectors.getD());
	}
}
