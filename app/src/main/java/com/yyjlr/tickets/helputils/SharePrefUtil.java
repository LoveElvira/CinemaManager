package com.yyjlr.tickets.helputils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Elvira on 2016/5/23.
 */
public class SharePrefUtil {

    public static void putBoolean(String fileName, String key, boolean value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String fileName, String key, boolean defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String fileName, String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String fileName, String key, String defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(String fileName, String key, int value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String fileName, String key, int defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(String fileName, String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    /**
     * 使用SharedPreference保存对象
     *
     * @param fileKey    储存文件的key
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public static void save(String fileKey, String key, Object saveObject, Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(fileKey, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String string = Object2String(saveObject);
        editor.putString(key, string);
        editor.commit();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param fileKey 储存文件的key
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object get(String fileKey, String key, Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(fileKey, Activity.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }

    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 存储手机号
     */
//    public static void setPhoneNum(String fileName, String value, Context ctx) {
//        String str = "";
//        String regularEx = ".";
//        String[] phoneValues = getPhoneNum(fileName, ctx);
//        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
//        if (phoneValues!=null){
//            List phoneList = new ArrayList();//Arrays.asList(phoneValues);
//            for (String phone : phoneValues){
//                phoneList.add(phone);
//            }
//            for (int i=0;i<phoneList.size();i++){//去除重复号码
//                if (value.equals(phoneList.get(i))){
//                    phoneList.remove(i);
//                }
//            }
//            if (phoneValues.length==5) {//超过5个  删除第一个
//                phoneList.remove(0);
//            }
//            String[] arr = (String[])phoneList.toArray(new String[phoneList.size()]);
//            for (int i=0;i<arr.length;i++){
//                str += arr[i]+regularEx;
//            }
//            str += value;
//            sp.edit().putString(Constants.SHAREPREF_INFO_PHONENUM, str).commit();
//        } else {
//            sp.edit().putString(Constants.SHAREPREF_INFO_PHONENUM, value).commit();
//        }
//    }
//
//    /**
//     * 读取手机号
//     */
//    public static String[] getPhoneNum(String fileName, Context ctx) {
//        String regularEx = "\\.";
//        String[] str = null;
//        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
//        String values;
//        values = sp.getString(Constants.SHAREPREF_INFO_PHONENUM, "");
//        if (values.length() > 0 && !values.equals("")) {
//            str = values.split(regularEx);
//        }
//        return str;
//    }
//
//    /**
//     * 获取登录的手机号码
//     * */
//    public static String getLoginPhoneNum(String fileName, Context ctx){
//        String[] phoneValues = getPhoneNum(fileName, ctx);
//        return phoneValues[phoneValues.length-1];
//    }
}
