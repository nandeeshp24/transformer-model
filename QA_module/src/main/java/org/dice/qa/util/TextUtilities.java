package org.dice.qa.util;

import org.apache.commons.lang3.StringUtils;

public class TextUtilities {
    public static double getDistanceScore(String s, String s2) {
//        return (getJWDistance(s, s2) + nGramDistance(s, s2)) / 2;
//        return (0.8 * getJWDistance(s, s2)) + (0.2 * nGramDistance(s, s2));
        return getLevenshteinRatio(s, s2);
    }

    public static double getLevenshteinRatio(String s, String s2) {
        if (s == null || s2 == null) {
            return 1;
        }
        int lfd = StringUtils.getLevenshteinDistance(s2, s);
        return ((double) lfd) / (Math.max(s2.length(), s.length()));
    }
}
