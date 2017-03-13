package com.yyjlr.tickets.service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.helputils.SharePrefUtil;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by Elvira on 2016/12/2.
 * 网络请求
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    public static final MediaType JSON = MediaType.parse("application/json;CharSet=UTF-8");

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    //上传
    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final List<File> files, final TypeReference typeReference, Context context) {
        getInstance()._postAsyn(url, callback, requestMainDataData, files, typeReference, context);
    }

//    //上传
//    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final List<File> files, final Class<T> resultVOClass, Context context) {
//        getInstance()._postAsyn(url, callback, requestMainDataData, files, resultVOClass, context);
//    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass, Context context) {
        getInstance()._postAsyn(url, callback, requestMainDataData, resultVOClass, context);
    }

    //特殊返回 兑换券支付
    public static <T> void postAsyn(String url, final ResultVoucherCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass, Context context) {
        getInstance()._postAsyn(url, callback, requestMainDataData, resultVOClass, context);
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final TypeReference typeReference, Context context) {
        getInstance()._postAsyn(url, callback, requestMainDataData, typeReference, context);
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, Context context) {
        getInstance()._postAsyn(url, callback, requestMainDataData, context);
    }

    //异步请求 类(返回普通类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass, Context context) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", context);
        String deviceId = SharePrefUtil.getString(Constant.FILE_NAME, "deviceId", "", context);

        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        if (!"".equals(deviceId)) {
            requestData.setAppPushToken(deviceId);
        }
        //影院ID
        requestData.setAppDomain(Constant.AppDomain);
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        requestData.setAppVersion(Constant.AppVersion);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, resultVOClass, context);
    }

    //异步请求 类(返回普通类) 特殊返回 兑换券支付
    private <T> void _postAsyn(String cmd, final ResultVoucherCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass, Context context) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", context);
        String deviceId = SharePrefUtil.getString(Constant.FILE_NAME, "deviceId", "", context);

        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        if (!"".equals(deviceId)) {
            requestData.setAppPushToken(deviceId);
        }
        //影院ID
        requestData.setAppDomain(Constant.AppDomain);
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        requestData.setAppVersion(Constant.AppVersion);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, resultVOClass, context);
    }

    //异步请求 类(返回普通类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, Context context) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", context);
        String deviceId = SharePrefUtil.getString(Constant.FILE_NAME, "deviceId", "", context);

        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        if (!"".equals(deviceId)) {
            requestData.setAppPushToken(deviceId);
        }
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        requestData.setAppVersion(Build.BRAND + "_" + Build.DISPLAY + "_" + Build.FINGERPRINT + "_" + Build.ID);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request);
    }

    //异步请求 类(返回普通类) 上传专用
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final List<File> files, final TypeReference typeReference, Context context) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", context);

        String json = new Gson().toJson(requestMainDataData);
        deliveryResult(callback, token, cmd, files, json, typeReference);
    }

//    //异步请求 类(返回普通类) 上传专用
//    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final List<File> files, final Class<T> resultVOClass, Context context) {
//        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", context);
//
//        String json = new Gson().toJson(requestMainDataData);
//        deliveryResult(callback, token, cmd, files, json, resultVOClass);
//    }

    //异步请求 类(返回集合类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final TypeReference typeReference, Context context) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, "token", "", context);

        String deviceId = SharePrefUtil.getString(Constant.FILE_NAME, "deviceId", "", context);
        RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        if (!"".equals(deviceId)) {
            requestData.setAppPushToken(deviceId);
        }
        Locale curLocal = Application.getInstance().getResources().getConfiguration().locale;
        if (curLocal.equals(Locale.SIMPLIFIED_CHINESE)) {
            requestData.setLanguage("cn");
        } else {
            requestData.setLanguage("en");
        }
        requestData.setAppVersion(Constant.AppVersion);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        requestData.setCmd(cmd);
        //影院ID
        requestData.setAppDomain(Constant.AppDomain);
        requestData.setParameters(requestMainDataData);
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, typeReference);
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }


    private <T> Request buildPostRequest(String url, RequestData requestData) {

        String json = new Gson().toJson(requestData);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Log.v("xxxxxx", json);
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    public static class Param {
        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

    //上传图片和视频
    private <T> void deliveryResult(final ResultCallback callback, final String token, final String cmd, final List<File> files, String json, final TypeReference typeReference) {
        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);//表单形式
        builder.addFormDataPart("cmd", cmd);
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("parameters", json);
        for (int i = 0; i < files.size(); i++) {
            builder.addFormDataPart("files", files.get(i).getName(), RequestBody.create(null, files.get(i)));
        }
        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .url(Config.URL_SERVICE_UPLOAD)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    JsonNode node = mapper.readValue(string, JsonNode.class);

                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        T result = mapper.readValue(responseNode.toString(), typeReference);
                        sendSuccessResultCallback(result, callback);
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

//    //上传图片和视频
//    private <T> void deliveryResult(final ResultCallback callback, final String token, final String cmd, final List<File> files, String json, final Class<T> resultVOClass) {
//        MultipartBuilder builder = new MultipartBuilder();
//        builder.type(MultipartBuilder.FORM);//表单形式
//        builder.addFormDataPart("cmd", cmd);
//        builder.addFormDataPart("token", token);
//        builder.addFormDataPart("parameters", json);
//        for (int i = 0; i < files.size(); i++) {
//            builder.addFormDataPart("files", files.get(i).getName(), RequestBody.create(null, files.get(i)));
//        }
//        RequestBody requestBody = builder.build();
//        final Request request = new Request.Builder()
//                .url(Config.URL_SERVICE_UPLOAD)
//                .post(requestBody)
//                .build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(final Request request, final IOException e) {
//                sendFailedStringCallback(request, e, callback);
//            }
//
//            @Override
//            public void onResponse(final Response response) {
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                try {
//                    final String string = response.body().string();
//                    JsonNode node = mapper.readValue(string, JsonNode.class);
//
//                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
//                    if (statusCode == 0) {
//                        JsonNode responseNode = node.get("response");
//                        T data = mapper.treeToValue(responseNode, resultVOClass);
//                        sendSuccessResultCallback(data, callback);
//                    } else {
//                        JsonNode errorNode = node.get("error");
//                        Error error = mapper.treeToValue(errorNode, Error.class);
//                        if (401 == statusCode) {
//                            showExitDialog(error.getInfo());
//                        }
//                        sendErrorStringCallback(request, error, callback);
//                    }
//                } catch (IOException e) {
//                    sendFailedStringCallback(response.request(), e, callback);
//                }
//            }
//        });
//    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request, final Class<T> resultVOClass, Context context) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        if (resultVOClass == ResponseData.class) {//只判断状态(只确定返回是否成功)
                            ResponseData responseData = new ResponseData();
                            responseData.setStatusCode(0);
                            sendSuccessResultCallback(responseData, callback);
                        } else {
                            JsonNode responseNode = node.get("response");
                            T data = mapper.treeToValue(responseNode, resultVOClass);
                            sendSuccessResultCallback(data, callback);
                        }
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    //特殊返回 兑换券支付
    private <T> void deliveryResult(final ResultVoucherCallback callback, final Request request, final Class<T> resultVOClass, Context context) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        if (resultVOClass == ResponseData.class) {//只判断状态(只确定返回是否成功)
                            ResponseData responseData = new ResponseData();
                            responseData.setStatusCode(0);
                            sendSuccessResultCallback(responseData, callback);
                        } else {
                            JsonNode responseNode = node.get("response");
                            T data = mapper.treeToValue(responseNode, resultVOClass);
                            sendSuccessResultCallback(data, callback);
                        }
                    } else {
                        JsonNode errorNode = node.get("error");
                        VoucherError error = mapper.treeToValue(errorNode, VoucherError.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        sendSuccessResultCallback(responseNode.toString(), callback);
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request, final TypeReference typeReference) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        T result = mapper.readValue(responseNode.toString(), typeReference);
                        sendSuccessResultCallback(result, callback);
                    } else {
                        JsonNode errorNode = node.get("error");
//                        JsonNode errorNodeCode = mapper.readValue(errorNode.toString(), JsonNode.class);
//
//                        if (errorNodeCode.equals("101")) {
//                            VoucherError voucherError = mapper.treeToValue(errorNode, VoucherError.class);
//                            sendErrorStringCallback(request, voucherError, callback);
//                        } else {

                        Error error = mapper.treeToValue(errorNode, Error.class);
//                        Error error = mapper.readValue(errorNode.toString(), Error.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
//                        }
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onOtherError(request, e);
            }
        });
    }

    private void sendErrorStringCallback(final Request request, final Error e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    public static abstract class ResultCallback<T> {

        public abstract void onError(Request request, Error info);

        public abstract void onResponse(T response);

        public abstract void onOtherError(Request request, Exception exception);
    }

    //特殊返回 兑换券支付
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultVoucherCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onOtherError(request, e);
            }
        });
    }

    private void sendErrorStringCallback(final Request request, final VoucherError e, final ResultVoucherCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultVoucherCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    //特殊返回 兑换券支付
    public static abstract class ResultVoucherCallback<T> {

        public abstract void onError(Request request, VoucherError info);

        public abstract void onResponse(T response);

        public abstract void onOtherError(Request request, Exception exception);
    }

    private void showExitDialog(final String info) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builer = new AlertDialog.Builder(AbstractActivity.abstractActivity);
                // builer.setTitle(versionInfo.getAndrTitle());
                //  builer.setMessage(versionInfo.getAndrDescription().toString());
                // builer.setTitle(R.string.sure_log_out);
                builer.setMessage(info);
                builer.setPositiveButton(Application.getInstance().getCurrentActivity().getResources().getString(R.string.text_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Application.getInstance().finishAllActivity();
                        Intent intents = new Intent(Application.getInstance().getApplicationContext(), LoginActivity.class);
                        intents.putExtra("loginflag", "1");
                        AbstractActivity.abstractActivity.startActivity(intents);
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builer.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });

    }

}