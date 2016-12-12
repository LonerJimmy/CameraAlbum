package epiboly.loner.photo.util;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by loner on 2016/12/10.
 */

public class CameraUtil {

    private static final int MIN_PREVIEW_PIXELS = 480 * 320;

    private CameraUtil() {
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    public static Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 获取最合适的pictureview尺寸
     *
     * @param sizes
     * @return
     */
    public static Size getBestPictureSize(List<Size> sizes) {

        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    /**
     * 获取最合适的preview尺寸
     *
     * @param sizes
     * @return
     */
    public static Size getBestSupportedSize(int w, int h, List<Size> sizes) {
        int width = w;
        int height = h;

        //preview翻转的情况
        if (width > height) {
            width = h;
            height = w;
        }

        float svRate = 1.0f * width / height;

        //排序从大到小
        Collections.sort(sizes, new CameraDescendSizeComparator());

        List<RightSize> rightSizes = getRightSize(sizes);

        float min = Math.abs(svRate - 1.0f * rightSizes.get(0).width / rightSizes.get(0).height);

        Size bestSize = sizes.get(0);
        for (int i = 0; i < sizes.size(); i++) {
            Size size = sizes.get(i);
            RightSize rightSize = rightSizes.get(i);
            if (rightSize.height * rightSize.width < MIN_PREVIEW_PIXELS) {
                break;
            }
            if (Math.abs(svRate - 1.0f * rightSize.width / rightSize.height) < min) {
                min = Math.abs(svRate - 1.0f * rightSize.width / rightSize.height);
                bestSize = size;
            }
        }
        return bestSize;
    }

    private static List<RightSize> getRightSize(List<Size> sizes) {
        List<RightSize> resultList = new ArrayList<>();
        for (Size size : sizes) {
            if (size.width > size.height) {
                resultList.add(new RightSize(size.height, size.width));
            } else {
                resultList.add(new RightSize(size.width, size.height));
            }
        }
        return resultList;
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    //降序
    private static class CameraDescendSizeComparator implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            if (lhs.width * lhs.height == rhs.width * rhs.height) {
                return 0;
            } else if (lhs.width * lhs.height > rhs.width * rhs.height) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                              int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private static class RightSize {
        int height;
        int width;

        RightSize(int width, int height) {
            this.height = height;
            this.width = width;
        }
    }
}
