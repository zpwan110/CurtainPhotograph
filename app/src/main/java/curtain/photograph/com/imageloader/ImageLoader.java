package curtain.photograph.com.imageloader;

import android.content.Context;
import android.widget.ImageView;


public class ImageLoader{
    private static PicassoImageLoader instance;
    public static synchronized PicassoImageLoader getInstance() {
             if (instance == null) {
                     instance = new PicassoImageLoader();
                 }
             return instance;
             }

    protected ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
