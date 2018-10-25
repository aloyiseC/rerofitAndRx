package laiwei.rxandretrofit.retrofit;

import android.content.Context;

import java.io.IOException;

import laiwei.rxandretrofit.utils.VersionUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by laiwei on 2017/10/10 0010.
 */
public class HeaderInterceptor implements Interceptor {

    private final Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response originalResponse = chain.proceed(request);
        return VersionUtils.newBuildHeader(context,originalResponse);
    }


}
