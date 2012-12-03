/*
 * Created on 31/01/2005
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

import com.fasteasytrade.jrandtest.utils.Stat;

/**
 * The Runs test.
 * 
 * It counts runs up, and runs down, in a sequence of uniform [0,1)
 * variables, obtained by float- ing the 32-bit integers in the specified
 * file. This example shows how runs are counted: .123, .357, .789, .425,
 * .224, .416, .95 contains an up-run of length 3, a down-run of length 2
 * and an up-run of (at least) 2, depending on the next values. The
 * covariance matrices for the runs-up and runs-down are well known,
 * leading to chisquare tests for quadratic forms in the weak inverses of
 * the covariance matrices. Runs are counted for sequences of length
 * 10,000. This is done ten times. Then another three sets of ten.
 * 
 * <p>Originally from DieHard.</p>
 * 
 * @author Zur Aougav
 */

public class Run extends Base {
    final private Logger log = Logger.getLogger(getClass().getName());

    /**
     * @param x array of doubles
     * @param length is length of array x
     * @param ustat array with one double, to return value to caller
     * @param dstat array with one double, to return value to caller
     */
    public void udruns(double[] x, int length, double[] ustat, double[] dstat) throws Exception {
        int ru = 0;
        int rd = 0;
        int i;
        int j;
        int[] ucnt;
        int[] dcnt;

        double up;
        double[][] a = { { 4529.4, 9044.9, 13568., 18091., 22615., 27892. }, { 9044.9, 18097., 27139., 36187., 45234., 55789. }, { 13568., 27139., 40721., 54281., 67852., 83685. }, { 18091., 36187., 54281., 72414., 90470., 111580. }, { 22615., 45234., 67852., 90470., 113262., 139476. }, { 27892., 55789., 83685., 111580., 139476., 172860. } };

        double[] b = { 1. / 6, 5. / 24, 11. / 120, 19. / 720, 29. / 5040, 1. / 840 };

        if (length < 4000) {
            log.info("Length of the sequence is too short (< 4000)!!!");
            throw new Exception("Length of the sequence is too short (< 4000)!!!");
            //System.exit(0);
        }

        ucnt = new int[6];
        dcnt = new int[6];

        /*
         * The loop determines the number of runs-up and runs-down of 
         * length i for i = 1(1)5 and the number of runs-up and runs-down
         * of length greater than or equal to 6. 
         */

        for (i = 1; i < length; ++i) {
            up = x[i] - x[i - 1];

            /* 
             * this does not happen actually. it is included for logic reason 
             */
            if (up == 0) {
                if (x[i] <= .5) {
                    up = -1;
                } else {
                    up = 1;
                }
            }

            if (up > 0) {
                ++dcnt[rd];
                rd = 0;
                ru = Math.min(ru + 1, 5);
                continue;
            }

            if (up < 0) {
                ++ucnt[ru];
                ru = 0;
                rd = Math.min(rd + 1, 5);
                continue;
            }
        }

        ++ucnt[ru];
        ++dcnt[rd];

        /*
         * calculate the test-stat
         */
        ustat[0] = 0;
        dstat[0] = 0;
        for (i = 0; i < 6; ++i) {
            for (j = 0; j < 6; ++j) {
                ustat[0] += (ucnt[i] - length * b[i]) * (ucnt[j] - length * b[j]) * a[i][j];
                dstat[0] += (dcnt[i] - length * b[i]) * (dcnt[j] - length * b[j]) * a[i][j];
            }
        }

        ustat[0] /= length;
        dstat[0] /= length;

    } // end udruns

    @Override
    protected ResultStatus test(Map<String,String> details) throws Exception {
        final int no_sets = 2;
        final int no_seqs = 10;
        final int length = 10000;

        int i, j, k;
        double[] x;
        double[] ustat = new double[1];
        double[] dstat = new double[1];
        double[] pu;
        double[] pd;
        double pv;

        log.info("\t\t(Up and down runs in a sequence of 10000 numbers)");

        rs.openInputStream();

        x = new double[length];
        pu = new double[no_seqs];
        pd = new double[no_seqs];

        for (i = 1; i <= no_sets; ++i) {
            for (j = 0; j < no_seqs; ++j) {
                for (k = 0; k < length; ++k) {
                    //x[k] = uni() / (0.000 + UNIMAX);
                    x[k] = rs.read32BitsAsDouble();
                    //System.out.println("x["+k+"]="+x[k]);
                }

                udruns(x, length, ustat, dstat);
                pu[j] = Stat.Chisq(6, ustat[0]);
                pd[j] = Stat.Chisq(6, dstat[0]);
            }

            pv = Stat.KStest(pu, no_seqs);
            log.info("\n\t\t\t\tSet " + i + "\n");
            log.info("\t\t runs up; ks test for " + no_seqs + " p's: " + pv + "\n");
            pv = Stat.KStest(pd, no_seqs);
            log.info("\t\t runs down; ks test for " + no_seqs + " p's: " + pv + "\n");
        }

        rs.closeInputStream();

        return ResultStatus.UNKNOWN;
    }

}
