package relativity.toolkit;

public class QuadraticEquation {
	public static Complex solvePlus(final Complex a, final Complex b, final Complex c) throws Exception {
		if (a.lengthSquared() < 0.00001) {
			if (b.lengthSquared() < 0.00001) {
				// No solution, or infinitely many.
				throw new Exception("a and b both zero");
			}
			return c.dividedBy(b);
		}
		
		Complex discriminant = b.multiply(b).minus(a.multiply(c).scalarMultiply(4.0));
		
		return b.negate().plus(discriminant.squareRootPlus()).dividedBy(a.scalarMultiply(2.0));
	}

	public static Complex solveMinus(final Complex a, final Complex b, final Complex c) throws Exception {
		if (a.lengthSquared() < 0.00001) {
			if (b.lengthSquared() < 0.00001) {
				// No solution, or infinitely many.
				throw new Exception("a and b both zero");
			}
			return c.dividedBy(b);
		}
		
		Complex discriminant = b.multiply(b).minus(a.multiply(c).scalarMultiply(4.0));
		
		return b.negate().plus(discriminant.squareRootMinus()).dividedBy(a.scalarMultiply(2.0));
	}
}
