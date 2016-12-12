package epiboly.loner.photo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by loner on 2016/12/10.
 */
public class ClipImageView extends ImageView {


    /**
     * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。
     *
     * @author Alan
     */
    private int mBorderThickness = 0;
    private Context mContext;
    private int defaultColor = 0xFFFFFFFF;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor = 0;
    private int mBorderInsideColor = 0;
    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    private boolean imgPhoto = false;

    public ClipImageView(Context context, boolean b) {
        super(context);
        this.imgPhoto = b;
    }


    public ClipImageView(Context context) {
        super(context);
        mContext = context;
    }

    public ClipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public ClipImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    void setCustomAttributes(AttributeSet attrs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        if (defaultWidth == 0) {
            defaultWidth = getWidth();

        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }

        Rect src = new Rect();// 图片
        Rect dst = new Rect();// 屏幕
        src.left = 0;
        src.top = 0;
        src.right = bitmap.getWidth();
        src.bottom = bitmap.getWidth();
        dst.left = 0;
        dst.top = 0;
        dst.right = defaultWidth;
        dst.bottom = defaultHeight;
        Bitmap clipBitmap = ClipBitmap(bitmap);
        //canvas.drawBitmap(clipBitmap, 0, 0 , null);

        canvas.drawBitmap(clipBitmap, src, dst, null);

        //canvas.drawBitmap(clipBitmap,);

    }

    //对要显示的缩略图进行裁剪
    private Bitmap ClipBitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int wh = width > height ? height : width;// 裁切后所取的正方形区域边长

        int retX = width > height ? (width - height) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = width > height ? 0 : (height - width) / 2;
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

}

