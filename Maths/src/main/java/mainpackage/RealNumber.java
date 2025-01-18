package mainpackage;

public class RealNumber {
    
    private double value;
    
    
    public RealNumber(RealNumber n) {this(n.value);}
    public RealNumber(double v) {this.value = v;}
    public RealNumber() {this.value = Double.NaN;}
    
    public RealNumber sum(RealNumber n) {return new RealNumber(this.value + n.getValue());}
    //public RealNumber sum(Rational r){return new Rational();}
    public RealNumber sum(int n) {return this.sum(new Rational(n));}
    
    public RealNumber substr(RealNumber n) {return this.sum(n.invertSign());}
    //public RealNumber substr(Rational r) {return this.sum(r.invertSign());}
    public RealNumber substr(int n) {return this.sum(new Rational(-n));}
    
    public RealNumber mult(RealNumber n) {return new RealNumber(this.value * n.value);}
    //public RealNumber mult(Rational r) {return new RealNumber(this.value * r.getValue());}
    public RealNumber mult(int n) {return this.mult(new Rational(n));}
    
    public RealNumber div(RealNumber n) {return n.value == 0 ? new RealNumber(Double.NaN) : this.mult(n.inverse());}
    //public RealNumber div(Rational r) {return r.getValue() == 0 ? new RealNumber(Double.NaN) : this.mult(r.inverse());}
    public RealNumber div(int n) {return this.div(new Rational(n));}
    
    public RealNumber invertSign() {return new RealNumber(-this.value);}
    public RealNumber inverse() {return new RealNumber(this.value == 0 ? Double.NaN : 1/this.value);}

    public void setValue(double v) {this.value = v;}
    public double getValue() {return this.value;}
    
    @Override
    public String toString() {return Double.toString(this.value);}
    
    public boolean isRational() {return this instanceof Rational;}
    
    public static RealNumber parseRealNumber(String number) {
        number = number.replace(" ", "");
        try {return Rational.parseRational(number);}
        catch (Exception e) {return new RealNumber(Double.parseDouble(number));}
    }
    
    public static void main(String[] args) {
        RealNumber a = new Rational(2, 3);
        RealNumber r = new Rational(1, 2);
        
        System.out.println("a: " + (a instanceof Rational));
        System.out.println("r: " + (r instanceof Rational));
        
        System.out.println(a + " + " + r + " = " + a.sum(r) + " (" + a.sum(r).getClass().getSimpleName() + ")");
        
        System.out.println(a.invertSign() + " (" + a.invertSign().getClass().getSimpleName() + ")");
        System.out.println(a.inverse() + " (" + a.inverse().getClass().getSimpleName() + ")");
    }
}