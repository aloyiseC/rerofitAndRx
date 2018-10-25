package laiwei.rxandretrofit.retrofit.api.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import laiwei.rxandretrofit.bean.DataConverter;
import laiwei.rxandretrofit.retrofit.MyGsonRequestBodyConverter;
import laiwei.rxandretrofit.retrofit.MyGsonResponseBodyConverter;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by laiwei on 2017/10/12 0012.
 */
public class TianyaConverterFactory extends Converter.Factory{

    public static TianyaConverterFactory create() {
        return create(new Gson());
    }

    public static TianyaConverterFactory create(Gson gson) {
        return new TianyaConverterFactory(gson);
    }

    private final Gson gson;
    private Class<? extends DataConverter> typeClazz;

    private TianyaConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new TianyaResponseBodyConverter<>(gson, type,typeClazz);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyGsonRequestBodyConverter<>(gson, adapter);
    }

    public TianyaConverterFactory setTypeClazz(Class<? extends DataConverter> typeClazz) {
        this.typeClazz = typeClazz;
        return this;
    }
}
