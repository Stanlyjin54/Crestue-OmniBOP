package com.kuafu.common.domin;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author kuafui
 */
@Data
public class Result<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private boolean success;

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        if (code == 0) {
            success = true;
        } else {
            success = false;
        }
    }

    public Result(int code, T data) {
        this(code, data, "");
    }

    public Result(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(0, data, "ok");
    }

    public static Result success() {
        return new Result<>(0, null, "ok");
    }

    public static Result error(ErrorCode errorCode) {
        return new Result(errorCode);
    }

    public static Result error() {
        return new Result(-1, null, "system error");
    }

    public static Result error(String message) {
        return new Result(-1, null, message);
    }

    public static Result error(int code, String message) {
        return new Result(code, null, message);
    }

    public static Result error(ErrorCode errorCode, String message) {
        return new Result(errorCode.getCode(), null, message);
    }

    public static Result success(Object o, String message) {
        return new Result(0, o, message);
    }
}