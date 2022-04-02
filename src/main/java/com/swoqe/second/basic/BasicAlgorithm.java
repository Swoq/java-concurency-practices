package com.swoqe.second.basic;

import com.swoqe.second.DefaultMultiplicationAlgorithm;

public class BasicAlgorithm extends DefaultMultiplicationAlgorithm {

    public BasicAlgorithm(double[][] matrixA, double[][] matrixB) {
        super(matrixA, matrixB);
    }

    @Override
    public double[][] multiply() {
        double[][] matrixC = new double[matrixA.length][matrixB[0].length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                matrixC[i][j] = 0;
                for (int k = 0; k < matrixA[0].length; k++) {
                    matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return matrixC;
    }
}
