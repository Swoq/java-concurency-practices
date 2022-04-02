package com.swoqe.second.fox;

public class FoxAlgorithmThread extends Thread {
    private final int stepI;
    private final int stepJ;
    private final double[][] A;
    private final double[][] B;
    private final double[][] C;

    public FoxAlgorithmThread(double[][] A, double[][] B, double[][] C, int stepI, int stepJ) {
        this.A = A;
        this.B = B;
        this.C = C;

        this.stepI = stepI;
        this.stepJ = stepJ;
    }

    @Override
    public void run() {
        double[][] block = multiplyBlock();

        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                C[i + stepI][j + stepJ] += block[i][j];
            }
        }
    }

    private double[][] multiplyBlock() {
        double[][] blockRes = new double[A[0].length][B.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    blockRes[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return blockRes;
    }
}
