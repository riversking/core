package com.rivers.core.util;

import com.rivers.core.exception.BusinessException;
import org.springframework.util.Assert;

public class ExceptionUtil extends Assert {

    public static void throwBusinessException(String errorCode) {
        throw new BusinessException(errorCode);
    }

    public static void throwBusinessException(String errorCode, String msg) {
        throw new BusinessException(errorCode, msg);
    }

    public static void throwBusinessException(String errorCode, Throwable e) {
        throw new BusinessException(errorCode, e);
    }

}
