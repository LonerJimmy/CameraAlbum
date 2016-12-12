package epiboly.loner.photo.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by loner on 2016/12/10.
 */

public class ToastUtil {

    private static void show(Context context, @StringRes int resId, int length) {
        if (resId > 0) {
            Toast.makeText(context, resId, length).show();
        }
    }

    public static void showToast(Context context, @StringRes int resId) {
        show(context, resId, 0);
    }

}
