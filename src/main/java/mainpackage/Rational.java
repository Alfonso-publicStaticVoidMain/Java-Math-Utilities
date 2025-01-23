package mainpackage;

public class Rational extends RealNumber {
    /*
    * Class to work with Rational numbers, that extends my RealNumber class
    * by also adding two integer attributes to store the numerator and
    * denominator of the fraction that each object of this class will be
    * representing. The value of it when viewed as a RealNumber will simply
    * be the division of the numerator by the denominator.
    */
    private int num;
    private int den;
    
    /*
    * Main constructtor can either take one integer to represent it as a rational
    * number with denominator 1, or two integers to store them as numerator and
    * denominator of the new rational number. If a zero denominator is passed,
    * it will be stored, but the value of the real number will be set to NaN.
    * The empty constructor returns 0/0, with a value of NaN.
    */
    public Rational(int n) {this(n, 1);}
    public Rational(int n, int d) {
        super(d != 0 ? ((double) n)/d : Double.NaN);
        this.num = n;
        this.den = d;
    }
    public Rational() {
        super(Double.NaN);
        this.num = 0;
        this.den = 0;
    }

    // Returns a rational number that represents an integer.
    public static Rational intToRational(int n) {return new Rational(n);}
    
    // Returns true if this can be simplified to an integer, false otherwise.
    public boolean isInteger() {return this.getSimplified().den == 1;}
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rational)) return false;
        return this.num * ((Rational) obj).den == this.den * ((Rational) obj).num;
    }
    
    /*
    * Simplifies the fraction expression of this, modifying the object itself,
    * and then returning it as output.
    * It also standarizes the signs of the numerator and denominator, ensuring
    * that if the value of the RealNumber itself is negative, only the numerator
    * will be negative, and not the denominator.
    */
    public Rational simplify() {
        if (this.getValue() == 0) {
            this.num = 0;
            this.den = 1;
            return this;
        }

        if (this.den < 0) {
            this.num = -this.num;
            this.den = -this.den;
        }
        
        for (int i = 2; i <= Math.max(2, Math.min(Math.abs(this.num), this.den)/2); i++) {
            while (this.num % i == 0 && this.den % i == 0) {
                this.num = this.num / i;
                this.den = this.den / i;
            }
        }
        return this;
    }
    /*
    * Returns the value that this would have if simplified, without modifying
    * this object.
    */
    public Rational getSimplified() {return new Rational(this.num, this.den).simplify();}
    
    // Returns the sum of a rational, real, or integer number to this.
    public Rational sum(Rational r) {return new Rational(this.num * r.den + this.den * r.den, this.den * r.den);}
    @Override
    public RealNumber sum(RealNumber n) {return (n instanceof Rational) ? this.sum((Rational) n) : new RealNumber(this.getValue() + n.getValue());}
    @Override
    public Rational sum(int n) {return this.sum(new Rational(n));}
    
    // Returns the substraction of a rational, real, or integer number from this.
    public Rational substr(Rational r) {return this.sum(r.invertSign());}
    @Override
    public RealNumber substr(RealNumber n) {return (n instanceof Rational) ? this.substr((Rational) n) : new RealNumber(this.getValue() - n.getValue());}
    @Override
    public Rational substr(int n) {return this.substr(new Rational(n));}
    
    // Returns the product of a rational, real, or integer number by this.
    public Rational mult(Rational r) {return new Rational(this.num * r.num, this.den * r.den);}
    @Override
    public RealNumber mult(RealNumber n) {return (n instanceof Rational) ? this.mult((Rational) n) : new RealNumber(this.getValue() * n.getValue());}
    @Override
    public Rational mult(int n) {return this.mult(new Rational(n));}
    
    /*
    * Returns the division of this by a rational, real, or integer number.
    * Attempting to divide by zero will return NaN.
    */
    public Rational div(Rational r) {return this.mult(r.inverse());}
    @Override
    public RealNumber div(RealNumber n) {
        if (n.getValue() == 0) return new RealNumber(Double.NaN);
        return (n instanceof Rational) ? this.div((Rational) n) : new RealNumber(this.getValue() / n.getValue());
    }
    @Override
    public Rational div(int n) {return this.div(new Rational(n));}
    
    /*
    * Returns the rational number obtained when inverting the sign of this.
    * Consistency of the sign convention is kept so that if the number is negative,
    * the numerator will always be the part of the fraction shown as negative.
    */
    @Override
    public Rational invertSign() {
        if (this.getValue() == 0) return new Rational(0);
        return new Rational(this.den > 0 ? -this.num : this.num, this.den > 0 ? this.den : -this.den);
    }
    
    /*
    * Returns the multiplicative inverse of this, calculated by simply switching
    * the numerator and denominator. If numerator is zero, NaN is returned.
    */
    @Override
    public Rational inverse() {return new Rational(this.den, this.num);}
    
    @Override
    public String toString() {
        if (this.isInteger()) return Integer.toString(this.getSimplified().num);
        else return this.num + "/" + this.den;
    }
    
    /*
    * Tries to parse a string to a rational number, following the notation: p/q.
    * If the string can be parsed to an integer, the rational number representing
    * that integer is returned.
    */
    public static Rational parseRational(String rac) {
        rac = rac.replace(" ", "");
        try {return new Rational(Integer.parseInt(rac));}
        catch (Exception e) {return new Rational(Integer.parseInt(rac.substring(0, rac.indexOf('/'))), Integer.parseInt(rac.substring(rac.indexOf('/') + 1, rac.length())));}
    }
}
