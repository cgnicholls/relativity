package mobiustransformations.toolkit;

public class MobiusTransformation {
	private GL2C _matrix;
	
	public MobiusTransformation(final GL2C matrix) {
		_matrix = matrix;
	}
	
	public OneParameterSubgroup oneParameterSubgroup() throws Exception {
		if (!_matrix.isDiagonalisable())
			throw new Exception("Matrix is not diagonalisable");
		GL2C sl2c = _matrix.getSL2C();
		Complex lambda = sl2c.eigenvalueOne();
		
		double logMagnitude = Math.log(lambda.length());
		double polarAngle = lambda.polarAngle();
		
		// The equations for the eigenvector (x, y) are:
		// (a-lambda)x + by = 0
		// cx + (d-lambda)y = 0
		// Now, one of the equations may be identically zero. This happens, e.g. if the matrix is already diagonal.
		// We have to choose the equation which is not identically zero.
		// This has solution: (-b, a-lambda)
		// and since the other eigenvalue is 1/lambda (as the matrix has determinant 1), then the other solution is:
		// (-b, a-1/lambda).
		// Thus the matrix of eigenvectors is:
		// [     -b   d-1/lambda]
		// [ a-lambda     -c    ]

		// We let the eigenvectors be (x,y) and (z,w) respectively.
		HomCoords2D eVector1 = sl2c.eigenvector(lambda);
		HomCoords2D eVector2 = sl2c.eigenvector(lambda.inverse());
		
		GL2C matrixOfEVectors = new GL2C(eVector1.getZ1(), eVector2.getZ1(), eVector1.getZ2(), eVector2.getZ2());
		GL2C matrixOfEVectorsInverse = matrixOfEVectors.inverse();
		
		return new OneParameterSubgroup(logMagnitude, polarAngle, matrixOfEVectors, matrixOfEVectorsInverse);
	}

	public HomCoords2D evaluate(final HomCoords2D point) {
		Complex z1Prime = _matrix.getA().multiply(point.getZ1()).plus(_matrix.getB().multiply(point.getZ2()));
		Complex z2Prime = _matrix.getC().multiply(point.getZ1()).plus(_matrix.getD().multiply(point.getZ2()));
		return new HomCoords2D(z1Prime, z2Prime).normalise();
	}

	public static MobiusTransformation defineByFixedPointsAndLambda(HomCoords2D fixedPoint1, HomCoords2D fixedPoint2, Complex lambda) throws Exception {
		GL2C matrixOfEVectors = GL2C.setColumns(fixedPoint1, fixedPoint2);
		GL2C matrixOfEValues = GL2C.diagonal(lambda, lambda.inverse());
		GL2C matrixOfEVectorsInverse = matrixOfEVectors.inverse();
		
		GL2C matrix = matrixOfEVectors.multiply(matrixOfEValues).multiply(matrixOfEVectorsInverse);
		return new MobiusTransformation(matrix);
	}

	/*
	 * Returns whether the mobius transformation is in fact the identity transformation.
	 */
	public boolean isIdentity() {
		double margin = 0.00000001;
		if (_matrix.getB().lengthSquared() > margin || _matrix.getC().lengthSquared() > margin)
			return false;
		if (_matrix.getA().minus(_matrix.getD()).lengthSquared() < margin) {
			return true;
		}
		return false;
	}
	
	/*
	 * Returns whether the mobius transformation is parabolic.
	 */
	public boolean isParabolic() {
		// Consider the squared trace.
		double margin = 0.000001;
		Complex determinant = _matrix.determinant();
		Complex traceSquared = _matrix.trace().multiply(_matrix.trace());
		// The characteristic equation has repeated roots if and only if the squared trace equals four times the determinant.
		if (traceSquared.minus(determinant.scalarMultiply(4.0)).lengthSquared() < margin)
			return true;
		return false;
	}
}
