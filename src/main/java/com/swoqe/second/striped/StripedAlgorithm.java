package com.swoqe.second.striped;

import com.swoqe.second.DefaultMultiplicationAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedAlgorithm extends DefaultMultiplicationAlgorithm {
    private final int nThread;

    public StripedAlgorithm(double[][] matrixA, double[][] matrixB, int nThread) {
        super(matrixA, matrixB);
        this.nThread = nThread;
    }

    @Override
    public double[][] multiply() {
        return multiplyWithoutTransposedMatrix();
    }

    public double[][] multiply(boolean useTransposedMatrixB) {
        return useTransposedMatrixB ? multiplyWithTransposedMatrix() : multiplyWithoutTransposedMatrix();
    }

    public double[][] multiplyWithTransposedMatrix() {
        double[][] matrixC = new double[matrixA.length][matrixB[0].length];
        double[][] transposedMatrixB = transpose(matrixB);

        ExecutorService executor = Executors.newFixedThreadPool(this.nThread);

        List<Future<double[]>> futures = new ArrayList<>();

        for (int j = 0; j < transposedMatrixB.length; j++) {
            for (int i = 0; i < matrixA.length; i++) {
                Callable<double[]> worker =
                        new StripedAlgorithmCallableV1(matrixA[i], i, transposedMatrixB[j], j);
                Future<double[]> submit = executor.submit(worker);
                futures.add(submit);
            }
        }

        for (Future<double[]> future : futures) {
            try {
                double[] res = future.get();
                matrixC[(int) res[0]][(int) res[1]] = res[2];
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return matrixC;
    }

    public double[][] multiplyWithoutTransposedMatrix() {
        ExecutorService executor = Executors.newFixedThreadPool(this.nThread);

        List<Future<double[]>> list = new ArrayList<>();
        double[][] matrixC = new double[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            Callable<double[]> worker = new StripedAlgorithmCallableV2(matrixA[i], i, matrixB);
            Future<double[]> submit = executor.submit(worker);
            list.add(submit);
        }

        for (int i = 0; i < list.size(); i++) {
            try {
                matrixC[i] = list.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return matrixC;
    }

}
