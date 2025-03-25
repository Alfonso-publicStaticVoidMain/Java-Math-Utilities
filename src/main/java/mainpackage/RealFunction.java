package mainpackage;

import java.util.stream.IntStream;

@FunctionalInterface
public interface RealFunction {
    double apply(double x);
    
    static final double[][][] finiteDifferenceCoef = new double[][][] {
        { // Derivative of oder 0 (the same function)
            {1}
        },
        { // Derivate of order 1
            {-1d/2, 0, 1d/2},                                                   // accuracy 2
            {1d/12, -2d/3, 0, 2d/3, -1d/12},                                    // accuracy 4
            {-1d/60, 3d/20, -3d/4, 0, 3d/4, -3d/20, 1d/60},                     // accuracy 6
            {1d/280, -4d/105, 1d/5, -4d/5, 0, 4d/5, -1d/5, 4d/105, -1d/280}     // accuracy 8
        },
        { // Derivative of order 2
            {1, -2, 1},
            {-1d/12, 4d/3, -5d/2, 4d/3, -1d/12},
            {1d/90, -3d/20, 3d/2, -49d/18, 3d/2, -3d/20, 1d/90},
            {-1d/560, 8d/315, -1d/5, 8d/5, -205d/72, 8d/5, -1d/5, 8d/315, -1d/560}
        },
        { // Derivative of order 3
            {-1d/2, 1, 0, -1, 1d/2},
            {1d/8, -1, 13d/8, 0, -13d/8, 1, -1d/8},
            {-7d/240, 3d/10, -169d/120, 61d/30, 0, -61d/30, 169d/120, -3d/10, 7d/240}
        },
        { // Derivative of order 4
            {1, -4, 6, -4, 1},
            {-1d/6, 2, -13d/2, 28d/3, -13d/2, 2, -1d/6},
            {7d/240, -2d/5, 169d/60, -122d/15, 91d/8, -122d/15, 169d/60, -2d/5, 7d/240}
        },
        { // Derivative of order 5
            {-1d/2, 2, -5d/2, 0, 5d/2, -2, 1d/2},
            {1d/6, -3d/2, 13d/3, -29d/6, 0, 29d/6, -13d/3, 3d/2, -1d/6},
            {-13d/288, 19d/36, -87d/32, 13d/2, -323d/48, 0, 323d/48, -13d/2, 87d/32, -19d/36, 13d/288}
        },
        { // Derivative of order 6
            {1, -6, 15, -20, 15, -6, 1},
            {-1d/4, 3, -13, 29, -75d/2, 29, -13, 3, -1d/4},
            {13d/240, -19d/24, 87d/16, -39d/2, 323d/8, -1023d/20, 323d/8, -39d/2, 87d/16, -19d/24, 13d/240}
        }
    };
    
    public static RealFunction identity() {return x -> x;}    
    public static RealFunction invertSign() {return x -> -x;}
    public static RealFunction multInverse() {return x -> 1/x;}    
    public static RealFunction mult(RealFunction f, RealFunction g) {return x -> f.apply(x)*g.apply(x);}   
    public static RealFunction sum(RealFunction f, RealFunction g) {return x -> f.apply(x) + g.apply(x);}
    
    default RealFunction andThen(RealFunction f) {return x -> f.apply(this.apply(x));}    
    default RealFunction before(RealFunction f) {return x -> this.apply(f.apply(x));}   
    default RealFunction thenInvertSign() {return x -> -this.apply(x);}
    default RealFunction beforeInvertSign() {return x -> this.apply(-x);}    
    default RealFunction thenInverse() {return x -> 1/this.apply(x);}    
    default RealFunction beforeInverse() {return x -> this.apply(1/x);}
    
    /*
    Basic derivative approximation:
    f'(x) = (f(x+h)-f(x-h)) / 2h
    */
    default RealFunction derivative(double h) {
        return x -> (this.apply(x+h) - this.apply(x-h)) / 2*h;
    }
    
    default RealFunction derivative() {
        return this.derivative(1e-3);
    }
    
    default RealFunction nthDerivative(int order, double h) {
        RealFunction result = this;
        for (int i = 0; i < order; i++) result = result.derivative(h);
        return result;
    }
    
    default RealFunction nthDerivative(int order) {
        RealFunction result = this;
        for (int i = 0; i < order; i++) result = result.derivative();
        return result;
    }
    
    /*
    Tangent line t at point d equation:
    t(x) = f'(x)(x-d)+f(d)
    */
    default PolynomialFunction tangentLineAt(double d, double h) {
        return new PolynomialFunction(this.apply(d)-d*this.derivative(h).apply(d), this.derivative(h).apply(d));
    }
    
    default PolynomialFunction tangentLineAt(double d) {
        return new PolynomialFunction(this.apply(d)-d*this.derivative().apply(d), this.derivative().apply(d));
    }
    
    default PolynomialFunction TaylorPolynomial(int degree, double d) {
        double[] TaylorCoefficients = new double[]{};//IntStream.rangeClosed(0, degree)
//                .mapToDouble(i -> i==0 ? this.apply(d) : this.nthDerivative(i) * Math.pow(x-d, i)/factorial(i))
//                .toArray();
        // TO DO
        return new PolynomialFunction(TaylorCoefficients);
    }
    
    public static int factorial(int n) {
        if (n == 0) return 1;
        return n*factorial(n-1);
    }
}
