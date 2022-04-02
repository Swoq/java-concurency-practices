package com.swoqe.second.fox;

import com.swoqe.second.DefaultMultiplicationAlgorithm;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoxAlgorithm extends DefaultMultiplicationAlgorithm {

    private int nThread;

    public FoxAlgorithm(double[][] matrixA, double[][] matrixB, int nThread) {
        super(matrixA, matrixB);
        this.nThread = nThread;
    }

    private int findNearestDivider(int s, int p) {
    /*
    https://ru.stackoverflow.com/questions/434403/%D0%9F%D0%BE%D0%B8%D1%81%D0%BA-%D0%B1%D0%BB%D0%B8%D0%B6%D0%B0%D0%B9%D1%88%D0%B5%D0%B3%D0%BE-%D0%B4%D0%B5%D0%BB%D0%B8%D1%82%D0%B5%D0%BB%D1%8F
     */
        int i = s;
        while (i > 1) {
            if (p % i == 0) break;
            if (i >= s) {
                i++;
            } else {
                i--;
            }
            if (i > Math.sqrt(p)) i = Math.min(s, p / s) - 1;
        }

        return i >= s ? i : i != 0 ? p / i : p;
    }

    @Override
    public double[][] multiply() {
        double[][] matrixC = new double[matrixA.length][matrixB[0].length];
        validateEqualDimensions();

        this.nThread = Math.min(this.nThread, matrixA.length);
        this.nThread = findNearestDivider(this.nThread, matrixA.length);
        int step = matrixA.length / this.nThread;

        ExecutorService exec = Executors.newFixedThreadPool(this.nThread);
        ArrayList<Future<?>> futures = new ArrayList<>();

        int[][] matrixOfSizesI = new int[nThread][nThread];
        int[][] matrixOfSizesJ = new int[nThread][nThread];

        int stepI = 0;
        for (int i = 0; i < nThread; i++) {
            int stepJ = 0;
            for (int j = 0; j < nThread; j++) {
                matrixOfSizesI[i][j] = stepI;
                matrixOfSizesJ[i][j] = stepJ;
                stepJ += step;
            }
            stepI += step;
        }

        for (int l = 0; l < nThread; l++) {
            for (int i = 0; i < nThread; i++) {
                for (int j = 0; j < nThread; j++) {
                    int stepI0 = matrixOfSizesI[i][j];
                    int stepJ0 = matrixOfSizesJ[i][j];

                    int stepI1 = matrixOfSizesI[i][(i + l) % nThread];
                    int stepJ1 = matrixOfSizesJ[i][(i + l) % nThread];

                    int stepI2 = matrixOfSizesI[(i + l) % nThread][j];
                    int stepJ2 = matrixOfSizesJ[(i + l) % nThread][j];

                    FoxAlgorithmThread t = new FoxAlgorithmThread(
                            copyBlock(matrixA, stepI1, stepJ1, step),
                            copyBlock(matrixB, stepI2, stepJ2, step),
                            matrixC, stepI0, stepJ0);
                    futures.add(exec.submit(t));
                }
            }
        }

        for (Future<?> mapFuture : futures) {
            try {
                mapFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        exec.shutdown();

        return matrixC;
    }

    private double[][] copyBlock(double[][] matrix, int i, int j, int size) {
        double[][] block = new double[size][size];
        for (int k = 0; k < size; k++) {
            System.arraycopy(matrix[k + i], j, block[k], 0, size);
        }
        return block;
    }

    private void validateEqualDimensions() {
        if (!(matrixA.length == matrixA[0].length
                && matrixB.length == matrixB[0].length
                && matrixA.length == matrixB.length)) {
            throw new IllegalArgumentException();
        }
    }
}
