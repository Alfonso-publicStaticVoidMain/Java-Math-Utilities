package mainpackage;

import java.util.Arrays;
import java.util.function.Function;

public class RealMatrix {
    
    private double[][] matrix;
    private int rows;
    private int cols;
    
    public RealMatrix(){
        this.matrix = new double[0][0];
        this.rows = 0;
        this.cols = 0;
    }
    
    public RealMatrix(double[][] matrix) {
        if (isMatrix(matrix)) {
            this.matrix = matrix;
            this.rows = matrix.length;
            this.cols = matrix[0].length;
        } else {
            this.matrix = new double[0][0];
            this.rows = 0;
            this.cols = 0;
        }
    }
    
    public RealMatrix(int numRows, int numCols) {
        this.matrix = new double[numRows][numCols];
        this.rows = numRows;
        this.cols = numCols;
    }

    public double[][] getMatrix() {return this.matrix;}

    public int getRows() {return this.rows;}

    public int getCols() {return this.cols;}
    
    /*
    * Returns true if the double[][] array is a matrix,
    * ie, if all of its entries are double[] arrays
    * of the same length, false otherwise
    * Returns false if the array is empty.
    */
    public static boolean isMatrix(double[][] matrix) {
        if (matrix.length == 0) return false;
        if (matrix.length == 1) return true;
        int expectedLength = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i].length != expectedLength) return false;
        }
        return true;
    }
    
    /*
    * Returns true if the double[][] is a square matrix,
    * ie, if its a matrix and its number of columns is
    * equal to its number of rows, false otherwise.
    */
    public static boolean isSquareMatrix(double[][] matrix) {return isMatrix(matrix) && matrix.length == matrix[0].length;}
    public boolean isSquareMatrix() {return this.matrix.length == this.matrix[0].length;}
    
    /*
    * Returns the minor of this matrix obtained when skipping the specified
    * row and column passed.
    */
    public RealMatrix minor(int rowToSkip, int colToSkip) {return new RealMatrix(minor(this.matrix, rowToSkip, colToSkip));}
    public static double[][] minor(double[][] matrix, int rowToSkip, int colToSkip) {
        double[][] smallMatrix = new double[matrix.length - 1][matrix.length - 1];
        for (int row = 0, rowSmallMatrix = 0; row < matrix.length; row++) {
            for (int col = 0, colSmallMatrix = 0; col < matrix.length; col++) {
                if (rowToSkip != row && colToSkip != col) {
                    smallMatrix[rowSmallMatrix][colSmallMatrix] = matrix[row][col];
                    colSmallMatrix++;
                }
            } if (rowToSkip != row) rowSmallMatrix++;
        }
        return smallMatrix;
    }
    
    /*
    * Calculates the determinant of this matrix.
    * If it isn't squared, returns NaN.
    * Cases of size 1 and 2 are calculated directly, while higher sized matrix
    * are calculated by recursively calling the det method on their minors and
    * using the determinant expansion formula.
    */
    public double det() {
        if (!this.isSquareMatrix()) return Double.NaN;
        return det(this.matrix);
    }
    public static double det(double[][] matrix) {
        switch (matrix.length) {
            case 1 -> {return matrix[0][0];}
            case 2 -> {return matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];}
            default -> {
                double determinant = 0;
                for (int colToSkip = 0; colToSkip < matrix.length; colToSkip++) {
                    determinant += Math.pow(-1, colToSkip) * matrix[0][colToSkip] * det(minor(matrix, 0, colToSkip));
                }
                return determinant;
            }
        }
    }
    
    /*
    * Returns the adjoint matrix of this matrix, in which each element is the
    * determinant of its associated minor matrix.
    */
    public RealMatrix adjoint() {
        if (!this.isSquareMatrix()) return new RealMatrix();
        return new RealMatrix(adjoint(this.matrix));
    }
    public static double[][] adjoint(double[][] matrix) {
        if (matrix.length == 1) return matrix;
        double[][] adjointMatrix = new double[matrix.length][matrix.length];
        for (int row = 0; row < matrix.length; row++) for (int col = 0; col < matrix.length; col++) adjointMatrix[row][col] = det(minor(matrix, row, col));
        return adjointMatrix;
    }
    
    /*
    * Returns the inverse of this matrix.
    * If its determinant is zero of very close to zero (within 1e-10), it returns
    * an empty metrix instead.
    */
    public RealMatrix inverse() {
        if (Math.abs(this.det()) < 1e-10) return new RealMatrix();
        return this.adjoint().scalarMult(1/this.det());
    }
    
    // Sums A to this matrix.
    public RealMatrix sum(RealMatrix A) {
        if (!(this.rows == A.rows && this.cols == A.cols)) return new RealMatrix();
        double[][] result = new double[this.rows][this.cols];
        for (int row = 0; row < this.rows; row++) for (int col = 0; col < this.cols; col++) result[row][col] = this.matrix[row][col] + A.matrix[row][col];
        return new RealMatrix(result);
    }
    
    // Returns the matrix obtained when inverting the sign of each element of this.
    public RealMatrix invertSign() {return this.scalarMult(-1);}
    
    // Returns the matrix obtained by multying each element of this matrix by a real number.
    public RealMatrix scalarMult(double d) {
        double[][] result = new double[this.rows][this.cols];
        for (int row = 0; row < this.rows; row++) for (int col = 0; col < this.cols; col++) result[row][col] = d * this.matrix[row][col];
        return new RealMatrix(result);
    }
    
    // Returns the transpose of this matrix, switching rows with columns.
    public RealMatrix transpose() {
        double[][] result = new double[this.cols][this.rows];
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                result[col][row] = this.matrix[row][col];
            }
        }
        return new RealMatrix(result);
    }
    
    // Returns the row in the specified position as a double[] array
    public double[] getRow(int row) {
        return this.matrix[row];
//        double[] result = new double[this.cols];
//        for (int i = 0; i < this.cols; i++) result[i] = this.matrix[row][i];
//        return result;
    }
    // Returns the row in the specified position as a row matrix
    public RealMatrix getRowAsMatrix(int row) {
        return rowVector(this.getRow(row));
    }
    
    // Returns the column in the specified position as a double[] array
    public double[] getCol(int col) {
        double[] result = new double[this.rows];
        for (int i = 0; i < this.rows; i++) result[i] = this.matrix[i][col];
        return result;
    }
    // Returns the column in the specified position as column matrix
    public RealMatrix getColAsMatrix(int col) {
        return colVector(this.getCol(col));
    }
    
    /*
    * Returns the result of multiplying A to this matrix.
    * If dimensions aren't compatible, an empty matrix is returned.
    * For dimensions to be compatible, the number of columns of this must
    * be equal to the number of rows of A. If that happens, the returned matrix
    * will have dimension this.rows x A.cols.
    */
    public RealMatrix mult(RealMatrix A) {
        if (this.cols != A.rows) return new RealMatrix();
        double[][] result = new double[this.rows][A.cols];
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < A.cols; col++) {
                result[row][col] = CalcUtil.scalarProduct(this.getRow(row), A.getCol(col));
            }
        }
        return new RealMatrix(result);
    }
    
    // Passes a double[] array to a row matrix.
    public static RealMatrix rowVector(double[] vector) {
        return new RealMatrix(new double[][] {Arrays.copyOf(vector, vector.length)});
        // Old version stays commented while newer one is pending tests
//        double[][] result = new double[1][vector.length];
//        for (int i = 0; i < vector.length; i++) result[0][i] = vector[i];
//        return new RealMatrix(result);
    }
    
    // Passes a double[] array to a column matrix.
    public static RealMatrix colVector(double[] vector) {
        double[][] result = new double[vector.length][1];
        for (int i = 0; i < vector.length; i++) result[i][0] = vector[i];
        return new RealMatrix(result);
    }
    
    // Returns the result of the product y' * this * x.
    public double conjugateProduct(double[] x, double[] y) {
        return rowVector(x).mult(this).mult(colVector(y)).matrix[0][0];
    }
    // Returns the result of the product x' * this * x.
    public double conjugateProduct(double[] x) {
        return rowVector(x).mult(this).mult(colVector(x)).matrix[0][0];
    }
    
    // Returns a double[] array with the elements of the diagonal of this matrix.
    public double[] getDiagonal() {
        int diagLength = Math.min(this.rows, this.cols);
        double[] result = new double[diagLength];
        for (int i = 0; i < diagLength; i++) result[i] = this.matrix[i][i];
        return result;
    }
    
    // Returns true if this matrix is lower triangular, false otherwise.
    public boolean isLowerTriang() {
        for (int row = 1; row < this.rows; row++) {
            for (int col = 0; col < Math.min(row, this.cols); col++) {
                if (this.matrix[row][col] != 0) return false;
            }
        }
        return true;
    }
    
    // Returns true if this matrix is upper triangular, false otherwise.
    public boolean isUpperTriang() {
        for (int col = 1; col < this.rows; col++) {
            for (int row = 0; row < Math.min(col, this.cols); row++) {
                if (this.matrix[row][col] != 0) return false;
            }
        }
        return true;
    }
    
    // Returns the double[] array obtained when doing the product this * x.
    public double[] applyToVector(double[] x) {return this.mult(colVector(x)).getCol(0);}
    
    // Returns a linear function obtained by applying this matrix to each vector.
    public Function<double[], double[]> toFunction() {return x -> this.applyToVector(x);}
    
    /*
    * Prints this matrix with the following format:
    *    (a11, a12, a13, a14)
    *    (a21, a22, a23, a24)
    *    (a31, a32, a33, a34)
    *    (a41, a42, a43, a44)
    */
    public void printMatrix() {
        double[][] A = this.matrix;
        for (int row = 0; row < this.rows; row++) {
            System.out.println(CalcUtil.vectorToString(A[row]));
        }
    }
}
