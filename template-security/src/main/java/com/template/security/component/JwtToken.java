package com.template.security.component;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public JwtToken() {
        super();
    }

    public JwtToken(String token) {
        super();
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        // 添加缓存后，不允许返回null，否则缓存不生效
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
