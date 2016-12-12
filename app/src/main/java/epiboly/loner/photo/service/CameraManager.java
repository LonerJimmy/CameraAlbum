package epiboly.loner.photo.service;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import epiboly.loner.photo.constant.CameraConfig;
import epiboly.loner.photo.control.PictureTakeCallback;
import epiboly.loner.photo.util.CameraUtil;

/**
 * Created by loner on 2016/12/10.
 */

public class CameraManager {
    private Camera mCamera;

    private static CameraManager mCameraManager;
    private Activity mActivity;
    private PictureTakeCallback pictureTakeCallback;
    private Handler handler = new Handler();

    private CameraManager(Activity activity) {
        mActivity = activity;
        mCamera = CameraUtil.getCamera(CameraConfig.CAMERA.REAR_CAMERA);
    }

    public static synchronized CameraManager getInstance(Activity activity) {
        if (mCameraManager == null) {
            mCameraManager = new CameraManager(activity);
        }
        return mCameraManager;
    }

    /**
     * 相机预览
     *
     * @param holder
     */
    public void startPreview(SurfaceHolder holder, SurfaceView surfaceView) {
        if (mCamera == null) {
            mCamera = CameraUtil.getCamera(CameraConfig.CAMERA.REAR_CAMERA);
        }
        Camera.Parameters parameters = mCamera.getParameters();

        setFocusMode(parameters);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mCamera.enableShutterSound(true);
        }

        /**
         * 设置camera最佳width和height
         */
        if (surfaceView != null) {
            Camera.Size previewSize = CameraUtil.getBestSupportedSize(surfaceView.getWidth(), surfaceView.getHeight(), parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            Log.e("loner", "best preview size width=" + previewSize.width + "  height=" + previewSize.height);
            Camera.Size pictureSize = CameraUtil.getBestSupportedSize(previewSize.width, previewSize.height, parameters.getSupportedPictureSizes());
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            Log.e("loner", "best pictureSize width=" + pictureSize.width + "  height=" + pictureSize.height);

            if (pictureSize.width >= pictureSize.height) {
                parameters.setRotation(90);
            }
        }

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(holder);
            CameraUtil.setCameraDisplayOrientation(mActivity, CameraConfig.CAMERA.REAR_CAMERA, mCamera);
            mCamera.startPreview();

            mCamera.cancelAutoFocus();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 设置自动对焦
     *
     * @param parameters
     */
    private void setFocusMode(Camera.Parameters parameters) {
        List<String> focusModes = parameters.getSupportedFocusModes();

        for (String mode : focusModes) {
            if (mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                break;
            }
        }
    }

    /**
     * 关闭相机
     */
    public void stopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void takePicture(PictureTakeCallback callback) {
        this.pictureTakeCallback = callback;
        if (mCamera != null) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    ShutterCallback mShutterCallback = new ShutterCallback() {
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };

    PictureCallback mJpegPictureCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if (null != data) {
                CameraConfig.cacheBitmap = data;
                mCamera.stopPreview();
            }

            if (pictureTakeCallback != null) {
                pictureTakeCallback.takePhoto(data);
            }

        }
    };

}
