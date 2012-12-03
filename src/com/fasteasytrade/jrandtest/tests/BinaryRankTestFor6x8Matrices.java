/*
 * Created on 12/02/2005
 *
 * JRandTest package
 *
 * Copyright (c) 2005, Zur Aougav, aougav@hotmail.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 
 * Neither the name of the JRandTest nor the names of its contributors may be 
 * used to endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.fasteasytrade.jrandtest.tests;

import java.util.Map;
import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.utils.Format;
import com.fasteasytrade.jrandtest.utils.Stat;

/**
 * The Binary Rank Test for 6x8 matrices. From each of six random 32-bit
 * integers from the generator under test, a specified byte is chosen, and
 * the resulting six bytes form a 6x8 binary matrix whose rank is
 * determined. That rank can be from 0 to 6, but ranks 0,1,2,3 are rare;
 * their counts are pooled with those for rank 4. Ranks are found for
 * 100,000 random matrices, and a chi-square test is performed on counts
 * for ranks 6,5 and (0,...,4) (pooled together).
 * 
 * <p>Originally from DieHard.</p>
 * 
 * @author Zur Aougav
 */
public class BinaryRankTestFor6x8Matrices extends Base {
    int[] row;

    int no_row, no_col;

    long mask;

    String testName = "6x8"; // test name: "6x8", "31x31", "32x32"

    int rt = 24;

    int no_matrices = 100000;

    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public Result test(Map<String,String> details) throws Exception {

        setParameters();

        double[] p = new double[rt + 1];

        do {
            p[rt] = rankStatistics();
            --rt;
        } while (rt >= 0);

        if (!testName.equals("6x8")) {
            return Result.UNKNOWN;
        }

        log.info("\t    TEST SUMMARY, 25 tests on 100,000 random 6x8 matrices");
        log.info("\n\t    These should be 25 uniform [0,1] random variates:\n");

        for (rt = 24; rt >= 0; --rt) {
            if ((rt + 1) % 5 == 0) {
                log.info("\n");
            }
            log.info("\t" + Format.d4(p[rt]));
        }

        log.info("\n\t\tThe KS test for those 25 supposed UNI's yields\n");
        log.info("\t\t\tKS p-value = " + Format.d4(Stat.KStest(p, 25)) + "\n");
        return Result.UNKNOWN;

    }

    /**
     * set some parameters, so subclasses can set different values.
     * Algorithm, in test method, runs and use these different values.
     */
    public void setParameters() {
        testName = "6x8";
        rt = 24;
        no_matrices = 100000;
    }

    /**
     * perform the test and calculate test-stat
     * 
     * @return p-value
     */
    double rankStatistics() // , int no_mtr)
    {
        double[] p6 = { .009443, 0.217439, 0.773118 };
        double[] p30 = { .0052854502, .1283502644, .5775761902, .2887880952 };
        double[] p = null; // will take p6 or p30 array!

        int i, j;
        int cls, llim;
        String[] cat = { "r<=", "r=" };
        int[] f;

        int df = 3;
        double pvalue;
        double Ef, chsq = 0, tmp;

        if (testName.equals("31x31")) {
            no_row = no_col = 31;
            mask = 0x7fffffff;
            llim = 28;
            p = p30;
        } else if (testName.equals("32x32")) {
            no_row = no_col = 32;
            mask = 0xffffffff;
            llim = 29;
            p = p30;
        } else {
            df = 2;
            no_row = 6;
            no_col = 8;
            mask = 0xff;
            llim = 4;
            p = p6;
            log.info("\n\t\t\t      bits " + (25 - rt) + " to " + (32 - rt) + "\n");
        }

        row = new int[no_row];

        f = new int[df + 1];

        log.info("\n\tRANK\tOBSERVED\tEXPECTED\t(O-E)^2/E\tSUM\n");

        rs.openInputStream();

        for (i = 1; i <= no_matrices; i++) {

            /*
             * get the rows of a matrix
             */
            for (j = 0; j < no_row; ++j) {
                if (testName.equals("31x31")) {
                    row[j] = rs.readInt() >>> 1;
                } else if (testName.equals("32x32")) {
                    row[j] = rs.readInt();
                } else {
                    row[j] = (rs.readInt() >>> rt);
                }
                row[j] &= mask;
            }

            cls = rankBinaryMatrix();
            cls = Math.max(llim, cls) - llim;
            f[cls]++;
        }

        rs.closeInputStream();

        /*
         * compute chi-square
         */
        for (i = 0; i <= df; i++) {
            Ef = no_matrices * p[i];
            tmp = (f[i] - Ef) * (f[i] - Ef) / Ef;
            chsq += tmp;
            log.info("\t" + cat[Math.min(1, i)] + (i + llim) + "\t" + Format.d4(f[i]) + "\t" + Format.d4(Ef));
            log.info("\t" + Format.d4(tmp) + "\t" + Format.d4(chsq) + "\n");
        }

        pvalue = 1 - Stat.Chisq(df, chsq);
        log.info("\n\t\tchi-square = " + Format.d4(chsq) + " with df = " + df + ";");
        log.info("  p-value = " + Format.d4(pvalue) + "\n");

        log.info("\t--------------------------------------------------------------\n");

        return pvalue;
    }

    /**
     * compute the rank of a binary matrix
     */
    int rankBinaryMatrix() // binmatrix bm)
    {
        int i, j, k, rt = 0;
        int rank = 0;
        int tmp;

        for (k = 0; k < no_row; k++) {
            i = k;

            while (((row[i] >>> rt) & 1) == 0) {
                i++;

                if (i < no_row) {
                    continue;
                }
                rt++;
                if (rt < no_col) {
                    i = k;
                    continue;
                }

                return rank;
            }

            rank++;
            if (i != k) {
                tmp = row[i];
                row[i] = row[k];
                row[k] = tmp;
            }

            for (j = i + 1; j < no_row; j++) {
                if (((row[j] >>> rt) & 1) == 0) {
                    continue;
                }
                row[j] ^= row[k];
            }

            rt++;
        }

        return rank;
    }

}