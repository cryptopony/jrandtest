/*
 * Created on 13/02/2005
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
 * OverlappingQuadruplesSparseOccupancy (OQSO) from DieHard
 *
 * @author Zur Aougav
 */
public class OverlappingQuadruplesSparseOccupancy extends OverlappingPairsSparseOccupancy {

    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void help() {
        log.info("\n\t|------------------------------------------------------------ |");
        log.info("\t|    OQSO means Overlapping-Quadruples-Sparse-Occupancy       |");
        log.info("\t|  The test OQSO is similar, except that it considers 4-letter|");
        log.info("\t|words from an alphabet of 32 letters, each letter determined |");
        log.info("\t|by a designated string of 5 consecutive bits from the test   |");
        log.info("\t|file, elements of which are assumed 32-bit random integers.  |");
        log.info("\t|The mean number of missing words in a sequence of 2^21 four- |");
        log.info("\t|letter words,  (2^21+3 \"keystrokes\"), is again 141909, with  |");
        log.info("\t|sigma = 295.  The mean is based on theory; sigma comes from  |");
        log.info("\t|extensive simulation.                                        |");
        log.info("\t|------------------------------------------------------------ |\n");
    }

    @Override
    public void setParameters() {
        testName = "OQSO";
        bits_pl = 5;
        std = 295.0;
    }

}
