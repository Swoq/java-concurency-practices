package com.swoqe.second.striped;

import java.util.concurrent.Callable;

public class StripedAlgorithmCallableV1 implements Callable<double[]> {
  private final int rowIndex;
  private final int columnIndex;
  private final double[] row;
  private final double[] column;

  public StripedAlgorithmCallableV1(double[] row, int rowIndex, double[] column, int columnIndex) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.column = column;
    this.columnIndex = columnIndex;
  }

  @Override
  public double[] call() {
    double[] result = new double[3];
    double value = 0;
    for (int i = 0; i < row.length; i++) {
      value += row[i] * column[i];
    }

    result[0] = this.rowIndex;
    result[1] = this.columnIndex;
    result[2] = value;
    return result;
  }
}
