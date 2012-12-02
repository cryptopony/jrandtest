/*
 * Created on 03/02/2005
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

import java.util.logging.Logger;

/**
 * Count2Bits class extends Base
 * <p>
 * count each bit, the 0's and 2's
 *
 * @author Zur Aougav 
 */

public class Count2Bits extends Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void help() {
        log.info("\n\t|-------------------------------------------------------------|");
        log.info("\t|    This is part of the Count test.  It counts consecutive 2 |");
        log.info("\t|bits. The sums and the differences are reported. The         |");
        log.info("\t|expection is 25%, each sum from total 2 bits.                |");
        log.info("\t|-------------------------------------------------------------|\n");
    }

    /**
     * @param filename input file with random data
     */
    @Override
    public void test(String filename) throws Exception {
        final int no_seqs = 4;
        double[] v5 = new double[4]; // count 2-bits: 00/01/10/11
        int j;
        long length = 0;

        log.info("\t\t\tThe Count2Bits test for file " + filename + "\n");

        openInputStream();

        byte b;
        int temp;

        while (true) {
            b = readByte();
            if (!isOpen()) {
                break;
            }
            length += 4;

            temp = 0xff & b;
            for (j = 0; j < 4; j++) {
                v5[temp & 0x03]++; // increment counter
                temp = temp >>> 2; // drop 2 bit from temp
            }
        }

        closeInputStream();

        double pv = KStest(v5, no_seqs);
        log.info("\t ks test for " + no_seqs + " p's: " + d4(pv) + "\n");

        long k = length / v5.length;
        log.info("\n\t found " + length + " 2 bits.");
        log.info("\n\t expected avg for 2 bits: " + k);
        log.info("\n\t found avg for 2 bits: " + d4((long)avg(v5)));
        for (j = 0; j < 4; j++) {
            log.info("\n\t count 2 bits " + j + ": " + d4(v5[j]) + " delta: " + d4(v5[j] - k) + " %: " + d4(100.00 * v5[j] / k - 100.00));
        }

        double t = stdev(v5, k);
        log.info("\n\t stdev for 2 bits\t: " + d4(t));
        log.info("\n\t % stdev for 2 bits\t: %" + d4(100.00 * t / k));
        log.info("\n\t chitest for 2 bits\t: " + d4(chitest(v5, k)));
        log.info("\n\t r2 for 2 bits\t\t: " + d4(r2_double(v5)));

        return;
    }
} // end class
