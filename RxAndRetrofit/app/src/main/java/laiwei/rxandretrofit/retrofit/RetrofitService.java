package laiwei.rxandretrofit.retrofit;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import laiwei.rxandretrofit.bean.DataConverter;
import laiwei.rxandretrofit.retrofit.api.converter.TianyaConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by laiwei on 2017/10/10 0010.
 */
public class RetrofitService {

    private static final String HTTP_BASETESTURL = "";
    private static final String HTTP_LOGIN_BASETESTURL = "";
    private static final int CONNECTION_TIMEOUT = 6;
    private static final int DATE_TIMEOUT = 50;
    private static RetrofitService instance;
    private  OkHttpClient sOkHttpClient;
    private Retrofit.Builder builder;

    public static RetrofitService getInstance(Context context){
        if(instance == null){
            instance = new RetrofitService(context);
        }
        return instance;
    }

    private RetrofitService(Context context) {
        sOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new HeaderInterceptor(context))
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DATE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        builder = new Retrofit.Builder().client(sOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(HTTP_BASETESTURL);
    }

    public RetrofitService setDataConverter(Class<? extends DataConverter> clazz){
        builder.addConverterFactory(TianyaConverterFactory.create().setTypeClazz(clazz));
        return instance;
    }

    public RetrofitService setBaseUrl(String baseUrl){
        builder.baseUrl(baseUrl);
        return instance;
    }

    public  <T> T createApi(Class<T> clazz) {
        return builder.build().create(clazz);
    }
}
