package com.pipichao.test;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import java.io.IOException;

public class TestLoginSuccess  implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println("处理认证中");
//        httpServletResponse.getWriter().write("登录认证成功");
        System.out.println("认证成功");
        httpServletResponse.sendRedirect("/index.jsp");
    }
}
