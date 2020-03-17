package com.github.skyrylyuk;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

/**
 * Project
 * Created by skyrylyuk on 3/16/20.
 */
public class IsMatrixEquals extends TypeSafeMatcher<double[][]> {

    private double[][] sample;
    private double epsilon;

    public IsMatrixEquals(double[][] sample, double epsilon) {
        this.sample = sample;
        this.epsilon = epsilon;
    }

    protected boolean matchesSafely(double[][] exam) {

        if (sample.length == exam.length){
            for (int i = 0; i < exam.length; i++) {
                double[] examValues = exam[i];
                double[] sampleValues = sample[i];

                for (int j = 0; j < examValues.length; j++) {
                    double examValue = examValues[j];
                    double sampleValue = sampleValues[j];

                    assertThat(examValue, closeTo(sampleValue, epsilon));
                }

            }

            return true;
        } else {
            return false;
        }
    }

    public void describeTo(Description description) {

    }

    public static Matcher<double[][]> isMatrixEqualsTo(double[][] sample, double epsilon){
        return new IsMatrixEquals(sample, epsilon);
    }
}
