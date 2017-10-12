package com.hgad.warehousemanager.net;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;

import java.io.IOException;


/**
 * Created by Administrator on 2016/7/24.
 */
public class NetUtil {

    public static <Res extends BaseResponse> void sendRequest(BaseRequest request, Class<Res> responseClass, Callback callback) {

            new NetTask().execute(new NetBean(request, responseClass, callback));
    }

    private static class NetBean {

        public NetBean(BaseRequest request, Class<? extends BaseResponse> responseClass, Callback callback) {
            this.request = request;
            this.responseClass = responseClass;
            this.callback = callback;
        }

        BaseRequest request;
        Callback callback;
        //响应结果对象的字节码
        Class<? extends BaseResponse> responseClass;

        //响应封装的对象
        BaseResponse baseReponse;
        //异常信息
        Exception exception;

        //错误信息
        ErrorResponseInfo errorResponseInfo;

    }

    //需要拿到Resuest,Callback
    private static class NetTask extends AsyncTask<NetBean, Void, NetBean> {

        @Override
        protected NetBean doInBackground(NetBean... params) {

            NetBean netBean = params[0];
            //执行网络请求
            try {

                String stringResponse = HttpWrapper.getInstance().getStringResponse(netBean.request);
                if (stringResponse != null) {

                Log.e("NetUtil", "获取的json数据:" + stringResponse);
//                    if (stringResponse.contains("nulldata")) {
//                        stringResponse = stringResponse.replaceAll("\"nulldata\":[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],", "");
//                        stringResponse = stringResponse.replaceAll("\"nulldata\":[\"\",\"\",\"\",\"\",\"\",\"\"],", "");
////                        stringResponse = stringResponse.replaceAll("\"nulldata\":[],", "");
//                    }
//                JSONObject jsonObject = new JSONObject(stringResponse);
//                String response = jsonObject.toString();
//                if (!"error".equals(response)) {

                    BaseResponse baseReponse = new Gson().fromJson(stringResponse, netBean.responseClass);
                    netBean.baseReponse = baseReponse;
                } else {
                    Log.d("NetUtil", "null");
////
//                    ErrorResponseInfo errorResponseInfo = new Gson().fromJson(stringResponse, ErrorResponseInfo.class);
//                    netBean.errorResponseInfo = errorResponseInfo;
                }

            } catch (IOException e) {
                e.printStackTrace();
                netBean.exception = e;
            } catch (JsonParseException e) {
                e.printStackTrace();
                netBean.exception = e;
            } catch (Exception e) {
                e.printStackTrace();
                netBean.exception = e;
            }
            return netBean;
        }

        //在主线程回调调用者的Callback对象
        @Override
        protected void onPostExecute(NetBean netBean) {

            if (netBean.exception != null) {

                netBean.callback.onError(netBean.request, netBean.exception);
            } else {

                if (netBean.errorResponseInfo != null) {

                    netBean.callback.onOther(netBean.request, netBean.errorResponseInfo);
                } else {

                    netBean.callback.onSuccess(netBean.request, netBean.baseReponse);
                }

            }
        }
    }


}
