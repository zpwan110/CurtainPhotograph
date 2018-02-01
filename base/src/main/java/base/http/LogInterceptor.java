package base.http;


import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import jingshu.com.base.BuildConfig;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Jack on 2016/1/14.
 */
public class LogInterceptor implements Interceptor {
//    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Logger logger;

    public LogInterceptor() {
        this(Logger.DEFAULT);
    }

    public LogInterceptor(Logger logger) {
        this.logger = logger;
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        Request request = chain.request();
        RequestBody requestBody = request.body();

        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "Request: " + protocol(protocol) + ' ' + request.method() + ' ' + request.url();
        stringBuffer.append(requestStartMessage + "\n");

        stringBuffer.append("RequestHeaders: ");
        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                stringBuffer.append("Content-Type:" + requestBody.contentType());
            }
            if (requestBody.contentLength() != -1) {
                stringBuffer.append(" Content-Length:" + requestBody.contentLength());
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                stringBuffer.append(" " + name + ":" + headers.value(i));
            }
        }
        if (hasRequestBody) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            stringBuffer.append("\nRequestBody:" + buffer.readString(charset));
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        stringBuffer.append("\nResponse: " + response.code() + ' ' + response.message() + ' ' + " (" + tookMs + "ms");

        stringBuffer.append("\nResponseHeaders: ");
        headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            stringBuffer.append(headers.name(i) + ": " + headers.value(i));
        }

        if (responseBody.contentLength() != 0) {
            stringBuffer.append("\nResponseBody: ");
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (contentLength != 0) {
                stringBuffer.append("");
                stringBuffer.append(buffer.clone().readString(charset));
            }
        }
        if(BuildConfig.DEBUG){
            logger.log(stringBuffer.toString());
        }
        return response.newBuilder()
                .body(ResponseBody.create(responseBody.contentType(), responseBody.string()))
                .build();
    }

    public interface Logger {
        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Log.e("XX", message);
            }
        };

        void log(String message);
    }
}