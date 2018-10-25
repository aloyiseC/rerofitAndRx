package laiwei.rxandretrofit.retrofit;

/**
 * Created by liuwei on 2016/7/15 0015.
 */
public class ResultException extends RuntimeException {

    private int errCode = -1;

    public ResultException(int errCode, String msg) {
        super(msg);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
