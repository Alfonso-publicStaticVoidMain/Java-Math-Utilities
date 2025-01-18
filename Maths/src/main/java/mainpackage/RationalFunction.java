package mainpackage;

import java.util.function.Function;

public class RationalFunction {
    
    private RealPolynomial num;
    private RealPolynomial den;
    
    public RationalFunction(RealPolynomial p, RealPolynomial q) {
        this.num = p;
        this.den = q.isZero() ? new RealMonomial(1, 0) : q;
    }
    
    public RationalFunction() {
        this.num = new RealMonomial(0);
        this.den = new RealMonomial(0);
    }
    
    // a/b * c/d = (a*d + b*c) / b*d
    public RationalFunction sum(RationalFunction r) {
        RationalFunction result = new RationalFunction();
        result.num = this.num.mult(r.den).sum(this.den.mult(r.num));
        result.den = this.den.mult(r.den);
        return result;
    }
    
    // a/b * c/d = a*c / b*d
    public RationalFunction mult(RationalFunction r) {
        RationalFunction result = new RationalFunction();
        result.num = this.num.mult(r.num);
        result.den = this.den.mult(r.den);
        return result;
    }
    
    // d(u/v) = (v du - u dv) / v^2
    public RationalFunction diff() {
        RationalFunction result = new RationalFunction();
        result.num = this.den.mult(this.num.diff()).substr(this.num.mult(this.den.diff()));
        result.den = this.den.pow(2);
        return result;
    }
    
    public RationalFunction diff(int n) {
        RationalFunction result = this;
        for (int i = 0; i < n; i++) result = result.diff();
        return result;
    }
    
    public double evaluate(double d) {
        try {return this.num.evaluate(d)/this.den.evaluate(d);}
        catch (Exception e) {return Double.NaN;}
    }
    
    public RationalFunction inverse() {
        RationalFunction result = new RationalFunction();
        result.num = this.den;
        result.den = this.num;
        return result;
    }
    
    @Override
    public String toString() {
        return this.num + " / " + this.den;
    }
    
    public Function<Double, Double> toFunction() {return x -> this.evaluate(x);}
    
    public double NewtonRaphsonMethod(double x, double eps, int maxit, boolean printIterations) {
        int counter = 1;
        if (printIterations) {
            System.out.printf("Newton's Method applied to the rational function: " + this + "\nWith initial value %.2f\n", x);
            System.out.println("| it\t| x\t\t| p(x)\t\t| p'(x)\t\t|");
        }
        while (Math.abs(this.evaluate(x)) > eps && counter < maxit) {
            if (printIterations) System.out.printf("| %d\t| %.6f\t| %.6f\t| %.6f\t|\n", counter, x, this.evaluate(x), this.diff().evaluate(x));
            if (this.diff().evaluate(x) != 0) x = x - this.evaluate(x)/this.diff().evaluate(x);
            else {
                if (printIterations) System.out.println("Division by zero");
                return Double.NaN;
            }
            counter++;
        }
        return x;
    }
    
    public double HalleyMethod(double x, double eps, int maxit, boolean printIterations) {
        int counter = 1;
        if (printIterations) {
            System.out.printf("Halley's Method applied to the rational function: " + this + "\nWith initial value %.2f\n", x);
            System.out.println("| it\t| x\t\t| p(x)\t\t| p'(x)\t\t|");
        }
        while (Math.abs(this.evaluate(x)) > eps && counter < maxit) {
            if (printIterations) System.out.printf("| %d\t| %.6f\t| %.6f\t| %.6f\t|\n", counter, x, this.evaluate(x), this.diff().evaluate(x));
            if (2*Math.pow(this.diff().evaluate(x), 2) - this.evaluate(x)*this.diff(2).evaluate(x) != 0) x = x - 2*this.evaluate(x)*this.diff().evaluate(x)/(2*Math.pow(this.diff().evaluate(x), 2) - this.evaluate(x)*this.diff(2).evaluate(x));
            else {
                if (printIterations) System.out.println("Division by zero");
                return Double.NaN;
            }
            counter++;
        }
        return x;
    }
    
    public static void main(String[] args) {
        RealPolynomial p = new RealPolynomial(1, -1, 0, -1);
        RealPolynomial q = new RealPolynomial(1, -1);
        RationalFunction r = new RationalFunction(p, q);       
        
        double root = r.NewtonRaphsonMethod(3, 10e-8, 10000, true);
        System.out.println("Full value of the root found with Newton-Raphson method: " + root);
        System.out.println("-------------------------------------------------------------------");
        root = r.HalleyMethod(3, 10e-8, 10000, true);
        System.out.println("Full value of the root found with Halley's method: " + root);
    }
}
