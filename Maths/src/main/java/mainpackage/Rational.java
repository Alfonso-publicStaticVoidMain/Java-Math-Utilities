package mainpackage;

public class Rational extends RealNumber {
    
    private int num;
    private int den;
    
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

    public static Rational intToRational(int n) {return new Rational(n);}
    public boolean isInteger() {return this.getSimplified().den == 1;}
    public boolean equals(Rational r) {return this.num * r.den == this.den * r.num;}
    
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
    public Rational getSimplified() {return new Rational(this.num, this.den).simplify();}
    
    public Rational sum(Rational r) {return new Rational(this.num * r.den + this.den * r.den, this.den * r.den);}
    @Override
    public RealNumber sum(RealNumber n) {return (n instanceof Rational) ? this.sum((Rational) n) : new RealNumber(this.getValue() + n.getValue());}
    @Override
    public Rational sum(int n) {return this.sum(new Rational(n));}
    
    public Rational substr(Rational r) {return this.sum(r.invertSign());}
    @Override
    public RealNumber substr(RealNumber n) {return (n instanceof Rational) ? this.substr((Rational) n) : new RealNumber(this.getValue() - n.getValue());}
    @Override
    public Rational substr(int n) {return this.substr(new Rational(n));}
    
    
    public Rational mult(Rational r) {return new Rational(this.num * r.num, this.den * r.den);}
    @Override
    public RealNumber mult(RealNumber n) {return (n instanceof Rational) ? this.mult((Rational) n) : new RealNumber(this.getValue() * n.getValue());}
    @Override
    public Rational mult(int n) {return this.mult(new Rational(n));}
    
    public Rational div(Rational r) {return this.mult(r.inverse());}
    @Override
    public RealNumber div(RealNumber n) {
        if (n.getValue() == 0) return new RealNumber(Double.NaN);
        return (n instanceof Rational) ? this.div((Rational) n) : new RealNumber(this.getValue() / n.getValue());
    }
    @Override
    public Rational div(int n) {return this.div(new Rational(n));}
    
    @Override
    public Rational invertSign() {
        if (this.getValue() == 0) return new Rational(0);
        return new Rational(this.den > 0 ? -this.num : this.num, this.den > 0 ? this.den : -this.den);
    }
    
    @Override
    public Rational inverse() {
        if (this.getValue() == 0) {
            Rational resultado = new Rational(0);
            resultado.setValue(Double.NaN);
            return resultado;
        }
        return new Rational(this.den, this.num);
    }
    
    @Override
    public String toString() {
        if (this.isInteger()) return Integer.toString(this.getSimplified().num);
        else return this.num + "/" + this.den;
    }
    
    public static Rational parseRational(String rac) {
        rac = rac.replace(" ", "");
        try {return new Rational(Integer.parseInt(rac));}
        catch (Exception e) {return new Rational(Integer.parseInt(rac.substring(0, rac.indexOf('/'))), Integer.parseInt(rac.substring(rac.indexOf('/') + 1, rac.length())));}
    }
}
