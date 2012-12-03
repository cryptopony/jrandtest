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

import java.util.Arrays;
import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.io.RandomStream;

/**
 * Base class for all test classes. <p> Contains common methods and handle
 * input/output list of listeners.
 * 
 * @author Zur Aougav
 */

public abstract class Base {

    final Logger log2 = Logger.getLogger(Base.class.getName());

    public static java.text.DecimalFormat df = new java.text.DecimalFormat();
    static {
        df.setMaximumFractionDigits(4);
        df.setMinimumFractionDigits(4);
    }

    /**
     * @return double with 4 decimal places (as in C "%.4f")
     */
    public double d4d_d(double d) {
        d *= 10000.0;
        return Math.ceil(d) / 10000.0;
    }

    /**
     * @return double with 4 decimal places (as in C "%.4f")
     */
    public String d4d(double d) {
        return df.format(d);
    }

    /**
     * Returns double as string 10.4: zzzzz.dddd.
     * 
     * @param d double number
     * 
     * @return String with 4 decimal places (as in C "%10.4f")
     */
    public String d4(double d) {
        //d *= 10000.0;
        //String e = "" + (Math.ceil(d) / 10000.0);
        String e = d4d(d);
        int i = e.indexOf(".");
        if (i < 0) {
            e = e + ".0000";
        } else {
            int j = 4 + i - e.length() + 1;
            for (; j > 0; j--) {
                e += "0";
            }
        }
        //while (e.length() < 10)
        //	e = " "+e;
        i = e.length();
        if (i >= 10) {
            return e;
        }
        if (i == 9) {
            return " " + e;
        }
        if (i == 8) {
            return "  " + e;
        }
        if (i == 7) {
            return "   " + e;
        }
        if (i == 6) {
            return "    " + e;
        }
        if (i == 5) {
            return "     " + e;
        }
        return e;
    }

    RandomStream rs = null;

    /**
     * register RandomStream interface. <p> Supports only one random
     * stream.
     * 
     * @param rs register radom stream interface
     */
    public void registerInput(RandomStream rs) {
        this.rs = rs;
    }

    /**
     * KStest <p> This test is based on a modified Kolmogorov-Smirnov
     * method. <p> The test-statistic is (FN(X)-X)**2/(X*(1-X))
     * (Anderson-Darling) where X is a uniform under null hypothesis. FN(X)
     * is the empirical distribution of X.
     */
    public double KStest(double[] x, int dim) {
        int i;
        double pvalue, tmp;
        double z = -dim * dim;
        double epsilon = Math.pow(10, -20);

        Arrays.sort(x, 0, dim);

        for (i = 0; i < dim; ++i) {
            tmp = x[i] * (1 - x[dim - 1 - i]);
            tmp = Math.max(epsilon, tmp);
            z -= (2 * i + 1) * Math.log(tmp);
        }

        z /= dim;
        pvalue = 1 - AD(z);

        return pvalue;
    }

    /**
     * c.d.f of Anderson-Darling statistic (a quick algorithm) <p> Used by
     * KStest
     */
    private static double AD(double z) {
        if (z < .01) {
            return 0;
        }

        if (z <= 2) {
            return 2 * Math.exp(-1.2337 / z) * (1 + z / 8 - .04958 * z * z / (1.325 + z)) / Math.sqrt(z);
        }

        if (z <= 4) {
            return 1 - .6621361 * Math.exp(-1.091638 * z) - .95095 * Math.exp(-2.005138 * z);
        }

        if (4 < z) {
            return 1 - .4938691 * Math.exp(-1.050321 * z) - .5946335 * Math.exp(-1.527198 * z);
        }

        return -1; // error indicator
    } // end AD

    /**
     * test method to be implemented by each test class.
     * 
     * @param filename is the filename to be read or the algorithm name.
     */
    public abstract void test(String filename) throws Exception;

}