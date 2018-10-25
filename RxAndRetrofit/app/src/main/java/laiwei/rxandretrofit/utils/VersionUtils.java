
package laiwei.rxandretrofit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




public class VersionUtils {

    /**
     * Http 头
     *
     * @param context
     * @return
     */
    public static Map<String, String> createHttpHeaderMapParam(Context context) {
        return createHttpHeaderMapParam(context, UserEncryptUtil.getUserCheckString());
    }

    /**
     * Http 头
     *
     * @param context
     * @return
     */
    public static Map<String, String> createHttpHeaderMapParam(Context context, String userCheckString) {
        String verName = null;
        int verCode = 0;
        ApplicationVersionUtils.ClientVersionInfo vinfo = ApplicationVersionUtils.getApplicationVersionInfo(context);
        if (vinfo != null) {
            verCode = vinfo.getVersionCode();
            verName = vinfo.getVersionName();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("uuid", getAndroidUUID(context));
//        String vendor = context.getString(R.id.vendor);
//        if (TextUtils.isEmpty(vendor)) {
//            vendor = "ty";
//        }
//        map.put("channel", vendor);
//        String vertype = context.getString(R.id.vertype);
//        if (!TextUtils.isEmpty(vertype)) {
//            map.put("vertype", vertype);
//        }
        map.put("mobile", android.os.Build.MODEL + ";" + android.os.Build.DEVICE);
        map.put("sdk", "" + android.os.Build.VERSION.SDK_INT);
        map.put("vcode", "" + verCode);
        map.put("vname", verName);
        map.put("os", "android");
//        String product = context.getString(R.id.product);
//        if (!TextUtils.isEmpty(product)) {
//            map.put("product", product);
//        }
        // headCstr=md5(uuid+channel+mobile+vcode+vname+os+checkStr)
        StringBuilder sb = new StringBuilder();
        sb.append(map.get("uuid"));
        sb.append(map.get("channel"));
        sb.append(map.get("mobile"));
        sb.append(map.get("vcode"));
        sb.append(map.get("vname"));
        sb.append(map.get("os"));
        sb.append(userCheckString);
        // Log.v("VersionUtils", "sb=" + sb.toString());
        map.put("headCstr", MD5Util.MD5(sb.toString()));

        return map;
    }


    public static okhttp3.Response newBuildHeader(Context context,okhttp3.Response originalResponse){
        String verName = null;
        int verCode = 0;
        ApplicationVersionUtils.ClientVersionInfo vinfo = ApplicationVersionUtils.getApplicationVersionInfo(context);
        if (vinfo != null) {
            verCode = vinfo.getVersionCode();
            verName = vinfo.getVersionName();
        }
        String uuid = getAndroidUUID(context);
        String mobile = android.os.Build.MODEL + ";" + android.os.Build.DEVICE;
        okhttp3.Response.Builder builder = originalResponse.newBuilder();
        builder.addHeader("uuid",uuid);
        builder.addHeader("mobile",mobile);
        builder.addHeader("sdk", "" + android.os.Build.VERSION.SDK_INT);
        builder.addHeader("vcode", "" + verCode);
        builder.addHeader("vname", verName);
        builder.addHeader("os", "android");

        StringBuilder sb = new StringBuilder();
        sb.append(uuid);
        sb.append(mobile);
        sb.append(verCode);
        sb.append(verName);
        sb.append("android");
        sb.append(UserEncryptUtil.getUserCheckString());

        builder.addHeader("headCstr", MD5Util.MD5(sb.toString()));
        return builder.build();
    }




    private static final String CONFIG_ANDROID_UUID = "AndroidUUID";

    private static String getAndroidUUID(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String uuid = prefs.getString(CONFIG_ANDROID_UUID, null);
        if (uuid == null) {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            if (telephonyManager == null
                    || TextUtils.isEmpty(telephonyManager.getDeviceId())) {
                uuid = UUID.randomUUID().toString().replace("-", "");
            } else {
                uuid = MD5Util.MD5(telephonyManager.getDeviceId()
                        .toString());
            }

            Editor editor = prefs.edit();
            editor.putString(CONFIG_ANDROID_UUID, uuid);
            editor.commit();
        }
        return uuid;
    }
}
