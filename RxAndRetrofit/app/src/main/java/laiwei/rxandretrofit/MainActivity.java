package laiwei.rxandretrofit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.Observable;
import laiwei.rxandretrofit.bean.QuestionType;
import laiwei.rxandretrofit.bean.QuestionTypeList;
import laiwei.rxandretrofit.network.bean.HttpResult;
import laiwei.rxandretrofit.retrofit.RetrofitService;
import laiwei.rxandretrofit.retrofit.api.QuestionApi;
import laiwei.rxandretrofit.utils.RxUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxUtils.rxLoad(MainActivity.this, new RxUtils.Connector() {
                    @Override
                    public Observable<HttpResult> connect() {
                        return RetrofitService.getInstance(MainActivity.this)
                                .setDataConverter(QuestionTypeList.class)
                                .createApi(QuestionApi.class)
                                .getQuestionType(9488474);
                    }
                }, new RxUtils.OnFinishListener<ArrayList<QuestionType>>() {
                    @Override
                    public void onNext(ArrayList<QuestionType> questionTypes) {
                        Log.i("step","success:"+new Gson().toJson(questionTypes));
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        return false;
                    }
                },null);
            }
        });
    }
}
