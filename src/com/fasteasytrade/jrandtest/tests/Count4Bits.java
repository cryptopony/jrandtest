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
 * Count4Bits class extends Base
 * <p>
 * count each 4 bit, half byte.
 * 
 * @author Zur Aougav 
 */

public class Count4Bits extends Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void help() {
        log.info("\n\t|-------------------------------------------------------------|");
        log.info("\t|    This is part of the Count test.  It counts consecutive 4 |");
        log.info("\t|bits. The sums and the differences are reported. The         |");
        log.info("\t|expection is 1/16, each sum from total 4 bits.               |");
        log.info("\t|-------------------------------------------------------------|\n");
    }

    /**
     * @param filename input file with random data
     */
    @Override
    public void test(String filename) throws Exception {
        final int no_seqs = 16;
        double[] v6 = new double[no_seqs];
        // count 4-bits: 0000/0001/.../1111		
        int j;
        long length = 0;

        log.info("\t\t\tThe Count4Bits test for file " + filename + "\n");

        openInputStream();

        byte b;
        int temp;

        while (true) {
            b = readByte();
            if (!isOpen()) {
                break;
            }
            length += 2;

            temp = 0xff & b;
            v6[temp & 0x0f]++; // increment counter
            temp = temp >>> 4; // drop 4 bit from temp
            v6[temp & 0x0f]++; // increment counter
        }

        closeInputStream();

        double pv = KStest(v6, no_seqs);
        log.info("\t ks test for " + no_seqs + " p's: " + d4(pv) + "\n");

        long k = length / no_seqs;
        //printf("\n\t expected count 4 bits. Should be: " + k);
        log.info("\n\t found " + length + " 4 bits.");
        log.info("\n\t expected avg for 4 bits: " + k);
        log.info("\n\t found avg for 4 bits: " + (long)avg(v6));
        for (j = 0; j < v6.length; j++) {
            log.info("\n\t 4 bits " + j + ": " + d4(v6[j]) + "\tdelta: " + d4(v6[j] - k) + "\t%: " + d4(100.00 * v6[j] / k - 100.00));
        }
        double t = stdev(v6, k);
        log.info("\n\t stdev for 4 bits\t: " + d4(t));
        log.info("\n\t % stdev for 4 bits\t: %" + d4(100.00 * t / k));
        log.info("\n\t chitest for 4 bits\t: " + d4(chitest(v6, k)));
        log.info("\n\t r2 for 4 bits\t\t: " + d4(r2_double(v6)));

        return;
    }

} // end class
