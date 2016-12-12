package epiboly.loner.photo.ui.camera;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import epiboly.loner.photo.R;
import epiboly.loner.photo.constant.CameraConfig;
import epiboly.loner.photo.control.PictureTakeCallback;
import epiboly.loner.photo.service.CameraManager;
import epiboly.loner.photo.util.DimenUtil;
import epiboly.loner.photo.util.FileUtil;
import epiboly.loner.photo.util.PreferenceUtil;
import epiboly.loner.photo.widget.CameraSurfaceView;

/**
 * Created by loner on 2016/12/10.
 */

public class CameraActivity extends Activity implements View.OnClickListener, PictureTakeCallback {

    CameraSurfaceView surfaceView;
    ImageView photoImageView;
    CameraManager cameraManager;
    TextView cancelTextView;
    boolean isTiming;
    DimenUtil dimenUtil;
    Context context;
    ImageView cancelImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        initParams();
        initUI();

        init();
    }

    private void initParams() {
        cameraManager = CameraManager.getInstance(this);
        isTiming = true;
    }

    private void initUI() {
        surfaceView = (CameraSurfaceView) findViewById(R.id.csv_camera);
        photoImageView = (ImageView) findViewById(R.id.iv_photo);
        photoImageView.setOnClickListener(this);
        cancelTextView = (TextView) findViewById(R.id.tv_photo_cancel);
        cancelImageView = (ImageView) findViewById(R.id.iv_return);
    }

    private void init() {
        dimenUtil = DimenUtil.getInstance(this);
        surfaceView.start(this);
        cancelTextView.setOnClickListener(this);
        cancelImageView.setOnClickListener(this);

        context = this;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_photo) {
            cameraManager.takePicture(this);
        } else if (i == R.id.tv_photo_cancel || i == R.id.iv_return) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void takePhoto(byte[] data) {

        saveImage();

        startActivityForResult(new Intent(this, ImagePreviewActivity.class), CameraConfig.SHOULD_FINISH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CameraConfig.SHOULD_FINISH_REQUEST_CODE) {
            setResult(RESULT_OK, new Intent());
            this.finish();
        }
    }

    private void saveImage() {
        if (null != CameraConfig.cacheBitmap) {
            final String url = FileUtil.getFilePath(CameraActivity.this, System.currentTimeMillis() + "");
            PreferenceUtil.saveUrl(this, url);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtil.saveBitmap(CameraConfig.cacheBitmap, url);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(url);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                }
            }).start();
        }
    }
}
