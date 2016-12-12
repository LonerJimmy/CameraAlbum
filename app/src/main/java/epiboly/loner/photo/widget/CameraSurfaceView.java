package epiboly.loner.photo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import epiboly.loner.photo.service.CameraManager;

/**
 * Created by loner on 2016/12/10.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private CameraManager cameraManager;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        cameraManager.startPreview(surfaceHolder, this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraManager.stopCamera();
    }

    public void start(Context context) {
        cameraManager = CameraManager.getInstance((Activity) context);
        SurfaceHolder mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

}
