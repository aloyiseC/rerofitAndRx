package laiwei.rxandretrofit.retrofit;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import laiwei.rxandretrofit.bean.QuestionTypeList;
import laiwei.rxandretrofit.network.bean.HttpResult;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by liuwei on 2016/7/15 0015.
 */
public final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public MyGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException{
        String response = value.string();
        HttpResult resultResponse = gson.fromJson(response,type);
        String dataJson = gson.toJson(resultResponse.getData());
        QuestionTypeList list = gson.fromJson(dataJson,QuestionTypeList.class);
        resultResponse.setData(list.getList());
        return (T) resultResponse;
    }
}