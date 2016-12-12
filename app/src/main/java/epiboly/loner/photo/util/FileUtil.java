package epiboly.loner.photo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import epiboly.loner.photo.constant.CameraConfig;

/**
 * Created by loner on 2016/12/10.
 */

public class FileUtil {
    private FileUtil() {
    }

    private static File getImgFile(Context context, String id) {
        String state = Environment.getExternalStorageState();
        File pic;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!pic.isDirectory()) {
                pic.mkdirs();
            }
        } else {
            pic = context.getCacheDir();
        }
        return new File(pic, CameraConfig.PHOTO_SAVE_PATH + "_" + id + ".jpg");
    }

    public static String getFilePath(Context context, String id) {
        try {
            return getImgFile(context, id).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveBitmap(byte[] b, String url) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(url);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(String url) {
        Bitmap bitmap;
        try {
            FileInputStream fileInputStream = new FileInputStream(url);
            BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);
            bitmap = BitmapFactory.decodeStream(inputStream);
            fileInputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    private static boolean deletePhoto(String url) {
        if (StringUtil.isNotBlank(url)) {
            File file = new File(url);
            if (file.isFile() && file.exists()) {
                return file.delete();
            }
        }

        return false;
    }

    /**
     * 删除图片
     *
     * @return
     */
    static void deletePhoto(List<String> urls) {
        if (urls != null && urls.size() > 0) {
            for (int i = 0; i < urls.size(); i++) {
                deletePhoto(urls.get(i));
            }
        }
    }
}
