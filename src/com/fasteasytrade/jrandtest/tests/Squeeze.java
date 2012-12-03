/*
 * Created on 09/02/2005
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
 * The Squeeze test.
 * 
 * Random integers are floated to get uniforms on [0,1). Starting with
 * k=2^31-1=2147483647, the test finds j, the number of iterations
 * necessary to reduce k to 1 using the reduction k=ceiling(k*U), with U
 * provided by floating integers from the file being tested. Such j''s are
 * found 100,000 times, then counts for the number of times j was
 * <=6,7,...,47,>=48 are used to provide a chi-square test for cell
 * frequencies.
 * 
 * <p>Originally from DieHard.</p>
 * 
 * @author Zur Aougav
 */

public class Squeeze extends Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    final int no_trials = 100000;
    final double ratio = no_trials / 1000000.0;
    final double std = Math.sqrt(84);

    int i;
    long k;
    int j;

    int[] f = new int[43];
    double[] Ef = { 21.03, 57.79, 175.54, 467.32, 1107.83, 2367.84, 4609.44, 8241.16, 13627.81, 20968.49, 30176.12, 40801.97, 52042.03, 62838.28, 72056.37, 78694.51, 82067.55, 81919.35, 78440.08, 72194.12, 63986.79, 54709.31, 45198.52, 36136.61, 28000.28, 21055.67, 15386.52, 10940.20, 7577.96, 5119.56, 3377.26, 2177.87, 1374.39, 849.70, 515.18, 306.66, 179.39, 103.24, 58.51, 32.69, 18.03, 9.82, 11.21 };
    double tmp;
    double chsq = 0;

    @Override
    protected ResultStatus test(Map<String,String> details) throws Exception {
        log.info("\t\t    Table of standardized frequency counts\n");
        log.info("\t\t(obs-exp)^2/exp  for j=(1,..,6), 7,...,47,(48,...)\n\t");

        rs.openInputStream();

        for (i = 0; i < 43; ++i) {
            f[i] = 0;
            Ef[i] *= ratio;
        }

        for (i = 1; i <= no_trials; ++i) {
            k = 2147483647;
            j = 0;

            /*
             *  squeeze k 
             */
            while (k != 1 && j < 48) {
                tmp = rs.read32BitsAsDouble();
                if (!rs.isOpen()) {
                    break;
                }
                if (tmp < 0 || tmp > 1) {
                    log.info("\ntmp < 0 || tmp > 1: " + tmp);
                }
                k = (long)(k * tmp + 1);
                ++j;
            }
            if (!rs.isOpen()) {
                break;
            }

            j = Math.max(j - 6, 0);
            ++f[j];
        }

        rs.closeInputStream();

        /* 
         * compute chi-square 
         */
        for (i = 0; i < 43; ++i) {
            tmp = (f[i] - Ef[i]) / Math.sqrt(Ef[i]);
            chsq += tmp * tmp;
            log.info("\t% " + Format.d4(tmp) + "  ");
            if ((i + 1) % 6 == 0) {
                log.info("\n\t");
            }
        }

        log.info("\n\t\tChi-square with 42 degrees of freedom: " + Format.d4(chsq) + "\n");
        log.info("\t\tz-score=" + Format.d4((chsq - 42.) / std) + ", p-value=" + Format.d4(1 - Stat.Chisq(42, chsq)) + "\n");
        log.info("\t_____________________________________________________________\n\n");

        return ResultStatus.UNKNOWN;
    }

}
