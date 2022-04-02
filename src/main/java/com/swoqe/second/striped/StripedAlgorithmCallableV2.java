package com.swoqe.second.striped;

import java.util.concurrent.Callable;

public class StripedAlgorithmCallableV2 implements Callable<double[]> {
  private final int rowIndex;
  private final double[] row;
  private double[][] matrixB;
  private double[] result;

  public StripedAlgorithmCallableV2(double[] row, int rowIndex) {
    this.row = row;
    this.rowIndex = rowIndex;
  }

  public StripedAlgorithmCallableV2(double[] row, int rowIndex, double[][] matrixB) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.matrixB = matrixB;
    this.result = new double[matrixB[0].length];
  }

  @Override
  public double[] call() {
    for (int j = 0; j < matrixB[0].length; j++) {
      for (int i = 0; i < row.length; i++) {
        result[j] += row[i] * matrixB[i][j];
      }
    }
    return this.result;
  }

}
