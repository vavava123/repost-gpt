package com.gzhu.funai.exception;

/**
 * @Author :wuxiaodong
 * @Date: 2023/7/16 15:56
 * @Description:
 */
public class SessionNameRepeatException extends RuntimeException{
    public SessionNameRepeatException(String message) {
        super(message);
    }
}
