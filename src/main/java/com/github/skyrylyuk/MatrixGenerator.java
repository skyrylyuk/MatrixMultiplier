package com.github.skyrylyuk;


import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
/**
 * Project
 * Created by skyrylyuk on 3/17/20.
 */
public class MatrixGenerator {

    public static double[][] generateMatrix(int size) {
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
