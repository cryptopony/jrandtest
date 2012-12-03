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

import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.io.RandomStream;
import com.fasteasytrade.jrandtest.utils.Derf;

/**
 * Base class for all test classes. <p> Contains common methods and handle
 * input/output list of listeners.
 * 
 * @author Zur Aougav
 */

public abstract class Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    final public int DIM = 4096;

    final public long UNIMAX = 4294967296L; //  pow(2,32)

    public double SIGN(double a) {
        return (a > 0 ? 1 : -1);
    }

    public int SIGN(int a) {
        return (a > 0 ? 1 : -1);
    }

    /**
     * Bubble sort array
     * 
     * @param arr is a double array
     * @param dim only first dim entries are sorted in array arr. So part
     *            of the array can be sorted.
     */
    public void qsort(double[] arr, int dim) {
        int i, j;
        double temp;

        for (i = 0; i < dim - 1; i++) {
            for (j = i + 1; j < dim; j++) {
                if (arr[i] > arr[j]) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * Bubble sort array
     * 
     * @param arr is an int array
     * @param dim only first dim entries are sorted in array arr. So part
     *            of the array can be sorted.
     */
    public void qsort(int[] arr, int dim) {
        int i, j;
        int temp;

        for (i = 0; i < dim - 1; i++) {
            for (j = i + 1; j < dim; j++) {
                if (arr[i] > arr[j]) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * @author Zur Aougav <p> point class is used by MinimumDistance
     * 
     */
    public class point {
        public double x;

        public double y;
    }

    /**
     * Bubble sort array of points (of class point),
     * 
     * @param arr is a point array
     * @param dim only first dim entries are sorted in array arr. So part
     *            of the array can be sorted.
     */
    public void qsort(point[] arr, int dim) {
        int i, j;
        point temp;

        for (i = 0; i < dim - 1; i++) {
            for (j = i + 1; j < dim; j++) {
                if (arr[i].y > arr[j].y) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

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

    /**
     * gamma(z) when 2z is a integer
     */
    public double G(double z) {
        int tmp = 2 * (int)z;

        if (tmp != 2 * (int)z || z == 0) {
            log.info("Error in calling G(z)!!!");
            return 0.0000001;
        }

        if (tmp == 1) {
            return Math.sqrt(Math.PI);
        } else if (tmp == 2) {
            return 1;
        }
        return (z - 1) * G(z - 1);
    }

    /**
     * p.d.f of Standard Normal
     */
    public double phi(double x) {
        return Math.exp(-x * x / 2.0) / Math.sqrt(2.0 * Math.PI);
    }

    /**
     * c.d.f of Standard Normal
     */
    public double Phi(double x) {
        double tmp;
        tmp = x / Math.sqrt(2.0);
        tmp = 1.0 + Derf.derf(tmp);
        return tmp / 2.0;
    }

    /**
     * p.d.f of Chi-square
     */
    public double chisq(int df, double x) {
        return (Math.pow(x / 2, (df - 2) / 2.) * Math.exp(-x / 2) / (2 * G(df / 2.)));
    }

    /**
     * c.d.f of Chi-square
     */
    public double Chisq(int df, double x) {
        if (df == 1) {
            return 2 * Phi(Math.sqrt(x)) - 1;
        } else if (df == 2) {
            return 1 - Math.exp(-x / 2);
        } else {
            return (Chisq(df - 2, x) - 2 * chisq(df, x));
        }
    }

    /**
     * p.d.f of Poisson distribution
     */
    public double Poisson(double lambda, int k) {
        if (k == 0) {
            return Math.exp(-lambda);
        }
        return Math.exp(-lambda) * Math.pow(lambda, k) / G(k + 1);
    }

    public static double chitest(double[] data, double expected) {
        double s = 0;
        if (expected == 0) {
            return 0;
        }

        for (int i = 0; i < data.length; i++) {
            s += (data[i] - expected) * (data[i] - expected) / expected;
        }
        return s;
    } // end chitest

    /**
     * Returns double Standard Deviation
     * 
     * @param data array of doubles
     * @param avg avg of data array
     */
    public static double stdev(double[] data, double avg) {
        double s1 = 0;
        double s2 = 0;

        for (int i = 0; i < data.length; i++) {
            s2 += (data[i] * data[i]) / data.length;
        }

        s1 = avg * avg;
        return Math.sqrt(s2 - s1);
    }

    public static double r2_double(double[] data) {
        // k is the length
        int k = data.length;
        double n = k - 1.0;
        double sumx = 0;
        double sumsqx = 0;
        double sumy = 0;
        double sumsqy = 0;
        double xy = 0;
        double p, p1;

        for (int i = 1; i < k; i++) {
            p = (data[i]);
            p1 = (data[i - 1]);
            sumx += p;
            sumsqx += p * p;
            sumy += p1;
            sumsqy += p1 * p1;
            xy += p * p1;
        }
        double ssx = sumsqx - ((sumx * sumx) / n);
        double ssy = sumsqy - ((sumy * sumy) / n);
        double ssxy = xy - ((sumx * sumy) / n);
        double r = ssxy / Math.sqrt(ssx * ssy);
        return r;
    }

    /**
     * computes the statistic of goodness of fit to a Poisson distribution
     * 
     * @return vector of 3 doubles: (1) degree of freedom, (2) chi_fit (3)
     *         piValue
     * 
     *         used by Birthday Spacings Test from diehard
     */
    public double[] Poisson_fit(double lambda, int[] obs, int no_obs) {
        int dim = no_obs / 5;
        int i = -1;
        int j;
        int k = 0;
        double rest = no_obs;
        int[] f = new int[dim];
        double[] Ef = new double[dim];

        qsort(obs, no_obs);

        for (j = 0; j < dim; j++) {
            while (Ef[j] < 5) {
                i++;
                Ef[j] += no_obs * Poisson(lambda, i);
            }

            while (k < no_obs && obs[k] <= i) {
                f[j]++;
                k++;
            }

            rest -= Ef[j];
            if (rest < 5) {
                Ef[j] += rest;
                f[j] += no_obs - k;
                break;
            }
        }

        /*
         * chi_fit & dgf to be returned!
         */
        int dgf;
        double chi_fit = 0;

        for (i = 0; i <= j; i++) {
            chi_fit += (f[i] - Ef[i]) * (f[i] - Ef[i]) / Ef[i];
        }

        dgf = j;

        double result = 1 - Chisq(dgf, chi_fit);

        double[] resultVec = { dgf, chi_fit, result };
        return resultVec;
    }

    /**
     * @param data input array of doubles
     * @return avg of all enties in data array
     */
    public static double avg(double[] data) {
        double sum = 0;

        if (data == null || data.length == 0) {
            return 0;
        }

        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }

        return sum / data.length;
    } // end avg

    /**
     * show the bit-pattern of an integer 32 bits
     */
    public void showbit(int n) {
        for (int i = 0; i < 32; ++i) {
            if ((n & 0x8000) == 0) {
                log.info("0");
            } else {
                log.info("1");
            }
            n <<= 1;
        }
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
     * checks if input random stream is open. <p> random stream isOpen
     * method is used to return boolean.
     * 
     * @return true if input random stream is open, else false.
     */
    public boolean isOpen() {
        if (rs == null) {
            return false;
        }

        return rs.isOpen();
    }

    /**
     * open registered RandomStream.
     * 
     * @return boolean as true for success, else false.
     */
    public boolean openInputStream() {
        if (rs == null) {
            return false;
        }
        try {
            return rs.openInputStream();
        } catch (Exception e) {
            log.info("" + e);
            return false;
        }
    }

    /**
     * open registered RandomStream.
     * 
     * @return boolean as true for success, else false.
     */
    public boolean closeInputStream() {
        if (rs == null) {
            return false;
        }
        try {
            return rs.closeInputStream();
        } catch (Exception e) {
            log.info("" + e);
            return false;
        }
    }

    /**
     * read one byte from registered input random stream.
     * 
     * @return one byte, 8 bits
     */
    //public byte readByte(String filename)
    public byte readByte() {
        if (rs == null) {
            return -1;
        }
        try {
            //return rs.readByte(filename);
            return rs.readByte();
        } catch (Exception e) {
            log.info("" + e);
            return -1;
        }
    }

    /**
     * read one byte from registered input random stream.
     * 
     * @return a uniform random number, 32 bits
     */
    public int readInt() {
        if (rs == null) {
            return -1;
        }

        try {
            return rs.readInt();
        } catch (Exception e) {
            log.info("" + e);
            return -1;
        }
    }

    /**
     * read one long from registered input random stream.
     * 
     * @return a uniform random long number, 64 bits
     */
    public long readLong() {
        if (rs == null) {
            return -1;
        }

        try {
            return rs.readLong();
        } catch (Exception e) {
            log.info("" + e);
            return -1;
        }
    }

    /**
     * Use readInt method.
     */
    public int uni() {
        return readInt();
    }

    /**
     * read one int from registered input random stream and divide it by
     * (2^32 - 1).
     * 
     * @return double number netween 0 and 1.
     */
    public double read32BitsAsDouble() {
        return ((double)(0x00ffffffffL & readInt())) / (double)0x0ffffffffL;
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

        qsort(x, dim);

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