package mainpackage;

import java.util.Arrays;
import java.util.stream.*;

public class PolynomialFunction implements RealFunction {
    
    // The array element at position i represent the coefficient of degree i
    private final double[] coefficients;
    private final int degree;

    public PolynomialFunction(double... coefficients) {
        int lastNonZeroIndex = -1;
        
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != 0) lastNonZeroIndex = i;
        }
        
        this.coefficients = lastNonZeroIndex == -1 ? new double[] {0} : Arrays.copyOfRange(coefficients, 0, lastNonZeroIndex);
        this.degree = this.coefficients.length+1;
    }

    @Override
    public double apply(double x) {
        return IntStream.range(0, this.coefficients.length)
            .mapToDouble(i -> i==0 ? this.coefficients[i] : this.coefficients[i] * Math.pow(x, i))
            .sum();
    }

    @Override
    public PolynomialFunction derivative() {
        double[] derivativeCoefficients = IntStream.range(0, this.coefficients.length)
            .mapToDouble(i -> i==this.coefficients.length-1 ? 0 : this.coefficients[i+1]*(i+1))
            .toArray();
        return new PolynomialFunction(derivativeCoefficients);
    }

    @Override
    public PolynomialFunction beforeInvertSign() {
        double[] newCoef = IntStream.range(0, this.coefficients.length)
            .mapToDouble(i -> i%2==0 ? this.coefficients[i] : -this.coefficients[i])
            .toArray();
        return new PolynomialFunction(newCoef);
    }

    @Override
    public PolynomialFunction thenInvertSign() {
        double[] newCoef = DoubleStream.of(this.coefficients)
            .map(a -> -a)
            .toArray();
        return new PolynomialFunction(newCoef);
    }
    
    public PolynomialFunction antiDerivative() {
        double[] newCoef = IntStream.range(0, this.coefficients.length+1)
            .mapToDouble(i -> i==0 ? 0 : this.coefficients[i-1]/i)
            .toArray();
        return new PolynomialFunction(newCoef);
    }
    
    public double integrate(double lowerBound, double upperBound) {
        PolynomialFunction antiDerivative = this.antiDerivative();
        return antiDerivative.apply(upperBound) - antiDerivative.apply(lowerBound);
    }
    
}
