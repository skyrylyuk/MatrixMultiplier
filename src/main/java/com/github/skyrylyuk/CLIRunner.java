package com.github.skyrylyuk;


import org.apache.commons.cli.*;

import java.time.Duration;

import static com.github.skyrylyuk.MatrixGenerator.generateMatrix;

/**
 * Project
 * Created by skyrylyuk on 3/16/20.
 */
public class CLIRunner {


    public static void main(String[] args) {

        Options options = new Options();
        options.addRequiredOption("s", "size", true, "Matrix size");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        if (cmd != null && cmd.hasOption("s")) {
            int size = Integer.parseInt(cmd.getOptionValue("s"));

            final double[][] a = generateMatrix(size);
            final double[][] b = generateMatrix(size);

            final MatrixMultiplier matrixMultiplier = MatrixMultiplier.getInstance();

            final long sp = System.currentTimeMillis();
            matrixMultiplier.multiplyMatricesParallel(a, b);
            final long ep = System.currentTimeMillis();

            final long ss = System.currentTimeMillis();
            matrixMultiplier.multiplyMatrices(a, b);
            final long se = System.currentTimeMillis();


            final Duration p = Duration.ofNanos(ep - sp);
            final Duration s = Duration.ofNanos(se - ss);
            System.out.printf("Parallel execution time:\t%ds\t%dms\t%dns \n", p.getSeconds(), p.toMillis(), p.getNano());
            System.out.printf("Sequential execution time:\t%ds\t%dms\t%dns \n", s.getSeconds(), s.toMillis(), s.getNano());


        }
    }
}
