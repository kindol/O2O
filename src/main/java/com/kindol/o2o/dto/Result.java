package com.kindol.o2o.dto;

/**
 * 封装json对象，所有返回结果都使用它
 * @param <T>
 */
public class Result<T> {

    private boolean success;
    private T data;
    private String errorMsg;
    private int errorCode;

    //默认构造器
    public Result() {
    }

    //成功返回时的构造器
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    //运行错误时的构造器
    public Result(boolean success, int errorCode, String errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
