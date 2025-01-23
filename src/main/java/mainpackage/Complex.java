package mainpackage;

public class Complex {
    /*
    * Class for working with Complex numbers, using my RealNumber and Rational
    * Each complex number will have a real and imaginary part, stored in an
    * object of RealNumber. Note that they can also be Rational numbers, and
    * their properties and specific methods will be stored and preserved
    * whenever possible.
    */
    private RealNumber real;
    private RealNumber img;
    
    /*
    * The main constructor takes any combination of two real number objects and/or
    * double values to store them as real and imaginary parts of the new complex.
    * The empty constructor returns a complex number with NaN as real and img parts.
    */
    public Complex(RealNumber r, RealNumber i) {
        this.real = r;
        this.img = i;
    }
    public Complex(double r, double i) {
        this.real = new RealNumber(r);
        this.img = new RealNumber(i);
    }
    public Complex(RealNumber r, double i) {
        this.real = r;
        this.img = new RealNumber(i);
    }
    public Complex(double r, RealNumber i) {
        this.real = new RealNumber(r);
        this.img = i;
    }
    public Complex() {
        this.real = new RealNumber(Double.NaN);
        this.img = new RealNumber(Double.NaN);
    }

    // Getters and setters. re() and im() get the value of the real and img parts.
    public RealNumber getReal() {return real;}
    public void setReal(RealNumber real) {this.real = real;}
    public double re() {return this.real.getValue();}
    public RealNumber getImg() {return img;}
    public void setImg(RealNumber img) {this.img = img;}
    public double im() {return this.img.getValue();}
    
    // Returns a complex number representing a real, rational, or integer number.
    public static Complex toComplex(RealNumber r) {return new Complex(r, 0);}
    public static Complex toComplex(double d) {return new Complex(d, 0);}
    public static Complex toComplex(Rational r) {return new Complex(r, 0);}
    public static Complex toComplex(int n) {return new Complex(n, 0);}
    
    @Override
    public String toString() {
        return this.real.getValue() + " + " + this.img.getValue() + "*i";
    }
    
    // Returns the complex conjugate of this, inverting the sign of the img part.
    public Complex conjugate() {return new Complex(this.real, this.img.invertSign());}
    // Returns the complex with both the real and img part having their sign inverted.
    public Complex invertSign() {return new Complex(this.real.invertSign(), this.img.invertSign());}
    // Returns the module of this complex number.
    public RealNumber module() {return new RealNumber(Math.sqrt(Math.pow(this.real.getValue(), 2) + Math.pow(this.img.getValue(), 2)));}
    public double moduleValue() {return this.module().getValue();}
    // Returns the inverse of this.
    public Complex inverse() {return this.moduleValue() == 0 ? new Complex() : this.conjugate().div(this.moduleValue());}
    
    // Returns the sum of a complex, real, rational, or integer number to this.
    public Complex sum(Complex z) {return new Complex(this.real.sum(z.real), this.img.sum(z.img));}
    public Complex sum(RealNumber n) {return new Complex(this.real.sum(n), this.img);} //{return this.sum(toComplex(n));}
    public Complex sum(double d) {return new Complex(this.re() + d, this.im());} //{return this.sum(toComplex(d));}
    public Complex sum(int n) {return this.sum(new Rational(n));} //{return this.sum(toComplex(n));}
    // Returns the substraction of a complex, real, rational, or integer number from this.
    public Complex substr(Complex z) {return this.sum(z.invertSign());}
    public Complex substr(RealNumber n) {return new Complex(this.real.substr(n), this.img);} //{return this.substr(toComplex(n));}
    public Complex substr(double d) {return new Complex(this.re() - d, this.im());} //{return this.substr(toComplex(d));}
    public Complex substr(int n) {return this.substr(new Rational(n));} //{return this.substr(toComplex(n));}
    // Returns the product of a complex, real, rational, or integer number by this.
    public Complex mult(Complex z) {return new Complex(this.real.mult(z.real).substr(this.img.mult(z.img)), this.real.mult(z.img).sum(this.img.mult(z.real)));}
    public Complex mult(RealNumber n) {return new Complex(this.real.mult(n), this.img.mult(n));} //{return this.mult(toComplex(n));}
    public Complex mult(double d) {return new Complex(this.re()*d, this.im()*d);} //{return this.mult(toComplex(d));}
    public Complex mult(int n) {return this.mult(new Rational(n));}//{return this.mult(toComplex(n));}
    /*
    * Returns the division of this by a complex, real, rational, or integer number.
    * Attempting to divide by zero will return NaN.
    */
    public Complex div(Complex z) {return z.moduleValue() == 0 ? new Complex() : this.mult(z.inverse());}
    public Complex div(RealNumber n) {return n.getValue() == 0 ? new Complex() : new Complex(this.real.div(n), this.img.div(n));}
    public Complex div(double d) {return d == 0 ? new Complex() : new Complex(this.re()/d, this.im()/d);}
    public Complex div(int n) {return n == 0 ? new Complex() : this.div(new Rational(n));}
    
    public static double atan2(double x, double y) {
        if (x > 0) return Math.atan(y/x);
        if (x < 0 && y >= 0) return Math.atan(y/x) + Math.PI;
        if (x < 0 && y < 0) return Math.atan(y/x) - Math.PI;
        if (x == 0 && y > 0) return Math.PI/2;
        if (x == 0 && y < 0) return -Math.PI/2;
        return Double.NaN;
    }
    
    // Returns the principal value of this.
    public double getPrincipalValue() {return atan2(this.im(), this.re());}
    
}