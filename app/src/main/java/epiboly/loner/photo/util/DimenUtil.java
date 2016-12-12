package epiboly.loner.photo.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by loner on 2016/12/10.
 */

public class DimenUtil {

    private Context mContext;
    private static DimenUtil instance;

    public DimenUtil(Context context) {
        mContext = context;
    }

    public static synchronized DimenUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DimenUtil(context);
        }
        return instance;
    }

    public float dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public Point getScreenMetrics() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    public int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    public float getScreenRate() {
        Point P = getScreenMetrics();
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    public int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
    }
}
