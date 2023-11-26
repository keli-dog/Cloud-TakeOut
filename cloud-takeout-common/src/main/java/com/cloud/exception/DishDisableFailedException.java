package com.cloud.exception;

/**
 * 菜品禁用失败
 */
public class DishDisableFailedException extends BaseException{
    public static final String MSG = "菜品禁用失败";
    public DishDisableFailedException() {
        super(MSG);
    }
    public DishDisableFailedException(String msg) {
        super(msg);
    }
}
