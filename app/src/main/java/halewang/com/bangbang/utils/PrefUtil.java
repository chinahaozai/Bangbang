package halewang.com.bangbang.utils;

import android.content.Context;
import android.content.SharedPreferences;

import halewang.com.bangbang.Constant;

/**
 * Created by halewang on 2017/1/9.
 */

public class PrefUtil {
    public static void putString(Context mContext, String key, String msg){
        SharedPreferences preferences = mContext.getSharedPreferences(Constant.SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,msg);
        editor.apply();
    }

    public static String getString(Context mContext, String key, String defaultValue){
        SharedPreferences preferences = mContext.getSharedPreferences(Constant.SETTING, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void putBoolean(Context mContext, String key, boolean res){
        SharedPreferences preferences = mContext.getSharedPreferences(Constant.SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,res);
        editor.apply();
    }

    public static boolean getBoolean(Context mContext, String key, boolean defaultValue){
        SharedPreferences preferences = mContext.getSharedPreferences(Constant.SETTING, Context.MODE_PRIVATE);
        return preferences.getBoolean(key,defaultValue);
    }
}
