package mainpackage;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class TestClass {

    public static void main(String[] args) {
        
        
        // Backward Euler Method testing
//        BiFunction<Double, Double, Double> f = (x, t) -> x;
//        double initValue = 0;
//        double finValue = 1;
//        double initCondition = 0;
//        int n = 100;
//        double[] x = CalcUtil.partitionInterval(initValue, finValue, n);
//        double[] y = CalcUtil.backwardEulerMethod(f, initValue, finValue, initCondition, n);
//        CalcUtil.printGraphData(x, y);


        // Integral methods testing
//        Function<Double, Double> f = x -> Math.pow(Math.E, Math.pow(x, 2)*Math.sqrt(3*x-1));
//        double lowerBound = 1, upperBound = 3;
//        int n = 10000;
//        
//        System.out.println("Expected value of the integral: 5355567536");
//        System.out.println("Midpoint rule: " + CalcUtil.midpointRule(f, lowerBound, upperBound, n));
//        System.out.println("Trapezoidal rule: " + CalcUtil.trapezoidalRule(f, lowerBound, upperBound, n));
//        System.out.println("Simpson's rule: " + CalcUtil.SimpsonRule(f, lowerBound, upperBound, n));



        //Function<Double, Double> f = x -> Math.pow(Math.E, x) - Math.pow(x, 2);
        RealPolynomial f = new RealPolynomial(3, -2.333333333333333, 0.666666666666, 0.333333333);
        double x0 = -1, x = -1.5;
        double eps = 1e-8;
        int maxit = 100;
        double root;
        root = CalcUtil.secantMethod(f, x0, x, eps, maxit, true);
        System.out.println("Root found with Secant Method: " + root);
        System.out.println("------------------------------------------------------------------------");
        root = CalcUtil.rootFinderMethod(f, CalcUtil.Newton(f), x, eps, maxit, true);
        System.out.println("Root found with Newton Method: " + root);
        System.out.println("------------------------------------------------------------------------");
        root = CalcUtil.rootFinderMethod(f, CalcUtil.Halley(f), x, eps, maxit, true);
        System.out.println("Root found with Halley Method: " + root);
//        System.out.println("------------------------------------------------------------------------");
//        root = CalcUtil.rootFinderMethod(f, CalcUtil.Householder(f), x, eps, maxit, true);
//        System.out.println("Root found with Householder Method: " + root);
        System.out.println("------------------------------------------------------------------------");
        root = CalcUtil.rootFinderMethod(f, CalcUtil.compoundHouseholder(f), x, eps, maxit, true);
        System.out.println("Root found with compound Householder Method: " + root);
        System.out.println("------------------------------------------------------------------------");
    }
}
