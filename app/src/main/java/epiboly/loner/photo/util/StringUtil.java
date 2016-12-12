package epiboly.loner.photo.util;

/**
 * Created by loner on 2016/12/10.
 */

public class StringUtil {
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }
}
