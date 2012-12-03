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

import java.util.Map;
import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.utils.Derf;
import com.fasteasytrade.jrandtest.utils.Format;
import com.fasteasytrade.jrandtest.utils.Stat;

/**
 * Counts individual bits. It counts the bits, 0's and 1's. The sums and
 * the differences are reported. The expection is 50%, each sum from total
 * bits.
 * 
 * <p>Originally from DieHard.</p>
 * 
 * @author Zur Aougav
 */

public class Count1Bit extends Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    /**
     */
    @Override
    protected ResultStatus test(Map<String,String> details) throws Exception {
        final int no_seqs = 2;
        double[] v4 = new double[no_seqs]; // count bit 0's and 1's
        int j;
        long length = 0;

        rs.openInputStream();

        byte b;
        int temp;

        while (true) {
            b = rs.readByte();
            if (!rs.isOpen()) {
                break;
            }
            length += 8;

            temp = 0xff & b;
            for (j = 0; j < 8; j++) {
                v4[temp & 1]++; // increment counter 0 or 1
                temp = temp >>> 1; // drop 1 bit from temp
            }
        }

        rs.closeInputStream();

        double pv = Stat.KStest(v4, no_seqs);
        log.info("\t ks test for " + no_seqs + " p's: " + Format.d4(pv) + "\n");

        //printf("\n\t count bits 0&1. Should be: " + length * 4);
        long k = length / v4.length;
        log.info("\n\t found " + length + " 1 bit.");
        log.info("\n\t expected avg for 1 bit: " + k);
        log.info("\n\t found avg for 1 bit: " + (long)Stat.avg(v4));
        for (j = 0; j < no_seqs; j++) {
            log.info("\n\t bit " + j + ": " + Format.d4(v4[j]) + " delta: " + Format.d4(v4[j] - k) + " %: " + Format.d4((100.00 * v4[j] / k - 100.00)));
        }

        log.info("\n\t the sum is\t\t:" + (long)(v4[1] - v4[0]));
        log.info("\n\t % sum/n is\t\t: %" + Format.d4(100 * (v4[1] - v4[0]) / length));

        double t = Stat.stdev(v4, k);
        log.info("\n\t stdev for 1 bit\t: " + Format.d4(t));
        log.info("\n\t % stdev for 1 bit\t: %" + Format.d4(100.00 * t / k));
        log.info("\n\t chitest for 1 bit\t: " + Format.d4(Stat.chitest(v4, k)));
        log.info("\n\t r2 for 1 bit\t\t: " + Format.d4(Stat.r2_double(v4)));

        double e = Math.abs(v4[1] - v4[0]) / Math.sqrt(2.0 * length);
        e = Derf.derfc(e);
        log.info("\n\t pValue (erfc)\t\t\t: " + Format.d4(e));

        return ResultStatus.UNKNOWN;
    }

}
