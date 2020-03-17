package com.github.skyrylyuk;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Project
 * Created by skyrylyuk on 3/16/20.
 */
public class MatrixMultiplier {

    private final ForkJoinPool forkJoinPool;
    private static MatrixMultiplier instance;

    public static MatrixMultiplier getInstance() {
        MatrixMultiplier localInstance = instance;
        if (localInstance == null) {
            synchronized (MatrixMultiplier.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MatrixMultiplier();
                }
            }
        }
        return localInstance;
    }


    private MatrixMultiplier() {
        forkJoinPool = new ForkJoinPool();
    }

    public double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                final double v = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
                result[row][col] = v;
            }
        }

        return result;
    }

    public double[][] multiplyMatricesParallel(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                forkJoinPool.submit(new VA(firstMatrix, secondMatrix, row, col, result));
            }
        }
        forkJoinPool.awaitQuiescence(60, TimeUnit.SECONDS);
        return result;
    }

    private static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }


    static class VA extends RecursiveAction {

        double[][] ma;
        double[][] mb;
        int row;
        int col;
        double[][] mc;

        public VA(double[][] ma, double[][] mb, int row, int col, double[][] mc) {
            this.ma = ma;
            this.mb = mb;
            this.row = row;
            this.col = col;
            this.mc = mc;
        }

        @Override
        protected void compute() {
            double cell = 0;
            for (int i = 0; i < ma.length; i++) {
                cell += ma[row][i] * mb[i][col];
            }
            mc[row][col] = cell;
        }
    }

}
