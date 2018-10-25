package laiwei.rxandretrofit.retrofit.api;

import java.util.ArrayList;

import io.reactivex.Observable;
import laiwei.rxandretrofit.bean.QuestionType;
import laiwei.rxandretrofit.network.bean.HttpResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by laiwei on 2017/10/11 0011.
 */
public interface QuestionApi {
    @GET("forumStand/getWendaTagList")
    Observable<HttpResult> getQuestionType(@Query("userId")int userId);
}
