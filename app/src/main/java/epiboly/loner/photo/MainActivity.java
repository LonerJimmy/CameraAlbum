package epiboly.loner.photo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import epiboly.loner.photo.adapter.ImagePreviewAdapter;
import epiboly.loner.photo.components.BaseActivity;
import epiboly.loner.photo.constant.CameraConfig;
import epiboly.loner.photo.control.ImagePreviewCallback;
import epiboly.loner.photo.ui.photo.PhotoSelectActivity;
import epiboly.loner.photo.util.DimenUtil;
import epiboly.loner.photo.util.PreferenceUtil;
import epiboly.loner.photo.util.StringUtil;

/**
 * Created by loner on 2016/12/10.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, ImagePreviewCallback {

    private DimenUtil dimenUtil;
    private GridView mGridView;
    private ImagePreviewAdapter imagePreviewAdapter;

    public static final int PhotoSelectRequestCode = 88;

    public static final String SELECT_URLS = "select_urls";

    public List<String> displayUrls=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        dimenUtil = DimenUtil.getInstance(this);
        mGridView = (GridView) findViewById(R.id.gv_main);

        imagePreviewAdapter = new ImagePreviewAdapter(this);

        imagePreviewAdapter.setWH((int) dimenUtil.dip2px(70));
        imagePreviewAdapter.setCallback(this);

        mGridView.setAdapter(imagePreviewAdapter);

        findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_sure) {
            if (displayUrls.size() > 0) {
                Log.e("loner", "url=" + displayUrls.toString());
            }
        } else {
            Intent intent = new Intent(this, PhotoSelectActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void clickNormal() {
        startActivityForResult(new Intent(this, PhotoSelectActivity.class), PhotoSelectRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //获取选择的图片
            ArrayList<String> list = data.getStringArrayListExtra(SELECT_URLS);
            if (list != null) {
                imagePreviewAdapter.addUrls(list);

                displayUrls = imagePreviewAdapter.getUrls();
            }
        }
    }

    @Override
    public void clickDelete(List<String> urls) {
        displayUrls = urls;
    }
}
