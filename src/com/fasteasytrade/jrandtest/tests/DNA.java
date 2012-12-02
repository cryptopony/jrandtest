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
 * DNA from DieHard
 * 
 * @author Zur Aougav
 *
 */
public class DNA extends OverlappingPairsSparseOccupancy {

    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void help() {
        log.info("\n\t|------------------------------------------------------------ |");
        log.info("\t|    The DNA test considers an alphabet of 4 letters: C,G,A,T,|");
        log.info("\t|determined by two designated bits in the sequence of random  |");
        log.info("\t|integers being tested.  It considers 10-letter words, so that|");
        log.info("\t|as in OPSO and OQSO, there are 2^20 possible words, and the  |");
        log.info("\t|mean number of missing words from a string of 2^21  (over-   |");
        log.info("\t|lapping)  10-letter  words (2^21+9 \"keystrokes\") is 141909.  |");
        log.info("\t|The standard deviation sigma=339 was determined as for OQSO  |");
        log.info("\t|by simulation.  (Sigma for OPSO, 290, is the true value (to  |");
        log.info("\t|three places), not determined by simulation.                 |");
        log.info("\t|------------------------------------------------------------ |\n");
    }

    @Override
    public void setParameters() {
        testName = "DNA";
        bits_pl = 2;
        std = 339.0;
    }

}
