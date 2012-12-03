/*
 * Created on 11/02/2005
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
 * The Minimum Distance Test.
 * 
 * It does this 100 times: choose n=8000 random points in a square of side
 * 10000. Find d, the minimum distance between the (n^2-n)/2 pairs of
 * points. If the points are truly independent uniform, then d^2, the
 * square of the minimum distance should be (very close to) exponentially
 * distributed with mean .995 . Thus 1-exp(-d^2/.995) should be uniform on
 * [0,1) and a KSTEST on the resulting 100 values serves as a test of
 * uniformity for random points in the square. Test numbers=0 mod 5 are
 * printed but the KSTEST is based on the full set of 100 random choices of
 * 8000 points in the 10000x10000 square.
 * 
 * <p>Originally from DieHard.</p>
 * 
 * @author Zur Aougav
 */
public class MinimumDistance extends Base {

    final int no_pts = 8000;
    final int no_smpl = 100;
    final int side = 10000;
    final double ratio = 10000.0 / Integer.MAX_VALUE;
    final private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void test(String filename) throws Exception {
        Point[] pts;

        int i, j, k;
        double d, dmin;
        double[] p;
        double sum = 0;
        double pvalue;

        log.info("\t\tThis is the MINIMUM DISTANCE test for file " + filename + "\n\n");
        log.info("\tSample no.\t d^2\t\t mean\t\tequiv uni\n");

        rs.openInputStream();

        pts = new Point[no_pts];
        p = new double[no_smpl];

        for (i = 1; i <= no_smpl; ++i) {
            //dmin = 2 * side * side;
            dmin = 0x7fffffffffL;
            //int same = 0;
            for (j = 0; j < no_pts; j++) {
                pts[j] = new Point();
                pts[j].y = ratio * (0xffffffffL & rs.readInt());
                if (!rs.isOpen()) {
                    System.out.println("Eof... 1");
                    break;
                }
                pts[j].x = ratio * (0xffffffffL & rs.readInt());
                if (!rs.isOpen()) {
                    System.out.println("Eof... 2");
                    break;
                }
                //if (j > 0 && pts[j].y == pts[j-1].y && pts[j].x == pts[j-1].x)
                //	same++;
            }
            if (!rs.isOpen()) {
                break;
                //if (same > 0)
                //	System.out.println("same="+same+" pts.length="+pts.length);
            }

            Arrays.sort(pts, 0, pts.length);

            /* 
             * find the minimum distance 
             */
            for (j = 0; dmin > 0 && j < no_pts - 1; j++) {
                for (k = j + 1; dmin > 0 && k < no_pts; k++) {
                    d = (pts[k].y - pts[j].y) * (pts[k].y - pts[j].y);
                    if (d < dmin) {
                        d += (pts[k].x - pts[j].x) * (pts[k].x - pts[j].x);
                        dmin = Math.min(dmin, d);
                        if (dmin == 0) {
                            System.out.println("dmin=0 @ i=" + i + " j=" + j + " k=" + k);
                        }
                    }
                }
            }

            //dmin = sqrt(dmin);

            sum += dmin;
            p[i - 1] = 1 - Math.exp(-dmin / .995); /* transforming into U[0,1] */

            if (i % 5 == 0) {
                log.info("\n\t   " + i + "\t\t" + d4(dmin) + "\t\t" + d4(sum / i) + "\t\t" + d4(p[i - 1]));
            }
        }

        rs.closeInputStream();

        log.info("\n\t--------------------------------------------------------------");
        log.info("\n\tResult of KS test on " + no_smpl + " transformed mindist^2's:");
        pvalue = KStest(p, no_smpl);
        log.info(" p-value=" + d4(pvalue) + "\n\n");

    }

}
