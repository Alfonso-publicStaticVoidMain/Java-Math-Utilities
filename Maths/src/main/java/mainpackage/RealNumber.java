package mainpackage;

public class RealNumber {
    /*
    * A class to work with real numbers. Their only attribute is simply
    * a double to represent their value.
    *
    * It has a child class named Rational which also stores the value of the
    * numerator and denominator of the fraction that is representing, in order
    * to use them in calculations.
    */
    
    private double value;
    
    /*
    * Main constructors can either take an already existing real number to clone
    * it, or a double value to store it as the value of the new object.
    * The empty constructor returns NaN.
    */
    public RealNumber(RealNumber n) {this(n.value);}
    public RealNumber(double v) {this.value = v;}
    public RealNumber() {this.value = Double.NaN;}
    
    // Returns the sum of another real number or an int to this.
    public RealNumber sum(RealNumber n) {return new RealNumber(this.value + n.getValue());}
    public RealNumber sum(int n) {return this.sum(new Rational(n));}
    // Returns the substraction of another real number or an int from this.
    public RealNumber substr(RealNumber n) {return this.sum(n.invertSign());}
    public RealNumber substr(int n) {return this.sum(new Rational(-n));}
    // Returns the product of another real number or an int to this.
    public RealNumber mult(RealNumber n) {return new RealNumber(this.value * n.value);}
    public RealNumber mult(int n) {return this.mult(new Rational(n));}
    /*
    * Return the division of this by another real number or an int.
    * If trying to divide by zero, returns NaN.
    */
    public RealNumber div(RealNumber n) {return n.value == 0 ? new RealNumber() : this.mult(n.inverse());}
    public RealNumber div(int n) {return this.div(new Rational(n));}
    // Returns the real number with opposite value than this.
    public RealNumber invertSign() {return new RealNumber(-this.value);}
    // Returns the multiplicative inverse of this, or NaN if this is zero.
    public RealNumber inverse() {return this.value == 0 ? new RealNumber() : new RealNumber(1/this.value);}

    public void setValue(double v) {this.value = v;}
    public double getValue() {return this.value;}
    
    @Override
    public String toString() {return Double.toString(this.value);}
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealNumber)) return false;
        return this.value == ((RealNumber) obj).value;
    }
    
    // Returns true if this is an instance of Rational, false otherwise.
    public boolean isRational() {return this instanceof Rational;}
    
    /*
    * Tries to parse a string as a rational number, following the syntaxis p/q.
    * If unable, tries to parse it as a double and store it as the value of
    * a real number to be returned. If all this fails, returns NaN.
    * If the string introduces can be parsed as an integer, an object of
    * Rational is returned.
    */
    public static RealNumber parseRealNumber(String str) {
        str = str.replace(" ", "");
        try {return Rational.parseRational(str);}
        catch (Exception e) {
            try {return new RealNumber(Double.parseDouble(str));}
            catch (Exception f) {return new RealNumber();}
        }
    }
}