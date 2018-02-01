package base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;


@SuppressLint("DefaultLocale")
public class IntentUtil {
    private final static HashMap<String, String> mimeMap;
    private final static String[][] MIME_MAP = {
            // {后缀名， MIME类型}
            {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/msword"}, {".xls", "application/msword"}, {".xlsx", "application/msword"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".prop", "text/plain"}, {".rar", "application/x-rar-compressed"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"},
            // {".xml", "text/xml"},
            {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/zip"}, {"", "*/*"}};

    static {
        mimeMap = new HashMap<String, String>();
        for (String[] strs : MIME_MAP) {
            mimeMap.put(strs[0], strs[1]);
        }
    }

    /**
     * 直接拨号
     *
     * @param number
     * @return
     */
    public static Intent getCallIntent(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        return it;
    }

    public static Intent getCaptureIntent(File srcFile, File targetFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(srcFile), "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:2
        intent.putExtra("output", Uri.fromFile(targetFile));
        intent.putExtra("outputFormat", "JPEG");//返回格式
        return intent;
    }

    /**
     * 跳转到拨号页面
     *
     * @param number
     * @return
     */
    public static Intent getDialIntent(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        return it;
    }

    /**
     * 直接拨打电话
     *
     * @param number
     * @return
     */
    public static Intent getDirectDialIntent(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        return it;
    }

    public static Intent getPicFromCameraIntent(String tmpPicFilePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(tmpPicFilePath);
        Uri u = Uri.fromFile(f);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        return intent;
    }

    /**
     * @return 从图库选择图片的Intent
     */
    public static Intent getPicIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * @param context
     * @return
     */
    public static String getPicPath(Context context, Intent intent) {
        Uri uri = intent.getData();
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(index);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 打开OFFIC文件
     */
    public static void openDoc(final File file, final Activity activity) {
        if (file.exists()) {
            String[] strs = file.getName().replace(".", "-.").split("-");
            String suffix = strs[strs.length - 1].toLowerCase();
            try {
                final Intent it = openFileIntent(file);
                if (".xls.xlsx.doc.docx。ppt.pptx。pdf".contains(suffix)) {
                    @SuppressLint("WrongConstant")
                    List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(it, PackageManager.GET_INTENT_FILTERS);
                    boolean resoved = false;
                    if (!list.isEmpty()) {
                        for (ResolveInfo r : list) {
                            //							if("cn.wps.moffice_eng".equals(r.resolvePackageName)){
                            //								resoved = true;
                            //								activity.startActivity(it);
                            //								break;
                            //							}
                            if ((r.match & IntentFilter.MATCH_CATEGORY_MASK) >= IntentFilter.MATCH_CATEGORY_TYPE && (r.match & IntentFilter.MATCH_ADJUSTMENT_MASK) >= IntentFilter.MATCH_ADJUSTMENT_NORMAL) {
                                if (!r.activityInfo.processName.startsWith("com.tencent")) {
                                    resoved = true;
                                    activity.startActivity(it);
                                    break;
                                }

                            }
                        }
                    }
                    if (!resoved) {
                        //						DialogUtil.getConfirmDialog("你可能需要安装Wps Office软件才能打开此文件，是否现在就去下载办公应用", activity, new ExRunnable() {
                        //							@Override
                        //							public void runWithExceptionCaught() {
                        //								try {
                        //									String str = "market://details?id=cn.wps.moffice_eng";
                        ////									String str = "market://search?q=cn.wps";
                        //									Intent localIntent = new Intent("android.intent.action.VIEW");
                        //									localIntent.setData(Uri.parse(str));
                        //									activity.startActivity(localIntent);
                        //								} catch (Exception e) {
                        //									ToastUtil.showToast(activity, "未找到相关市场应用", Toast.LENGTH_SHORT);
                        //								}
                        //
                        //							}
                        //						}, new ExRunnable() {
                        //							public void runWithExceptionCaught() {
                        ////								activity.startActivity(Intent.createChooser(it, "请选择处理此文件的应用"));
                        //							}
                        //						}).show();
                    }
                } else {
                    activity.startActivity(it);
                }

            } catch (Exception e) {
                Toast.makeText(activity, "没有应用可以打开此文件", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @return
     */
    public static Intent openFileIntent(File file) {
        Intent it = new Intent();
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setAction(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        return it;
    }

    private static String getMIMEType(File file) {
        String type = "*";
        String[] strs = file.getName().replace(".", "-.").split("-");
        String suffix = strs[strs.length - 1].toLowerCase();
        return mimeMap.get(suffix);
    }

    public static Intent openGpsIntent() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * @param filePath
     * @return
     */
    public static Intent openPicIntent(String filePath) {
        Intent it = new Intent();
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setAction(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
        return it;
    }

    /**
     * @param sms 短信内容
     * @return
     */
    public static Intent openSmsIntent(String phone, String sms) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", sms);
        intent.putExtra("address", phone);
        return intent;
    }

    public static Intent picContact() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        return intent;
    }
}
