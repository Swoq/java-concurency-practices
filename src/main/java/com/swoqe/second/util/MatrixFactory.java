package com.swoqe.second.util;

public class MatrixFactory {
    public static double[][] generateRandomMatrix(int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }
}
