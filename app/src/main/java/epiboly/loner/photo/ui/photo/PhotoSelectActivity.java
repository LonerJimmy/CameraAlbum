package epiboly.loner.photo.ui.photo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import epiboly.loner.photo.MainActivity;
import epiboly.loner.photo.R;
import epiboly.loner.photo.adapter.ImageGridAdapter;
import epiboly.loner.photo.components.BaseActivity;
import epiboly.loner.photo.constant.CameraConfig;
import epiboly.loner.photo.control.AdapterCallback;
import epiboly.loner.photo.model.Image;
import epiboly.loner.photo.ui.camera.CameraActivity;
import epiboly.loner.photo.util.PreferenceUtil;
import epiboly.loner.photo.util.ToastUtil;

/**
 * Created by loner on 2016/12/10.
 */

public class PhotoSelectActivity extends BaseActivity implements View.OnClickListener, AdapterCallback {

    private List<Image> resultList = new ArrayList<>();
    private TextView nextButton;
    private TextView numTextView;
    private GridView mGridView;
    private TextView cancelTextView;
    // 预览按钮
    private TextView previewTextView;
    private ImageGridAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);

        initView();
        initGridView();
        initOthers();

        //显示所有图片
        getSupportLoaderManager().initLoader(CameraConfig.LOADER_ALL, null, mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == CameraConfig.LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mActivity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");

                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {

                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        File file = new File(path);
                        if (file.exists()) {
                            images.add(image);
                        }

                    } while (data.moveToNext());

                    mImageAdapter.setData(images);

                    // 设定默认选择
                    if (resultList != null && resultList.size() > 0) {
                        mImageAdapter.setDefaultSelected(resultList);
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void initOthers() {

        previewTextView.setOnClickListener(this);

        // 返回按钮
        findViewById(R.id.tv_cancel).setOnClickListener(this);

        if (resultList == null || resultList.size() <= 0) {
            setSelected(false);
        } else {
            setSelected(true);
        }

        nextButton.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);
    }

    private void initView() {
        nextButton = (TextView) findViewById(R.id.btn_next);
        numTextView = (TextView) findViewById(R.id.tv_num);
        previewTextView = (TextView) findViewById(R.id.tv_preview);
        mGridView = (GridView) findViewById(R.id.grid);
        cancelTextView = (TextView) findViewById(R.id.tv_cancel);
    }

    private void initGridView() {
        mImageAdapter = new ImageGridAdapter(this);

        mImageAdapter.selectImages(this);

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {

                final Picasso picasso = Picasso.with(mActivity);
                if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(this);
                } else {
                    picasso.pauseTag(this);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        mGridView.setAdapter(mImageAdapter);

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();

                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace * (2)) / 3;
                mImageAdapter.setItemSize(columnWidth);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //选择相机
                    showCameraAction();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(PhotoSelectActivity.this, PhotoDisplayActivity.class);
                    ArrayList<String> strList = new ArrayList<String>();
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    strList.add(image.path);

                    intent.putExtra(PhotoDisplayActivity.PHOTO_DISPLAY_FLAG, PhotoDisplayActivity.PHOTO_DISPLAY_ONE);
                    intent.putExtra(PhotoDisplayActivity.PHOTO_DISPLAY_URL, strList);
                    intent.putExtra(PhotoDisplayActivity.PHOTO_DISPLAY_POSITION, i);
                    startActivity(intent);
                    // 单张图片预览
                        /*
                        Intent intent1 = new Intent();
                        intent1.setClass(PhotoSelectFragment.this, PhotoDisplayActivity.class);*/

                    // selectImageFromGrid(image, mode);
                }
            }
        });
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private ArrayList<String> getUrls(List<Image> images) {
        ArrayList<String> urls = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            urls.add(images.get(i).path);
        }
        return urls;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next) {
            if (resultList != null && resultList.size() > 0) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(MainActivity.SELECT_URLS, getUrls(resultList));
                setResult(RESULT_OK, intent);
                finish();
//                PreferenceUtil.saveUrls(this, getUrls(resultList));
//                finish();
            }
        } else if (view.getId() == R.id.tv_cancel) {
            finish();
        } else if (view.getId() == R.id.tv_preview) {
            //预览
            if (resultList != null && resultList.size() > 0) {
                Intent intent = new Intent();
                intent.setClass(PhotoSelectActivity.this, PhotoDisplayActivity.class);
                ArrayList<String> strList = new ArrayList<String>();
                for (int x = 0; x < resultList.size(); x++) {
                    strList.add(x, resultList.get(x).path);
                }
                intent.putExtra(PhotoDisplayActivity.PHOTO_DISPLAY_FLAG, PhotoDisplayActivity.PHOTO_DISPLAY_MULTI);
                intent.putExtra(PhotoDisplayActivity.PHOTO_DISPLAY_URL, strList);
                startActivity(intent);
            }
        }

    }

    @Override
    public void selectPhotos(List<Image> images) {
        if (images != null) {
            resultList = images;
            if (resultList.size() == 0) {
                setSelected(false);
            } else if (resultList.size() <= CameraConfig.PHOTO_SELECT_NUM) {
                setSelected(true);
            }
        }
    }

    private void setSelected(boolean b) {
        if (b) {
            previewTextView.setBackgroundResource(R.drawable.bg_preview);
            numTextView.setVisibility(View.VISIBLE);
            numTextView.setText(resultList.size() + "");
        } else {
            previewTextView.setBackgroundResource(R.drawable.bg_not_preview);

            numTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void exceedPhotos() {
        ToastUtil.showToast(this, R.string.toast);
    }
}
