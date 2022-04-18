package com.swoqe.second;

import com.swoqe.second.basic.BasicAlgorithm;
import com.swoqe.second.fox.FoxAlgorithm;
import com.swoqe.second.striped.StripedAlgorithm;
import com.swoqe.second.util.MatrixFactory;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

import static com.swoqe.util.TimeUtil.recordTime;

public class Main {

    public static void main(String[] args) {
//         Test Setup
        List<MultiplicationResult> multiplicationResults = runExperimentExp(15, Runtime.getRuntime().availableProcessors());

        for (int i = 2; i < 22; i= i+2) {
            System.out.println("=Active Threads: " + i);
            List<MultiplicationResult> multiplicationResults1 = runFixedExperiment(1000, i);
        }

    }

    public static List<MultiplicationResult> runExperimentExp(int nExperiments, int nThread) {
        List<MultiplicationResult> results = new ArrayList<>();
        for (int i = 1; i <= nExperiments; i++) {
            int size = i * 200;
            System.out.println("===Experiment Num. " + i + " | Matrix size " + size + "x" + size + "===");
            results.addAll(runExperiment(size, nThread));
        }
        return results;
    }

    public static List<MultiplicationResult> runFixedExperiment(int matrixSize, int nThread) {
        return runExperiment(matrixSize, nThread);
    }

    public static List<MultiplicationResult> runExperiment(int size, int nThread) {
        List<MultiplicationResult> results = new ArrayList<>();
        double[][] matrixA = MatrixFactory.generateRandomMatrix(size, size);
        double[][] matrixB = MatrixFactory.generateRandomMatrix(size, size);

        DefaultMultiplicationAlgorithm basicAlgorithm = new BasicAlgorithm(matrixA, matrixB);
        DefaultMultiplicationAlgorithm stripedAlgorithm = new StripedAlgorithm(matrixA, matrixB, nThread);
        DefaultMultiplicationAlgorithm foxAlgorithm = new FoxAlgorithm(matrixA, matrixB, nThread);

        long time = recordTime(() -> new SimpleMatrix(matrixA).mult(new SimpleMatrix(matrixB))) / 1_000_000;
        System.out.println("Time for EJML: " + time);
        results.add(new MultiplicationResult(time, size, "EJM"));

//        time = recordTime(basicAlgorithm::multiply) / 1_000_000;
//        System.out.println("Time for Basic Algorithm: " + time);
//        results.add(new MultiplicationResult(time, size, "Basic"));

        time = recordTime(stripedAlgorithm::multiply) / 1_000_000;
        System.out.println("Time for Striped Algorithm: " + time);
        results.add(new MultiplicationResult(time, size, "Striped"));

        time = recordTime(foxAlgorithm::multiply) / 1_000_000;
        System.out.println("Time for Fox Algorithm: " + time);
        results.add(new MultiplicationResult(time, size, "Fox"));

        System.out.println("\n");
        return results;
    }

}