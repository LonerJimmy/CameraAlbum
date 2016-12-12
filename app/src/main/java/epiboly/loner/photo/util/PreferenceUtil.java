package epiboly.loner.photo.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import epiboly.loner.photo.constant.CameraConfig;

/**
 * Created by loner on 2016/12/10.
 */

public class PreferenceUtil {
    private PreferenceUtil() {
    }

    public static String getString(Context context, String prefName, String key) {
        return key == null ? null : getPreference(context, prefName).getString(key, (String) null);
    }

    public static String getUrl(Context context) {
        return getString(context, CameraConfig.PREFERENCE_URL, CameraConfig.PHOTO_SAVE_PATH);
    }

    public static String getUrls(Context context) {
        return getString(context, CameraConfig.PREFERENCE_URL, CameraConfig.PHOTO_SAVE_PATHS);
    }

    public static String getStringDefault(Context context, String prefName, String key, String defaultValue) {
        return key == null ? null : getPreference(context, prefName).getString(key, defaultValue);
    }

    public static void saveUrl(Context context, String value) {
        putString(context, CameraConfig.PREFERENCE_URL, CameraConfig.PHOTO_SAVE_PATH, value);
    }

    public static void saveUrls(Context context, String value) {
        putString(context, CameraConfig.PREFERENCE_URL, CameraConfig.PHOTO_SAVE_PATHS, value);
    }

    public static void putString(Context context, String prefName, String key, String value) {
        if (key != null) {
            getPreference(context, prefName).edit().putString(key, value).commit();
        }

    }

    public static int getInt(Context context, String prefName, String key) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
    }

    public static void putInt(Context context, String prefName, String key, int value) {
        if (key != null) {
            getPreference(context, prefName).edit().putInt(key, value).commit();
        }

    }

    public static long getLong(Context context, String prefName, String key) {
        return key == null ? 0L : getPreference(context, prefName).getLong(key, 0L);
    }

    public static void putLong(Context context, String prefName, String key, long value) {
        if (key != null) {
            getPreference(context, prefName).edit().putLong(key, value).commit();
        }

    }

    public static float getFloat(Context context, String prefName, String key) {
        return key == null ? 0.0F : getPreference(context, prefName).getFloat(key, 0.0F);
    }

    public static void putFloat(Context context, String prefName, String key, float value) {
        if (key != null) {
            getPreference(context, prefName).edit().putFloat(key, value).commit();
        }

    }

    public static boolean getBoolean(Context context, String prefName, String key) {
        return key != null && getPreference(context, prefName).getBoolean(key, false);
    }

    public static boolean getBooleanDefault(Context context, String prefName, String key, boolean defaultValue) {
        return key != null && getPreference(context, prefName).getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String prefName, String key, boolean value) {
        if (key != null) {
            getPreference(context, prefName).edit().putBoolean(key, value).commit();
        }

    }

    public static Set<String> getStringSet(Context context, String prefName, String key) {
        return (Set) (key == null ? new HashSet() : getPreference(context, prefName).getStringSet(key, new HashSet()));
    }

    public static void putStringSet(Context context, String prefName, String key, Set<String> value) {
        if (key != null) {
            getPreference(context, prefName).edit().putStringSet(key, value).commit();
        }

    }

    public static void remove(Context context, String prefName, String key) {
        if (key != null) {
            getPreference(context, prefName).edit().remove(key).commit();
        }

    }

    public static void clearAll(Context context, String prefName) {
        getPreference(context, prefName).edit().clear().commit();
    }

    private static SharedPreferences getPreference(Context context, String prefName) {
        return context.getSharedPreferences(prefName, 0);
    }
}
