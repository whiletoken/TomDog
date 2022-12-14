package bitbucket.neo.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

@Slf4j
public class OkHttp3Util {
    static volatile OkHttp3Util okHttpUtil;
    private final Request.Builder requestBuilder;
    private final OkHttpClient okHttpClient;

    private OkHttp3Util() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.addInterceptor(new RequestLoggerInterceptor())
                .addInterceptor(new ResponseLoggerInterceptor())
                .build();
        requestBuilder = new Request.Builder();//省的每次都new  request操作,直接builder出来,随后需要什么往里加,build出来即可
    }

    public static OkHttp3Util getInstance() {
        if (null == okHttpUtil) {
            synchronized (OkHttp3Util.class) {
                if (null == okHttpUtil) {
                    okHttpUtil = new OkHttp3Util();
                }
            }
        }
        return okHttpUtil;
    }

    public String httpGet(String url, Headers headers) {

        Request request = requestBuilder.url(url).headers(headers).build();

        Response response;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        String returnStr = null;
        if (response.isSuccessful()) {
            try {
                returnStr = StringEscapeUtils.unescapeJava(response.body().string());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            System.out.println(returnStr);
        }
        return returnStr;
    }

    public void httpPost(String urlString, RequestBody requestBody, Headers headers) {

        Request request = requestBuilder.url(urlString).method("POST", requestBody).headers(headers).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(StringEscapeUtils.unescapeJava(response.body().string()));
                }
            }
        });
    }

    public void httpPost(String urlString, RequestBody requestBody, Headers headers, Callback callback) {
        Request request = requestBuilder.url(urlString).method("POST", requestBody).headers(headers).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 接口用于回调数据
     */
    public interface ICallback {
        void invoke(String string);
    }

    /**
     * 请求拦截器
     */
    static class RequestLoggerInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            return chain.proceed(request);
        }
    }

    /**
     * 响应拦截器
     */
    static class ResponseLoggerInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());

            if (response.body() != null && response.body().contentType() != null) {
                MediaType mediaType = response.body().contentType();
                String string = response.body().string();
                ResponseBody responseBody = ResponseBody.create(mediaType, string);
                return response.newBuilder().body(responseBody).build();
            } else {
                return response;
            }
        }
    }
}