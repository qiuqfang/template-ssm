package com.template.security.exception;

import com.template.common.api.CommonResult;
import com.template.common.api.ResultCode;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常拦截
     *
     * @param e 自定义异常
     * @return 异常处理结果
     */
    @ExceptionHandler(value = ApiException.class)
    public CommonResult<?> handle(ApiException e) {
        if (e.getResultCode() != null) {
            return CommonResult.failed(e.getResultCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    /**
     * Shiro未授权异常拦截
     *
     * @param e Shiro异常
     * @return 异常处理结果
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public CommonResult<?> handle(UnauthorizedException e) {
        return CommonResult.failed(ResultCode.AUTHORIZE_FAILED);
    }
}
