package laiwei.rxandretrofit.network.bean;

/**
 * Created by laiwei on 2017/10/11 0011.
 */
public class HttpResult{
    public static final int SUCCESS = 1;
    public static final int CODE_SUCCESS = 0;
    private String code;
    private String message;
    private Object data;
    private int success;

    public int getCode() {
        return Integer.parseInt(code);
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
