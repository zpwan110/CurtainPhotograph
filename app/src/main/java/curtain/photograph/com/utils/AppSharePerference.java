package curtain.photograph.com.utils;

import base.util.SharePerferenceUtil;
import curtain.photograph.com.common.config.AppConfig;

/**
 *
 * @author zpwan110
 * @date 2017/4/6
 */

public class AppSharePerference extends SharePerferenceUtil {
    private static final String USER_TOKEN = "user_token";
    private static final String USER_INFO = "user_info";
    private static final String USER_ID = "user_id";
    private static final String LAST_ACCOUNT = "last_account";
    private static final String LAUNCH_COUNT = "LAUNCH_COUNT";
    private static final String LAUNCH_VERSION = "LAUNCH_VERSION";
    private static final String IS_APP_GUIDE_SHOWED = "IS_APP_GUIDE_SHOWED";
    /**
     * 存储用户信息
     */
    public static void saveUserInfo(String userInfo) {
            put(USER_INFO,userInfo);
    }
   /* public static UserInfo getUserInfo() {
        if (AppSharePerference.mContext==null)
        {
            throw new IllegalArgumentException("mApplication can not  be null");
        }else {
            return JSON.parseObject((String) get(AppSharePerference.mContext, USER_INFO, "{}"), UserInfo.class) ;
        }
    }*/
    /**
     * 存储UserToken
     * @param userToken
     * @return
     */
    public static void saveUserToken(String userToken) {
            put(USER_TOKEN,userToken);
    }

    /**
     * 获取token
     * @param
     */
    public static String getUserToken() {
            return String.valueOf(get(USER_TOKEN,""));
    }
    /**
     * 清除UserToken
     * @return
     */
    public static void deleteUserToken() {
       AppSharePerference.removeAppShare(USER_TOKEN);
    }
    public static void removeAppShare(String key){
           remove(key);
    }

    public static void saveLastAccount(String account) {
        put(LAST_ACCOUNT,account);
    }
    public static String getLastAccount() {
           return String.valueOf(get(LAST_ACCOUNT,""));
    }

    public static String getUserId() {
            return (String) get(USER_ID,"");
    }
    public static void saveUserId(String userId) {
            put(USER_ID,userId);
    }

    /**
     * 保存当前版本号
     * @param nowVersionCode
     */
    public static void saveVersionCode(int nowVersionCode) {
            put(LAUNCH_VERSION,nowVersionCode);
    }

    /**
     * 获取老的版本号
     */
    public static int getOldVersionCode() {
            return (int) get(LAUNCH_VERSION,0);
    }

    /**
     * 是否显示引导页
     * @return
     */
    public static boolean getGuideShow(){
            return (boolean) get(IS_APP_GUIDE_SHOWED,true);
    }

    /**
     *
     * @param versionCode
     * @return
     */
    public static void saveGuideShow(boolean versionCode){
            put(IS_APP_GUIDE_SHOWED,versionCode);
    }
    /**
     * 获取当前版本启动次数
     */
    public static int getLaunchCount(int versionCode){
            return (int) get(LAUNCH_COUNT+versionCode,0);
    }

    /**
     * 记录当前版本启动次数
     */
    public static void setLaunchCount(){
        int count  = getLaunchCount(AppConfig.getPackageInfo().versionCode);
        put(LAUNCH_COUNT+AppConfig.getPackageInfo().versionCode,count+1);
    }

    public static void clearLaunch(int versionCode){
            put(LAUNCH_VERSION,versionCode);
            put(LAUNCH_COUNT+versionCode,0);
    }

}
