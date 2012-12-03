package com.fasteasytrade.jrandtest.utils;

import java.text.DecimalFormat;

public class Format {

    public static java.text.DecimalFormat df = new java.text.DecimalFormat();

    static {
        Format.df.setMaximumFractionDigits(4);
        Format.df.setMinimumFractionDigits(4);
    }

    private Format() {
        super();
    }

    /**
     * @return double with 4 decimal places (as in C "%.4f")
     */
    public static double d4d_d(double d) {
        d *= 10000.0;
        return Math.ceil(d) / 10000.0;
    }

    /**
     * @return double with 4 decimal places (as in C "%.4f")
     */
    public static String d4d(double d) {
        return df.format(d);
    }

    /**
     * Returns double as string 10.4: zzzzz.dddd.
     * 
     * @param d double number
     * 
     * @return String with 4 decimal places (as in C "%10.4f")
     */
    public static String d4(double d) {
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

}
