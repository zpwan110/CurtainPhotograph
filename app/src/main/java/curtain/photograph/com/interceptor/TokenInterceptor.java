package curtain.photograph.com.interceptor;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import base.http.net.HttpClient;
import curtain.photograph.com.utils.AppSharePerference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Token 失效时拦截
 * Created by zpwan110 on 2017/4/8.
 */

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(addToken(request));
        return tokenTimeOut(response, request, chain);
    }

    /**
     * 添加token
     *
     * @param request
     * @return
     */
    private Request addToken(Request request) {
        String headers = request.header("Authorization");
        if (TextUtils.isEmpty(headers) && !TextUtils.isEmpty(AppSharePerference.getUserToken())) {
            Request authorised = request.newBuilder()
                    .addHeader("Authorization", "bearer " + AppSharePerference.getUserToken())
                    .addHeader("Accept-Encoding", "")
                    .build();
            return authorised;
        }
        Request authorised = request.newBuilder()
                .addHeader("Accept-Encoding", "")
                .build();
        return authorised;
    }

    /**
     * token 过期刷新
     *
     * @param response
     * @param chain
     */
    private Response tokenTimeOut(Response response, final Request request, final Chain chain) throws IOException {
        ResponseBody responseBody = response.body();
        String dataJson = responseBody.string();
        if (response.isSuccessful()) {
            JSONObject obj = JSONObject.parseObject(dataJson);
            if (obj.containsKey("errcode") && obj.getIntValue("errcode") > 0) {
                Response refResponse = refToken(obj.getIntValue("errcode"),request);
                if(refResponse!=null){
                    return refResponse;
                }
            }
        }
        return response.newBuilder()
                .body(ResponseBody.create(responseBody.contentType(), dataJson))
                .build();
    }

    /**
     * 刷新Token
     * @param errcode
     * @param request
     * @return
     * @throws IOException
     */
    private Response refToken(int errcode,Request request) throws IOException {
//        if (errcode == 40001) {
//            //access_token失效，需重新刷新token
//            Response responseRef = ApplicationApi.getAsyncRefrenshToken(APPSharePerference.getUserToken().access_token, APPSharePerference.getUserToken().refresh_token).execute();
//            ResponseBody responseRefBody = responseRef.body();
//            if (responseRef.isSuccessful()) {
//                String refTokenStr = "";
//                try {
//                    refTokenStr = responseRefBody.string();
//                    ResponseBodyJson responseBodyJson = JSON.parseObject(refTokenStr, ResponseBodyJson.class);
//                    if (responseBodyJson.errcode == 0) {
//                        APPSharePerference.saveUserToken(responseBodyJson.data);
//                        if(request.url().url().equals(ServerConfig.getNewToken())){
//                            return responseRef.newBuilder()
//                                    .body(ResponseBody.create(responseRefBody.contentType(), responseRefBody.string()))
//                                    .build();
//                        }
//                        return againRequest(request);
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//            return responseRef.newBuilder()
//                    .body(ResponseBody.create(responseRefBody.contentType(), responseRefBody.string()))
//                    .build();
//        } else if (errcode == 40002) {
//            //refresh_token过期，需用户重新登录
//            BroadCastReceiverUtil.sendBroadcast(App.getInstance(),
//                    BroadcastConstants.BROADCAST_LOGIN_SUCCESS);
//            return null;
//        }
        return null;
    }

    /**
     * 重新请求
     * @param request
     * @return
     * @throws IOException
     */
    private Response againRequest(Request request) throws IOException {
        return HttpClient.getHttpClient().newCall(addToken(request)).execute();
    }
}
