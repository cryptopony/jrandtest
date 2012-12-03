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

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * The Birthday Spacings Test. Choose m birthdays in a \"year\" of n days.
 * List the spacings between the birthdays. Let j be the number of values
 * that occur more than once in that list, then j is asymptotically Poisson
 * distributed with mean m^3/(4n). Experience shows n must be quite large,
 * say n>= 2^18, for comparing the results to the Poisson distribution with
 * that mean. This test uses n=2^24 and m=2^10, so that the underlying
 * distribution for j is taken to be Poisson with lambda=2^30/(2^26)=16. A
 * sample of 200 j's is taken, and a chi-square goodness of fit test
 * provides a p value. The first test uses bits 1-24 (counting from the
 * left) from integers in the specified file. Then the file is closed and
 * reopened, then bits 2-25 of the same integers are used to provide
 * birthdays, and so on to bits 9-32. Each set of bits provides a p-value,
 * and the nine p-values provide a sample for a KSTEST.
 * 
 * <p>Originally from DieHard.</p>
 * 
 * @author Zur Aougav
 */
public class BirthdaySpacings extends Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void test(String filename) throws Exception {

        final int no_obs = 500;
        final int no_bday = 1024;
        final int no_bits = 24;
        final int mask = (int)Math.pow(2, no_bits) - 1;
        final double lambda = Math.pow(no_bday, 3) / (4.0 * Math.pow(2, no_bits));

        int rt;
        int i, k, sum;
        int no_dup;
        double pvalue;

        double[] resultVec = null;
        int dgf = 0;
        double chi_fit;

        log.info("\t\tRESULTS OF BIRTHDAY SPACINGS TEST FOR " + filename + "\n");
        log.info("\t(no_bdays=" + no_bday + ", no_days/yr=2^" + no_bits + ",");
        log.info(" lambda=" + d4(lambda) + ", sample size=" + no_obs + ")\n\n");
        log.info("\tBits used\tmean\t\tchisqr\t\tp-value\n");

        int[] obs = new int[no_obs];
        double[] p = new double[32 - no_bits + 1];
        int[] bdspace = new int[no_bday]; // long?

        for (rt = 32 - no_bits; rt >= 0; --rt) {
            openInputStream();

            sum = 0;

            for (k = 0; isOpen() && k < no_obs; ++k) {
                for (i = 0; isOpen() && i < no_bday; ++i) {
                    bdspace[i] = (uni() >> rt) & mask;
                }

                if (!isOpen()) {
                    break;
                }

                Arrays.sort(bdspace, 0, no_bday);

                for (i = no_bday - 1; i >= 1; --i) {
                    bdspace[i] -= bdspace[i - 1];
                }

                Arrays.sort(bdspace, 0, no_bday);

                no_dup = 0;
                for (i = 1; i < no_bday; ++i) {
                    if (bdspace[i] != bdspace[i - 1]) {
                        continue;
                    }
                    no_dup++;
                }
                sum += no_dup;
                obs[k] = no_dup;
            }

            closeInputStream();

            resultVec = Stat.Poisson_fit(lambda, obs, no_obs);
            dgf = (int)resultVec[0];
            chi_fit = resultVec[1];
            p[rt] = resultVec[2];

            log.info("\t " + (33 - no_bits - rt) + " to " + (32 - rt));
            log.info("\t" + d4((double)sum / no_obs) + "\t\t" + d4(chi_fit) + "\t\t" + d4(p[rt]) + "\n");

        }

        pvalue = KStest(p, 32 - no_bits + 1);

        log.info("\n\t\t\tdegree of freedoms is: " + dgf + "\n");
        log.info("\t---------------------------------------------------------------");
        log.info("\n\t\tp-value for KStest on those " + (32 - no_bits + 1) + " p-values: " + d4(pvalue));
        log.info("\n");

        return;
    }

}