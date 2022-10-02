package com.template.security.component;

import com.template.common.api.CommonResult;
import com.template.common.api.ResultCode;
import com.template.security.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class JwtTokenFilter extends AccessControlFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Value("${security.tokenHeader}")
    private String tokenHeader;
    @Value("${security.tokenHead}")
    private String tokenHead;
    @Value("${security.ignoreUrls}")
    private String[] ignoreUrls;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        HttpServletRequest req = WebUtils.toHttp(request);

        for (String ignoreUrl : ignoreUrls) {
            // 判断访问路径是否在白名单中
            if (pathMatcher.matches(ignoreUrl, req.getRequestURI())) {
                return true;
            }
        }
        LOGGER.info("访问URI:{}", req.getRequestURI());

        String authorization = req.getHeader(tokenHeader);

        if (StringUtils.isEmpty(authorization)) {
            String json = new ObjectMapper().writeValueAsString(CommonResult.failed(ResultCode.AUTHENTICATE_FAILED));
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return Boolean.FALSE;
        }

        JwtToken token = new JwtToken(authorization.replace(tokenHead, ""));
        try {
            getSubject(request, response).login(token);
        } catch (ApiException e) {
            String json = new ObjectMapper().writeValueAsString(CommonResult.failed(e.getResultCode(), e.getMessage()));
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

    /**
     * 设置跨域，重写onPreHandle方法
     */
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest req = WebUtils.toHttp(request);
        HttpServletResponse res = WebUtils.toHttp(response);

        res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-Control-Allow-Methods", req.getMethod());
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        res.setHeader("Content-Type", "application/json;charset=UTF-8");
        return super.onPreHandle(req, res, mappedValue);
    }

}
