package epiboly.loner.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import epiboly.loner.photo.R;
import epiboly.loner.photo.control.ImagePreviewCallback;
import epiboly.loner.photo.util.ToastUtil;

/**
 * Created by loner on 2016/12/11.
 */

public class ImagePreviewAdapter extends BaseAdapter {

    public static final String PHOTO_NORMAL = "photo_normal";

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_PHOTO = 1;

    private ImagePreviewCallback mCallback;

    private Context mContext;
    private String[] urls;
    private List<String> imageUrls;
    private LayoutInflater mInflater;

    private int size;

    public ImagePreviewAdapter(Context context) {
        mContext = context;

        urls = new String[]{ImagePreviewAdapter.PHOTO_NORMAL, ImagePreviewAdapter.PHOTO_NORMAL, ImagePreviewAdapter.PHOTO_NORMAL, ImagePreviewAdapter.PHOTO_NORMAL,
                ImagePreviewAdapter.PHOTO_NORMAL, ImagePreviewAdapter.PHOTO_NORMAL, ImagePreviewAdapter.PHOTO_NORMAL, ImagePreviewAdapter.PHOTO_NORMAL};
        imageUrls = new ArrayList<>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    void setUrls() {
        for (int i = 0; i < urls.length; i++) {
            if (i < imageUrls.size()) {
                urls[i] = imageUrls.get(i);
            } else {
                urls[i] = PHOTO_NORMAL;
            }
        }
        notifyDataSetChanged();
    }

    public void addUrls(List<String> list) {
        if (imageUrls.size() + list.size() > 8) {
            ToastUtil.showToast(mContext, R.string.toast);
        } else {
            for (int i = 0; i < list.size(); i++) {
                imageUrls.add(list.get(i));
            }

            setUrls();
        }

        notifyDataSetChanged();
    }

    public List<String> getUrls() {
        return imageUrls;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public String getItem(int i) {
        return urls[i];
    }

    @Override
    public int getItemViewType(int position) {
        return (urls[position].equals(PHOTO_NORMAL)) ? TYPE_NORMAL : TYPE_PHOTO;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);

        if (type == TYPE_NORMAL) {
            view = mInflater.inflate(R.layout.list_item_preview, viewGroup, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_preview);

            if (i == 0) {
                imageView.setImageResource(R.drawable.bg_upload);
            } else {
                imageView.setImageResource(R.drawable.bg_add);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        mCallback.clickNormal();
                    }
                }
            });

            view.setTag(null);
        } else {
            ViewHold hold;
            view = mInflater.inflate(R.layout.list_item_delete, viewGroup, false);
            hold = new ViewHold(view);

            if (hold != null) {
                hold.deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(i);
                        if (mCallback != null) {
                            mCallback.clickDelete(imageUrls);
                        }
                    }
                });

                hold.bindData(getItem(i));
            }

        }
        return view;
    }

    public void setCallback(ImagePreviewCallback imagePreviewCallback) {
        mCallback = imagePreviewCallback;
    }

    private void delete(int positon) {
        imageUrls.remove(positon);

        setUrls();

        notifyDataSetChanged();
    }

    public void setWH(int w) {
        size = w;
    }

    class ViewHold {
        ImageView imageView;
        ImageView deleteImageView;

        ViewHold(View view) {
            imageView = (ImageView) view.findViewById(R.id.iv_preview);
            deleteImageView = (ImageView) view.findViewById(R.id.iv_delete);
        }

        void bindData(String url) {
            if (url != null) {
                File image = new File(url);

                Picasso.with(mContext)
                        .load(image)
                        .placeholder(R.drawable.default_error)
                        //        .error(R.drawable.default_error)
                        .resize(size, size)
                        .centerCrop()
                        .into(imageView);
            }
        }
    }
}
