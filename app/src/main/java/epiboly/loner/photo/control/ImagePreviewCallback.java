package epiboly.loner.photo.control;

import java.util.List;

/**
 * Created by loner on 2016/12/11.
 */

public interface ImagePreviewCallback {

    void clickNormal();

    void clickDelete(List<String> urls);
}
