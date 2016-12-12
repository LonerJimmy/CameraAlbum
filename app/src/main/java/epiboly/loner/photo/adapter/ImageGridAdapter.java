package epiboly.loner.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import epiboly.loner.photo.R;
import epiboly.loner.photo.constant.CameraConfig;
import epiboly.loner.photo.control.AdapterCallback;
import epiboly.loner.photo.model.Image;

/**
 * Created by loner on 2016/12/10.
 */

public class ImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;

    private LayoutInflater mInflater;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;
    private AdapterCallback mCallback;

    public ImageGridAdapter(Context context) {
        mContext = context;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }

    public void selectImages(AdapterCallback adapterCallback) {
        mCallback = adapterCallback;
    }


    /**
     * 通过图片路径设置默认选择
     *
     * @param resultList
     */
    public void setDefaultSelected(List<Image> resultList) {
        mSelectedImages = resultList;
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }


    /**
     * 设置数据集
     *
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();
        mImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;
        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return mImages.size() + 1;
    }

    @Override
    public Image getItem(int i) {
        if (i == 0) {
            return null;
        }
        return mImages.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 选择某个图片，改变选择状态
     *
     * @param image
     */
    private void select(Image image) {

        if (isContain(image)) {
            mSelectedImages.remove(image);
            notifyDataSetChanged();

            if (mCallback != null) {
                mCallback.selectPhotos(mSelectedImages);
            }
        } else if (mSelectedImages.size() < CameraConfig.PHOTO_SELECT_NUM) {
            mSelectedImages.add(image);
            notifyDataSetChanged();

            if (mCallback != null) {
                mCallback.selectPhotos(mSelectedImages);
            }
        } else {
            if (mCallback != null) {
                mCallback.exceedPhotos();
            }
        }


    }

    private boolean isContain(Image image) {
        for (int i = 0; i < mSelectedImages.size(); i++) {
            if (mSelectedImages.get(i).equals(image)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);
        if (type == TYPE_CAMERA) {
            view = mInflater.inflate(R.layout.list_item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHold hold;
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                hold = new ViewHold(view);

            } else {
                hold = (ViewHold) view.getTag();
                if (hold == null) {
                    view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                    hold = new ViewHold(view);
                }
            }
            if (hold != null) {
                hold.indicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select(getItem(i));
                    }
                });
                hold.bindData(getItem(i));
            }
        }

        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != mItemSize) {
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHold {
        ImageView image;
        ImageView indicator;

        ViewHold(View view) {
            image = (ImageView) view.findViewById(R.id.photo_img_view);
            indicator = (ImageView) view.findViewById(R.id.ic_check);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;
            // 处理单选和多选状态
            indicator.setVisibility(View.VISIBLE);
            if (mSelectedImages.contains(data)) {
                // 设置选中状态
                indicator.setImageResource(R.drawable.ic_photo_select);
            } else {
                // 未选择
                indicator.setImageResource(R.drawable.ic_photo_unselect);
            }

            File imageFile = new File(data.path);

            if (mItemSize > 0) {
                Picasso.with(mContext)
                        .load(imageFile)
                        .placeholder(R.drawable.default_error)
                        .resize(mItemSize, mItemSize)
                        .centerCrop()
                        .into(image);
            }
        }
    }

}
