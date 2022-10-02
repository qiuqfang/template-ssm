package com.template.security.exception;

import com.template.common.api.ResultCode;
import org.apache.shiro.authc.AuthenticationException;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class ApiException extends AuthenticationException {
    private ResultCode resultCode;

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
