package com.pipichao.test;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestLoginFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println("认证失败处理中");
        System.out.println("username:"+httpServletRequest.getParameter("username"));
        System.out.println("password:"+httpServletRequest.getParameter("password"));
        httpServletResponse.getWriter().write("认证失败");
    }
}
