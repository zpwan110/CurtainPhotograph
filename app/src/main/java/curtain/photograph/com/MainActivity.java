package curtain.photograph.com;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import base.BaseActivity;
import base.PermissionActivity;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.ToastUtil;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_IMAGE = 0;
    private static final int REQUEST_CODE_CAMERA = 1;
    @Id(R.id.takePhoto)
    TextView takePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Click(R.id.takePhoto)
    private void takePic(){
        PermissionActivity.requestPermission((Activity) activity, new String[]{Manifest.permission.CAMERA}, "需要授权使用摄像头", new PermissionActivity.OnPermissionCallback() {
            @Override
            public void onPermissionAuthenticated() {
                ToastUtil.showToast("授权成功",activity);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
            @Override
            public void onPermissionDenied() {
                ToastUtil.showToast("授权失败",activity);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//                ImageLoader.getInstance().displayImage(activity,path,ivPhoto);
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
            Bitmap bitmap = (Bitmap) bundle.get("data");
        }

    }
}
