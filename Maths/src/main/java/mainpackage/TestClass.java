package mainpackage;

import java.util.Arrays;



public class TestClass {

    public static void main(String[] args) {
//        RealMatrix A = new RealMatrix(new double[][] {
//            {1, -1, 1},
//            {1, 3, -1},
//            {1, 1, 2}
//        });
//        System.out.println(A.det());
//        double[] b = new double[] {1, 2, -1};
//        System.out.println(Arrays.toString(CalcUtil.solveLinearSystem(A, b)));
//        System.out.println(Arrays.toString(A.applyToVector(CalcUtil.solveLinearSystem(A, b))));
//        System.out.println(Arrays.toString(b));
        
//        double[] b = new double[] {1, 2, -1};
//        double[] x = new double[] {0, 0, 0};
//        double eps = 1e-6;
//        int maxit = 100;
//        x = CalcUtil.quadraticGradientDescent(A, b, x, eps, maxit, true);
//        System.out.println("Minimum found with quadratic gradient descent method: " + CalcUtil.vectorToString(x));
        

//        System.out.println("A =");
//        A.printMatrix();
//        System.out.println("---------------------------------");
//        System.out.println("b = ");
//        RealMatrix.colVector(b).printMatrix();
//        System.out.println("---------------------------------");
//        System.out.println("A*b = ");
//        (A.mult(RealMatrix.colVector(b))).printMatrix();

        System.out.println(Arrays.toString(CalcUtil.partitionInterval(0, 1, 10)));

        //CalcUtil.analyticVsNumericalDerivative(new RealPolynomial(3, 2, -4, -1, 3, -1, 2), -20, 20, 0.1, 1e-2, 2, 5);
    }
    
}
