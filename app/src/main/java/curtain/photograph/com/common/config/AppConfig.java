package curtain.photograph.com.common.config;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

import base.util.LogUtil;
import curtain.photograph.com.base.App;
import curtain.photograph.com.utils.AppSharePerference;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author zhangpeng
 *
 */
public class AppConfig {
    public final static String RELEASE = "release";
    public final static String DEBUG = "debug";
    public final static String TEST = "test";
    public static final String PORT_TYPE = DEBUG;

    /**
     * 判断Service是否在运行
     * @param mContext
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> serviceList = activityManager.getRunningAppProcesses();

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).getClass().getName().equals(className) == true) {
                isRunning = true;
                break;
            }
            LogUtil.e("类名:"+serviceList.get(i).getClass().getName());
        }
        return isRunning;
    }

    /**
     * 判断app是否打开运行状态？
     * @param context
     * @return
     */
    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }
    /**
     * 版本名不一致时需要显示划屏引导页面（三种情况：新安装，数据被清空）
     */
    public static boolean isAppGuideShowed() {
        return  AppSharePerference.getGuideShow();
    }

    public static void setIsAppGuideShowed(boolean isAppFirstStart) {
        AppSharePerference.saveGuideShow(isAppFirstStart);
    }

    /**
     * 判断当前版本是否是第一次启动
     * @return
     */
    public static boolean isFirstLaunch(){
        int nowVersionCode = getPackageInfo().versionCode;
        if(nowVersionCode> AppSharePerference.getOldVersionCode()){
            if(AppSharePerference.getLaunchCount(nowVersionCode)!=0){
                AppSharePerference.saveVersionCode(nowVersionCode);
                return false;
            }else{
                AppSharePerference.setLaunchCount();
                return true;
            }
        }
        return false;
    }
    public static PackageInfo getPackageInfo(){
        if(null==App.getContext()) {
            throw new NullPointerException(" the context for Application cannot be null!");
        }
        PackageInfo pi = null;
        try {
            PackageManager pm = App.getContext().getPackageManager();
            pi = pm.getPackageInfo(App.getContext().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}
