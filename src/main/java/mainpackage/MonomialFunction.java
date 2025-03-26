package mainpackage;

import java.util.stream.*;

public class MonomialFunction extends PolynomialFunction {
    private double mainCoef;
    private int degree;
    
    public MonomialFunction(double mainCoef, int degree) {
        super(monomialArray(mainCoef, degree));
        this.mainCoef = mainCoef;
    }
    
    // This other constructor is used to more quickly store real numbers as monomials of degree 0.
    public MonomialFunction(double number) {this(number, 0);}
    
    @Override
    public double apply(double x) {
        if (degree==0) return mainCoef;
        return mainCoef*Math.pow(x, degree);
    }
    
    
    public static double[] monomialArray(double value, int n) {
        if (value == 0) return new double[] {0};
        return IntStream.range(0, n+1)
            .mapToDouble(i -> i == n ? value : 0)
            .toArray();
    }
    
    
    
}
