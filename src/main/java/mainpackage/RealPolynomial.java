package mainpackage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class RealPolynomial {
    /*
    * Class I created to work with polynomials of real numbers.
    * A subclass RealMonomial was also created to more easily implement
    * certain methods that'd be easier to do with monomials than with
    * polynomials, to then use the monomial decomposition to make those
    * methods work with polynomials too.
    */
    
    /*
    * The degree of a polynomial is the highest exponent that as a nonzero
    * coefficient. It will always be equal to the size of the coefficients
    * array minus 1.
    */
    private int degree;
    /*
    * The coefficients of a polynomial will be stored on a double[] array.
    * To prevent having irrelevant zeros on the end of it, a function named
    * deleteLastZeros has been created and is called each time a polynomial is
    * constructed.
    */
    private double[] coef;
    
    /*
    * The two main constructors consist on passing each coefficient stored on
    * a double[] array, a List<Double>, or simply separated by commas.
    */
    public RealPolynomial(List<Double> coef) {
        // Old version stays commented while the newer is pending tests.
//        double[] coefArray = new double[coef.size()];
//        for (int i = 0; i < coef.size(); i++) coefArray[i] = coef.get(i);
//        coefArray = deleteLastZeros(coefArray);
//        this.coef = coefArray;
        double[] coefArray = CalcUtil.deleteExteriorIfCondition(CalcUtil.doubleListToDoubleArray(coef), x -> x == 0, false, true);
        this.coef = coefArray;
        this.degree = coefArray.length - 1;
    }
    public RealPolynomial(double... coef) {
        coef = deleteLastZeros(coef);
        this.coef = coef;
        this.degree = coef.length - 1;
    }
    
    /*
    * Returns the coefficient in the i-th position.
    * If i is negative, returns NaN.
    * If i is greater than the degree of the polynomial, always returns 0.
    */
    public double getCoefficient(int i) {
        if (i < 0) return Double.NaN;
        if (i <= this.degree) return this.coef[i];
        return 0;
        // Old version stays commented while the newer is pending tests.
//        try {return coef[i];}
//        catch (Exception e) {return Double.NaN;}
    }

    public int getDegree() {return this.degree;}
    public double[] getCoef() {return this.coef;}
    
    public boolean isZero() {return this.nonZeroIndex() == -2;}
    
    public static double[] deleteLastZeros(double[] coefArray) {
        int index = -1;
        int len = coefArray.length;
        for (int i = 0; i < len; i++) {
            if (coefArray[len - 1 - i] != 0) {
                index = i;
                break;
            }
        }
        if (index != -1) return Arrays.copyOfRange(coefArray, 0, len - index);
        else return new double[] {0d};
    }
        
    /*
    * Method used to check whether or not a polynomial can be cast to a monomial.
    * The following information can be extracted from the return of this method:
    * -2 -> All coefficients are 0, hence it's the zero polynomial, which can be cast to a trivial monomial.
    * -1 -> More than one coefficient is nonzero, hence it can't be cast to a monomial.
    * n >=0 -> Exactly one coefficient, with index n, is nonzero, so it can be cast to a monomial of degree n.
    */
    public int nonZeroIndex() {
        return CalcUtil.onlyIndexCondition(this.coef, x -> x != 0);
        // Old version stays commented while the newer is pending tests.
//        double[] coefArray = this.coef;
//        int index = -2;
//        boolean foundNonZero = false;
//        for (int i = 0; i < coefArray.length; i++) {
//            if (!foundNonZero && coefArray[i] != 0) {
//                index = i;
//                foundNonZero = true;
//            } else if (foundNonZero && index != -1 && coefArray[i] != 0) index = -1;
//        }
//        return index;
    }
    
    /*
    * Attempts to "cast" this polynomial to a monomial if able.
    * If the object can be cast to a monomial, returns the monomial object that represents it.
    * If it can't, returns the same object.
    */
    public RealPolynomial castToMonomial() {
        int monomialDegree = this.nonZeroIndex();
        switch (monomialDegree) {
            case -2 -> {return new RealMonomial(0, 0);}
            case -1 -> {return this;}
            default -> {return new RealMonomial(this.getCoefficient(monomialDegree), monomialDegree);}
        }
    }
    
    /*
    * Returns the monomial of the given degree in the monomial decomposition
    * of this polynomial.
    */
    public RealMonomial getMonomial(int deg) {
        try {return new RealMonomial(this.getCoefficient(deg), deg);}
        catch (Exception e) {return new RealMonomial(0, 0);}
    }
    
    /*
    * Returns the result of adding p to this polynomial.
    * Attempts to cast the result to a monomial if able to retain the methods of
    * that class in case the result is used in future operations.
    */
    public RealPolynomial sum(RealPolynomial p) {
        double[] resultCoef = new double[Math.max(this.degree, p.degree)+1];
        for (int i = 0; i < resultCoef.length; i++) {
            if (i < this.coef.length && i < p.coef.length) resultCoef[i] = this.coef[i] + p.coef[i];
            else if (i < this.coef.length && i >= p.coef.length) resultCoef[i] = this.coef[i];
            else if (i >= this.coef.length && i < p.coef.length) resultCoef[i] = p.coef[i];
        }
        return new RealPolynomial(resultCoef).castToMonomial();
    }
    
    // Returns the polynomial obtained when inverting the sign of all its coefficients.
    public RealPolynomial invertSign() {
        double[] resultCoef = new double[this.degree + 1];
        for (int i = 0; i <= this.degree; i++) resultCoef[i] = -this.getCoefficient(i);
        return new RealPolynomial(resultCoef).castToMonomial();
    }
    
    // Returns the result of substracting p from this polynomial.
    public RealPolynomial substr(RealPolynomial p) {return this.sum(p.invertSign());}
    
    /*
    * Returns the result of multiplying p to this polynomial.
    * To compute that, this same method is called on each monomial of the monomial
    * decomposition of this, and that result is recursively summed, because the
    * override of this method on the RealMonomial class is substantially simpler.
    */
    public RealPolynomial mult(RealPolynomial p) {
        RealPolynomial result = new RealPolynomial(0);
        for (int i = 0; i <= this.degree; i++) result = result.sum(this.getMonomial(i).mult(p));
        return result;
    }
    
    /*
    * Returns the result of taking the n-th power of this polynomial,
    * recursively calling the mult method.
    */
    public RealPolynomial pow(int n) {
        RealPolynomial result = new RealMonomial(1);
        for (int i = 0; i < n; i++) result = result.mult(this);
        return result;
    }
    
    /*
    * Returns the polynomial that represents the derivative of this with respect
    * to its variable. If the int n parameter is present, it calculates the
    * n-th derivative.
    */
    public RealPolynomial diff() {
        RealPolynomial result = new RealMonomial(0);
        for (int i = 0; i <= this.degree; i++) result = result.sum(this.getMonomial(i).diff());
        return result;
    }
    public RealPolynomial diff(int n) {
        if (n < 0) return new RealPolynomial();
        if (n == 0) return this;
        RealPolynomial result = this;
        for (int i = 0; i < n; i++) result = result.diff();
        return result;
    }
    
    /*
    * Returns the polynomial that represents the antiderivative of this polynomial
    * with respect to its variable.
    */
    public RealPolynomial antiDerivative() {
        RealPolynomial result = new RealMonomial(0);
        for (int i = 0; i <= this.degree; i++) result = result.sum(this.getMonomial(i).antiDerivative());
        return result;
    }
    
    @Override
    public String toString() {
        String str = "";
        if (this.degree == 0 && this.getCoefficient(0) == 0) return "0";
        for (int i = this.degree; i >= 0; i--) {
            String sign = "";
            double coeff = this.getCoefficient(i);
            if (i != this.degree && coeff > 0) sign = " + ";
            else if (i != this.degree && coeff < 0) sign = " - ";
            else if (i == this.degree && coeff < 0) sign = "-";
            
            if (coeff != 0) {
                str = str + sign + Math.abs(coeff);
                if (i != 0) {
                    str = str + " * x";
                    if (i != 1) str = str + "^" + i;
                }
            }
        }
        return str;
    }
    
    // Evaluates this polynomial on a double value d.
    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i <= this.degree; i++) result += this.getCoefficient(i) * Math.pow(x, i);
        return result;
    }
    
    // Returns the polynomial viewed as a real function.
    public Function<Double, Double> toFunction() {return x -> this.evaluate(x);}
    
} // Fin class RealPolynomial

class RealMonomial extends RealPolynomial {
    /* 
    * A monomial is a polynomial with only one nonzero coefficient, that being
    * the one in the position of its degree. That coefficient will be stored
    * in the mainCoef double attribute.
    */
    private double mainCoef;
    
    /*
    * The main constructor uses a monomialArray method which constructs a
    * double[] array to properly represent the monomial as a RealPolynomial object.
    */
    public RealMonomial(double mainCoef, int degree) {
        super(monomialArray(mainCoef, degree));
        this.mainCoef = mainCoef;
    }
    
    // This other constructor is used to more quickly store real numbers as monomials of degree 0.
    public RealMonomial(double number) {this(number, 0);}

    public double getMainCoef() {return this.mainCoef;}
    
    /*
    * This method creates a double[] array with all zeros except the number in
    * the n-th position, which takes the value passed as parameter.
    */
    public static double[] monomialArray(double value, int n) {
        if (value == 0) return new double[] {0};
        double[] coefArray = new double[n+1];
        for (int i = 0; i < n+1; i++) {
            if (i == n) coefArray[i] = value;
            else coefArray[i] = 0;
        }
        return coefArray;
    }
    
    /*
    * Returns the result of adding p to this monomial.
    * If p is a monomial of the same degree, the method will return a monomial
    * with the same degree and the sum of their main coefficients.
    * Otherwise, this method will return a non-monomial polynomial.
    */
    @Override
    public RealPolynomial sum(RealPolynomial p) {
        p = p.castToMonomial();
        if (p instanceof RealMonomial && p.getDegree() == this.getDegree()) return new RealMonomial(this.mainCoef + ((RealMonomial) p).mainCoef, this.getDegree());
        
        double[] resultCoef = new double[Math.max(this.getDegree(), p.getDegree())+1];
        for (int i = 0; i < resultCoef.length; i++) {
            if (i == this.getDegree()) resultCoef[i] = this.mainCoef + p.getCoefficient(i);
            else resultCoef[i] = p.getCoefficient(i);
        }
        return new RealPolynomial(resultCoef).castToMonomial();
    }
    
    /* 
    * Returns the result of multiplying p to this monomial.
    * If p is also a monomial, this method will return a monomial with the product
    * of their degrees and the product of their main coefficients.
    * Otherwise, it will return a non-monomial polynomial.
    */
    @Override
    public RealPolynomial mult(RealPolynomial p) {
        p = p.castToMonomial();
        if (p instanceof RealMonomial) return new RealMonomial(this.mainCoef * ((RealMonomial) p).mainCoef, this.getDegree() + p.getDegree());
        int monomialDegree = this.getDegree();
        double[] resultCoef = new double[p.getDegree() + monomialDegree + 1];
        
        for (int i = 0; i < resultCoef.length; i++) {
            if (i < monomialDegree) resultCoef[i] = 0;
            else resultCoef[i] = p.getCoefficient(i - monomialDegree) * this.mainCoef;
        }
        return new RealPolynomial(resultCoef);
    }
    
    /*
    * Returns the monomial that represents the derivative of this monomial
    * with respect to its variable.
    */
    @Override
    public RealPolynomial diff() {
        return this.getDegree() == 0 ? new RealMonomial(0, 0) : new RealMonomial(this.getDegree() * this.mainCoef, this.getDegree() - 1);
    }
    
    /*
    * Returns the monomial that represents the antiderivative of this monomial
    * with respect to its variable.
    */
    @Override
    public RealPolynomial antiDerivative() {
        return new RealMonomial(this.mainCoef / (this.getDegree() + 1), this.getDegree() + 1);
    }
}
