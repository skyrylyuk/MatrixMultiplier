package com.github.skyrylyuk;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.github.skyrylyuk.IsMatrixEquals.isMatrixEqualsTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Project
 * Created by skyrylyuk on 3/16/20.
 */
class MatrixMultiplierTest {

    public static final double DOUBLE_COMPARE_OVERSIGHT = 0.01;

    @Test
    void checkMultiplyMatrices() {
        final MatrixMultiplier instance = MatrixMultiplier.getInstance();
        int size = 10;
        for (int i = 1; i < 10; i++) {

            final double[][] a = generateMatrix(size);
            final double[][] b = generateMatrix(size);

            RealMatrix ma = new Array2DRowRealMatrix(a);
            RealMatrix mb = new Array2DRowRealMatrix(b);

            final double[][] actual = instance.multiplyMatrices(a, b);

            final RealMatrix expect = ma.multiply(mb);


            assertThat(actual, isMatrixEqualsTo(expect.getData(), DOUBLE_COMPARE_OVERSIGHT));

            System.out.println("i = " + i + " size = " + size);
            size = (int) (size * 1.6);
        }
    }

    @Test
    void checkMultiplyMatricesParallel() throws IOException {
        final MatrixMultiplier instance = MatrixMultiplier.getInstance();

        List<String> list = new ArrayList<>(3);

        int size = 10;
        for (int i = 1; i < 15; i++) {

            System.out.println("size = " + size);
            size = (int) (size * 1.6);


            final double[][] a = generateMatrix(size);
            final double[][] b = generateMatrix(size);


            final long sp = System.nanoTime();
            final double[][] actual = instance.multiplyMatricesParallel(a, b);
            final long ep = System.nanoTime();

            final long ss = System.nanoTime();
            final double[][] expect = instance.multiplyMatrices(a, b);
            final long se = System.nanoTime();

            assertThat(actual, isMatrixEqualsTo(expect, DOUBLE_COMPARE_OVERSIGHT));

            list.add(String.format("%d,%d,%d%n", size, (se - ss), (ep - sp)));
        }

        Path path = Paths.get("./reports/output.csv");
        System.out.println("path = " + path);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("SIZE,SEQUENTIAL,PARALLEL\n");
            for (String s : list) {
                writer.write(s);
            }
        }
    }

    private double[][] generateMatrix(int size) {
        double[][] result = new double[size][size];
        final RandomGenerator randomGenerator = new RandomDataGenerator().getRandomGenerator();
        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {
                result[i][j] = randomGenerator.nextDouble();
            }
        }
        return result;
    }
}