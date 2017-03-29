package halewang.com.bangbang.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by halewang on 2016/11/21.
 */

public class InternetUtil {

    public static boolean isNetworkAvailable(final Context context) {
        boolean hasWifiCon = false;
        boolean hasMobileCon = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {

            String type = net.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                //LevelLogUtils.getInstance().i(tag, "get Wifi connection");
                if (net.isConnected()) {
                    hasWifiCon = true;
                }
            }

            if (type.equalsIgnoreCase("MOBILE")) {
                //LevelLogUtils.getInstance().i(tag, "get Mobile connection");
                if (net.isConnected()) {
                    hasMobileCon = true;
                }
            }
        }
        return hasWifiCon || hasMobileCon;
    }
}
