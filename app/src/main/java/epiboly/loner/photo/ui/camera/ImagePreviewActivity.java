package epiboly.loner.photo.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import epiboly.loner.photo.R;
import epiboly.loner.photo.components.BaseActivity;
import epiboly.loner.photo.constant.CameraConfig;
import epiboly.loner.photo.util.FileUtil;
import epiboly.loner.photo.util.PreferenceUtil;
import epiboly.loner.photo.util.StringUtil;

/**
 * Created by loner on 2016/12/10.
 */

public class ImagePreviewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_img_preview);
        initUI();
        initParams();
        init();
    }

    private void initUI() {
        TextView submitTextView = (TextView) findViewById(R.id.tv_submit);

        submitTextView.setOnClickListener(this);
    }

    private void initParams() {
        imageView = (ImageView) findViewById(R.id.iv_preview);
    }

    private void init() {
        url = PreferenceUtil.getUrl(this);

        if (CameraConfig.cacheBitmap != null) {
            ImageShowTask imageShowTask = new ImageShowTask();
            imageShowTask.execute(CameraConfig.cacheBitmap);
        } else {
            ImageFromFileTask imageFromFileTask = new ImageFromFileTask();
            imageFromFileTask.execute(this);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_submit) {
            //上传照片
            //            qcApi.uploadPhoto(cache);
            setResult(RESULT_OK, new Intent());
            this.finish();
        }
    }

    class ImageFromFileTask extends AsyncTask<Activity, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Activity... activities) {
            if (url != null) {
                return FileUtil.getBitmap(url);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

    class ImageShowTask extends AsyncTask<byte[], Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(byte[]... bytes) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            return BitmapFactory.decodeByteArray(bytes[0], 0, bytes[0].length, options);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //回收static byte
        if (CameraConfig.cacheBitmap != null) {
            CameraConfig.cacheBitmap = null;
        }
    }

}
