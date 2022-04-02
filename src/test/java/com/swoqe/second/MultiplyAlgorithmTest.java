package com.swoqe.second;

import com.swoqe.second.basic.BasicAlgorithm;
import com.swoqe.second.fox.FoxAlgorithm;
import com.swoqe.second.striped.StripedAlgorithm;
import com.swoqe.second.util.MatrixFactory;
import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiplyAlgorithmTest {

    double[][] matrixA;
    double[][] matrixB;
    int threadsNum;

    @BeforeEach
    void setUp() {
        matrixA = MatrixFactory.generateRandomMatrix(10, 10);
        matrixB = MatrixFactory.generateRandomMatrix(10, 10);
        Runtime runtime = Runtime.getRuntime();
        this.threadsNum = runtime.availableProcessors();
    }

    @Test
    void basicAlgorithm() {
        BasicAlgorithm basicAlgorithm = new BasicAlgorithm(matrixA, matrixB);
        verifyAlgorithmOnEJML(basicAlgorithm.multiply());
    }

    @Test
    void stripedAlgorithm() {
        StripedAlgorithm stripedAlgorithm = new StripedAlgorithm(matrixA, matrixB, threadsNum);
        verifyAlgorithmOnEJML(stripedAlgorithm.multiply());
    }

    @Test
    void foxAlgorithm() {
        FoxAlgorithm foxAlgorithm = new FoxAlgorithm(matrixA, matrixB, threadsNum);
        verifyAlgorithmOnEJML(foxAlgorithm.multiply());
    }

    void verifyAlgorithmOnEJML(double[][] actual) {
        SimpleMatrix expected = new SimpleMatrix(matrixA).mult(new SimpleMatrix(matrixB));
        assertThat(new SimpleMatrix(actual)).matches(matrix -> matrix.isIdentical(expected, Math.pow(1, -15)));
    }

}
