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
 * Counts the 1 bits. Consider the file under test as a stream of bytes
 * (four per 32 bit integer). Each byte can contain from 0 to 8 1's, with
 * probabilities 1,8,28,56,70,56,28,8,1 over 256. Now let the stream of
 * bytes provide a string of overlapping 5-letter words, each "letter"
 * taking values A,B,C,D,E. The letters are determined by the number of 1's
 * in a byte: 0,1,or 2 yield A, 3 yields B, 4 yields C, 5 yields D and 6,7
 * or 8 yield E. Thus we have a monkey at a typewriter hitting five keys
 * with various probabilities (37,56,70,56,37 over 256). There are 5^5
 * possible 5-letter words, and from a string of 256,000 (over-lapping)
 * 5-letter words, counts are made on the frequencies for each word. The
 * quadratic form in the weak inverse of the covariance matrix of the cell
 * counts provides a chisquare test: Q5-Q4, the difference of the naive
 * Pearson sums of (OBS-EXP)^2/EXP on counts for 5- and 4-letter cell
 * counts.
 * 
 * @author Zur Aougav
 */
public class CountThe1s extends Base {
    // the Money at a typewriter hitting 5 keys - A, B, C, D, E
    final int A = 0;

    final int B = 1;

    final int C = 2;

    final int D = 3;

    final int E = 5;

    final double mean = 2500.0;

    final double std = Math.sqrt(5000.0);

    final double prob[] = { 37. / 256., 56. / 256., 70. / 256., 56. / 256., 37. / 256. };

    int rt = 24;

    int no_wds = 256000;

    String s = "bits used";

    boolean testStream = false;

    final private Logger log = Logger.getLogger(getClass().getName());

    /**
     * to support CountThe1s as stream (current class) and also
     * CountThe1sSpecificBytes, we use differrent help() and
     * setParameters() methods.
     */
    public void setParameters() {
        testStream = true;
        rt = -1;
        s = "\t";
        no_wds = 2560000;
    }

    @Override
    protected ResultStatus test(Map<String,String> details) throws Exception {

        setParameters();

        log.info("\t  (Degrees of freedom: 5^4-5^3=2500; sample size: " + no_wds + ")\n\n");
        log.info("\t" + s + "\tchisquare\tz-score\t\tp-value\n");

        do {
            cnt_stat(rt);
            --rt;
        } while (rt >= 0);

        return ResultStatus.UNKNOWN;
    }

    /**
     * convert a byte to a letter : A to E
     * 
     * @return final int A to E
     */
    int b2l(int x) {
        int cnt1s = 0;

        for (int j = 0; j < 8; ++j) {
            if (((x >>> j) & 1) == 1) {
                cnt1s++;
            }
        }

        if (cnt1s < 3) {
            return A;
        }
        if (cnt1s == 3) {
            return B;
        }
        if (cnt1s == 4) {
            return C;
        }
        if (cnt1s == 5) {
            return D;
        }
        return E;
    }

    /**
     * count the 1's in the sequence of bytes
     */
    double cnt_stat(int rshft) {
        int wd;
        int i, j;
        int[] f = null; // will be used as f4 or f5
        int[] f4 = new int[625];
        int[] f5 = new int[3125];
        double Ef, chsq = 0, z;

        if (!testStream) {
            log.info("\t" + (25 - rshft) + " to " + (32 - rshft) + "  ");
        } else {
            log.info("\t\t");
        }

        rs.openInputStream();

        int x1, x2, x3, x4, x5;
        if (testStream) {
            x1 = b2l(0xff & rs.readByte());
            x2 = b2l(0xff & rs.readByte());
            x3 = b2l(0xff & rs.readByte());
            x4 = b2l(0xff & rs.readByte());
            x5 = b2l(0xff & rs.readByte());
        } else {
            x1 = b2l(0xff & (rs.readInt() >>> rshft));
            x2 = b2l(0xff & (rs.readInt() >>> rshft));
            x3 = b2l(0xff & (rs.readInt() >>> rshft));
            x4 = b2l(0xff & (rs.readInt() >>> rshft));
            x5 = b2l(0xff & (rs.readInt() >>> rshft));
        }

        wd = 625 * x1 + 125 * x2 + 25 * x3 + 5 * x4 + x5;

        for (i = 1; i < no_wds; i++) {
            /*
             * Erase leftmost letter of w
             */
            wd %= 625;
            f4[wd]++;

            if (testStream) {
                x1 = b2l(0xff & rs.readByte());
            } else {
                x1 = b2l(0xff & (rs.readInt() >>> rshft));
            }
            if (!rs.isOpen()) {
                break;
            }

            /*
             * Shift wd left, add new letter
             */
            wd = 5 * wd + x1;
            f5[wd]++;
        }

        rs.closeInputStream();

        /**
         * compute Q5-Q4, where Q4,Q5=sum(obs-exp)**2/exp
         */
        for (int s = 4; s <= 5; s++) {
            int ltrspwd = 0;
            int wdspos = 0;
            int ltr = 0;

            switch (s) {
                case 4:
                    wdspos = 625;
                    f = f4;
                    ltrspwd = 4;
                    break;
                case 5:
                    wdspos = 3125;
                    f = f5;
                    chsq = -chsq;
                    ltrspwd = 5;
                    break;
                default:
                    throw new AssertionError();
            }

            for (i = 0; i < wdspos; i++) {
                Ef = no_wds;
                wd = i;

                for (j = 0; j < ltrspwd; j++) {
                    ltr = wd % 5;
                    Ef *= prob[ltr];
                    wd /= 5;
                }

                if (Ef != 0) {
                    chsq += (f[i] - Ef) * (f[i] - Ef) / Ef;
                }
            }
        }

        z = (chsq - mean) / std;
        log.info("\t" + Format.d4(chsq) + "\t\t" + Format.d4(z) + "\t\t" + Format.d4(1.0 - Stat.Phi(z)) + "\n");

        return chsq;
    }

}