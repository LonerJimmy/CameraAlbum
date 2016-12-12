package epiboly.loner.photo.control;

import java.util.List;

import epiboly.loner.photo.model.Image;

/**
 * Created by loner on 2016/12/11.
 */

public interface AdapterCallback {
    void selectPhotos(List<Image> images);
    void exceedPhotos();
}
