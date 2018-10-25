package laiwei.rxandretrofit.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import laiwei.rxandretrofit.network.bean.HttpResult;


/**
 * Created by laiwei on 2017/8/31 0031.
 */
public class RxUtils {
    /**
     * 基于Rx异步请求数据方法
     * @param context
     * @param connector 请求接口
     * @param listener 数据请求完成处理接口 onNext处理成功 onError 处理失败
     * @param observerOnMainThread listener是否在UI线程 处理数据
     * @param loadingStr 为空则不显示进度条
     * @param <T> 请求返回的数据类型 一般为ArrayList<Entity> 或者 某个bean
     */
    public static <T> DisposableObserver rxLoad(final Context context, final Connector connector, final OnFinishListener<T> listener, boolean observerOnMainThread, String loadingStr
                                                ){
//        Observable obserable = Observable.create(new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
//
//                if(ContextUtils.checkNetworkConnection(context)){
//                    ClientRecvObject clientRecvObject = connector.connect();
//                    T result = null;
//                    if (clientRecvObject.isSuccess() &&
//                            clientRecvObject.getErrorCode() == ClientRecvObject.RESPONCE_ERROR_SUCCESS) {
//                        result = (T) clientRecvObject.getClientData();
//                    }
//                    if (result != null) {
//                        e.onNext(result);
//                        e.onComplete();
//                    }else{
//                        ClientRecvErrorException errorException = new ClientRecvErrorException();
//                        errorException.setClientRecvObject(clientRecvObject);
//                        e.onError(errorException);
//                    }
//                }else{
//                    e.onError(new NetworkErrorException());
//                }
//            }
//        }).subscribeOn(Schedulers.io());
//        if(observerOnMainThread){
//            obserable = obserable.observeOn(AndroidSchedulers.mainThread());
//            if(loadingStr != null){
//                obserable = obserable.compose(LoadingTransformer.<T>applyLoading(context,loadingStr));
//            }
//        }
        Observable obserable = connector.connect()
        .flatMap(new Function<HttpResult,Observable<T>>() {
            @Override
            public Observable<T> apply(final @NonNull HttpResult result) throws Exception {
                if (result.getSuccess() == HttpResult.SUCCESS && result.getCode() == HttpResult.CODE_SUCCESS) {
                    return Observable.create(new ObservableOnSubscribe<T>(){
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                            e.onNext((T) result.getData());
                            e.onComplete();
                        }
                    });
                } else {
                    return Observable.error(new HttpErrorException(result));
                }
            }
        })
                .subscribeOn(Schedulers.io());

        DisposableObserver disposableObserver = new DisposableObserver<T>() {
            @Override
            public void onNext(T t) {
                if(listener != null){
                    listener.onNext(t);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(listener != null){
                    listener.onError(e);
                }
            }

            @Override
            public void onComplete() {

            }
        };
        obserable.subscribe(disposableObserver);
        return disposableObserver;
    }


    public static <T> DisposableObserver rxLoad(final Context context, final Connector connector, final OnFinishListener<T> listener, String loadingStr){
       return rxLoad(context,connector,listener,true,loadingStr);
    }


    public static interface Connector{
        Observable<HttpResult> connect();
    }

    public static interface OnFinishListener<T> {
        void onNext(T t);
        boolean onError(Throwable e);
    }



    public static class HttpErrorException extends Exception implements Serializable {
        private HttpResult result;

        public HttpErrorException() {
            super();
        }

        public HttpResult getClientRecvObject() {
            return result;
        }

        public void setHttpResult(HttpResult result) {
            this.result = result;
        }

        public HttpErrorException(HttpResult result) {
            super();
            this.result = result;
        }

        public HttpErrorException(String code,String message) {
            super();
            result = new HttpResult();
            result.setCode(code);
            result.setMessage(message);
        }
    }

    public static enum ResultType{
        BEAN,LIST,DEFAULT
    }

    public static <T> DisposableObserver rxLoad(final Context context,Observable obserable, final OnFinishListener<T> listener, boolean observerOnMainThread, String loadingStr
    ){

        Observable ob = obserable
                .flatMap(new Function<T,Observable<T>>() {
                    @Override
                    public Observable<T> apply(final @NonNull T t) throws Exception {

                        if(t instanceof HttpResult){
                            HttpResult result = (HttpResult)t;
                            if (result.getSuccess() == HttpResult.SUCCESS && result.getCode() == HttpResult.CODE_SUCCESS) {
                                return Observable.create(new ObservableOnSubscribe<T>(){
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        e.onNext((T) result.getData());
                                        e.onComplete();
                                    }
                                });
                            } else {
                                return Observable.error(new HttpErrorException(result));
                            }
                        }else{
                            return Observable.error(new HttpErrorException(result));
                        }
                    }
                })
                .subscribeOn(Schedulers.io());

        DisposableObserver disposableObserver = new DisposableObserver<T>() {
            @Override
            public void onNext(T t) {
                if(listener != null){
                    listener.onNext(t);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(listener != null){
                    listener.onError(e);
                }
            }

            @Override
            public void onComplete() {

            }
        };
        ob.subscribe(disposableObserver);
        return disposableObserver;
    }
}
