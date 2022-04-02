package com.swoqe.second;

public abstract class DefaultMultiplicationAlgorithm implements MatrixMultiplicationAlgorithm {

    protected final double[][] matrixA;
    protected final double[][] matrixB;

    public DefaultMultiplicationAlgorithm(double[][] matrixA, double[][] matrixB) {
        if (matrixA.length == 0 || matrixB.length == 0)
            throw new IllegalArgumentException("One of the matrix is empty");
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    public void print(double[][] matrix) {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) {
                System.out.printf("%10.1f", aDouble);
            }
            System.out.println();
        }
    }

    public double[][] transpose(double[][] matrix) {
        double[][] newMatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[j][i];
            }
        }
        return newMatrix;
    }

}
