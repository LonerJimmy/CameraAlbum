package epiboly.loner.photo.constant;

/**
 * Created by loner on 2016/12/10.
 */

public class CameraConfig {

    public interface CAMERA {
        int FRONT_CAMERA = 1;
        int REAR_CAMERA = 0;
    }

    public static byte[] cacheBitmap;

    public static String PHOTO_SAVE_PATH = "loner_upload_image";

    public static String PHOTO_SAVE_PATHS = "loner_upload_images";

    public static String PREFERENCE_URL="loner_preference";

    public static final int PHOTO_SELECT_NUM = 8;

    public static final String SEPARATOR = " ";

    public static final int LOADER_ALL = 0;

    public static final int SHOULD_FINISH_REQUEST_CODE = 1;
}
