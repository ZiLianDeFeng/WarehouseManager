package com.hgad.warehousemanager.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/7/24.
 * <p/>
 * <p/>
 * Http的封装对象：可以封装OkHttp,NoHttp,HttpClient,HttpUrlConnection
 */
public class HttpWrapper {

    private final OkHttpClient okHttpClient;

    private HttpWrapper() {

        okHttpClient = new OkHttpClient();
    }

    private static HttpWrapper sInstance = new HttpWrapper();

    public static HttpWrapper getInstance() {

        return sInstance;
    }

    public InputStream getInputStreamResponse(BaseRequest request) throws IOException {

        ResponseBody responseBody = getResponseBody(request);
        return responseBody.byteStream();
    }

    public byte[] getBytesResponse(BaseRequest request) throws IOException {

        ResponseBody responseBody = getResponseBody(request);
        return responseBody.bytes();
    }

    public String getStringResponse(BaseRequest request) throws IOException {

        ResponseBody responseBody = getResponseBody(request);
        return responseBody.string();
    }

    //返回json字符流
    public Reader getReaderResponse(BaseRequest request) throws IOException {
        ResponseBody responseBody = getResponseBody(request);
        return responseBody.charStream();
    }

    private ResponseBody getResponseBody(BaseRequest request) throws IOException {

        Request.Builder builder = new Request.Builder();

        //是否缓存请求数据
        if (request.noCache()) {

            CacheControl.Builder cacheControlBuilder = new CacheControl.Builder();
            cacheControlBuilder.noCache();
            builder.cacheControl(cacheControlBuilder.build());
        }

        Map<String, String> headers = request.getHeaders();
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (request.getHttpMethod() == BaseRequest.HttpMethod.GET) {

            StringBuilder stringBuilder = new StringBuilder(request.getUrl());
            Map<String, String> params = request.getParams();
            if (params != null && params.size() > 0) {
                stringBuilder.append("?");
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            builder.get().url(stringBuilder.toString());
        } else if (request.getHttpMethod() == BaseRequest.HttpMethod.POST) {
            FormBody.Builder builder1 = new FormBody.Builder();
            Map<String, String> params = request.getParams();
            if (params != null && params.size() > 0) {
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    builder1.addEncoded(entry.getKey(), entry.getValue());

                }
            }
            builder.post(builder1.build()).url(request.getUrl());
        }
        return okHttpClient.newCall(builder.build()).execute().body();
    }

}
