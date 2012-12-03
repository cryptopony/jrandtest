package com.fasteasytrade.jrandtest.tests;

import java.util.Arrays;
import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.utils.Derf;

/**
 * Statistical functions.
 */
public class Stat {

    final static Logger log = Logger.getLogger(Base.class.getName());

    private Stat() {
        super();
    }

    /**
     * gamma(z) when 2z is a integer
     */
    public static double G(double z) {
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
    public static double phi(double x) {
        return Math.exp(-x * x / 2.0) / Math.sqrt(2.0 * Math.PI);
    }

    /**
     * c.d.f of Standard Normal
     */
    public static double Phi(double x) {
        double tmp;
        tmp = x / Math.sqrt(2.0);
        tmp = 1.0 + Derf.derf(tmp);
        return tmp / 2.0;
    }

    /**
     * p.d.f of Chi-square
     */
    public static double chisq(int df, double x) {
        return (Math.pow(x / 2, (df - 2) / 2.) * Math.exp(-x / 2) / (2 * G(df / 2.)));
    }

    /**
     * c.d.f of Chi-square
     */
    public static double Chisq(int df, double x) {
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
    public static double Poisson(double lambda, int k) {
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
    public static double[] Poisson_fit(double lambda, int[] obs, int no_obs) {
        int dim = no_obs / 5;
        int i = -1;
        int j;
        int k = 0;
        double rest = no_obs;
        int[] f = new int[dim];
        double[] Ef = new double[dim];

        Arrays.sort(obs, 0, no_obs);

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
     * KStest <p> This test is based on a modified Kolmogorov-Smirnov
     * method. <p> The test-statistic is (FN(X)-X)**2/(X*(1-X))
     * (Anderson-Darling) where X is a uniform under null hypothesis. FN(X)
     * is the empirical distribution of X.
     */
    public static double KStest(double[] x, int dim) {
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
        pvalue = 1 - Stat.AD(z);
    
        return pvalue;
    }

    /**
     * c.d.f of Anderson-Darling statistic (a quick algorithm) <p> Used by
     * KStest
     */
    public static double AD(double z) {
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

}
