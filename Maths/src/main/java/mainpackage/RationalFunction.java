package mainpackage;

import java.util.function.Function;

public class RationalFunction {
    /*
    * Rational Functions are quotients of polynomials, so their attributes will
    * be two objects of the RealPolynomial class to act as numerator and
    * denominator.
    */
    private RealPolynomial num;
    private RealPolynomial den;
    
    /*
    * The main constructor simply passes the two polynomials as arguments.
    * Attempting to pass a zero polynomial as denominator will instead put
    * the real number 1, viewed as a monomial, as denominator.
    */
    public RationalFunction(RealPolynomial p, RealPolynomial q) {
        this.num = p;
        this.den = q.isZero() ? new RealMonomial(1, 0) : q;
    }
    // The empty constructor returns NaN/NaN.
    public RationalFunction() {
        this.num = new RealMonomial(Double.NaN);
        this.den = new RealMonomial(Double.NaN);
    }
    
    // a/b * c/d = (a*d + b*c) / b*d
    public RationalFunction sum(RationalFunction r) {
        return new RationalFunction(this.num.mult(r.den).sum(this.den.mult(r.num)), this.den.mult(r.den));
        // Old version stays commented while newer one is pending tests
//        RationalFunction result = new RationalFunction();
//        result.num = this.num.mult(r.den).sum(this.den.mult(r.num));
//        result.den = this.den.mult(r.den);
//        return result;
    }
    
    // a/b * c/d = a*c / b*d
    public RationalFunction mult(RationalFunction r) {
        return new RationalFunction(this.num.mult(r.num), this.den.mult(r.den));
        // Old version stays commented while newer one is pending tests
//        RationalFunction result = new RationalFunction();
//        result.num = this.num.mult(r.num);
//        result.den = this.den.mult(r.den);
//        return result;
    }
    
    /*
    * Returns the Rational Function that represents the derivative of this,
    * following the formula:
    * d(u/v) = (v du - u dv) / v^2
    * If the int n argument is present, the n-th derivative is taken instead.
    */
    public RationalFunction diff() {
        return new RationalFunction(this.den.mult(this.num.diff()).substr(this.num.mult(this.den.diff())), this.den.pow(2));
        // Old version stays commented while newer one is pending tests
//        RationalFunction result = new RationalFunction();
//        result.num = this.den.mult(this.num.diff()).substr(this.num.mult(this.den.diff()));
//        result.den = this.den.pow(2);
//        return result;
    }
    public RationalFunction diff(int n) {
        RationalFunction result = this;
        for (int i = 0; i < n; i++) result = result.diff();
        return result;
    }
    
    // Evaluates this rational function on a double value d.
    public double evaluate(double d) {
        try {return this.num.evaluate(d)/this.den.evaluate(d);}
        catch (Exception e) {return Double.NaN;}
    }
    
    // Returns the inverse of this rational function, switching numerator and denominator.
    public RationalFunction inverse() {
        return new RationalFunction(this.den, this.num);
        // Old version stays commented while newer one is pending tests
//        RationalFunction result = new RationalFunction();
//        result.num = this.den;
//        result.den = this.num;
//        return result;
    }
    
    @Override
    public String toString() {
        return this.num + " / " + this.den;
    }
    
    // Returns a number function that represents this rational function.
    public Function<Double, Double> toFunction() {return x -> this.evaluate(x);}

}
