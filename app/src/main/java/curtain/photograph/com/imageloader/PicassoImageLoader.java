package curtain.photograph.com.imageloader;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import base.util.LogUtil;


public class PicassoImageLoader extends ImageLoader {
    public  void displayImage(Context context, Object path, ImageView imageView) {
        if(TextUtils.isEmpty(path.toString())){
                return;
        }
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        if(path instanceof Uri){
            Picasso.with(context).load((Uri) path).into(imageView);
        }else if(path instanceof File){
            Picasso.with(context).load((File)path).into(imageView);
        }else if(path instanceof String){
            Picasso.with(context).load((String)path).into(imageView);
        }else if(path instanceof Integer){
            Picasso.with(context).load((Integer) path).into(imageView);
        }else{
            try {
                throw new Exception();
            } catch (Exception e) {
                LogUtil.e("cannot find classes by path!");
            }
        }
    }

}
