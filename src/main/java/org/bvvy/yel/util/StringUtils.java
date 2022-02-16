package org.bvvy.yel.util;

/**
 * @author bvvy
 * @date 2022/2/14
 */
public class StringUtils {

    public static boolean hasText(CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    public static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0 || !containsText(str));
    }

    public static String capitalize(String suffix) {
        return null;
    }
}
