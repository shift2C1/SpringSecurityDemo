package com.pipichao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wangchao
 * @date: 2023/7/21 10:25
 **/
@Configuration
@Order(100)
public class FilterChain01 extends WebSecurityConfigurerAdapter {
    private final String contextPath="/sanguo";

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/*.html")
                .antMatchers("/*.css")
                .antMatchers("/*.js");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        InMemoryUserDetailsManager shuguo=new InMemoryUserDetailsManager();
        shuguo.createUser(User.withUsername("liubei").password("{noop}111").roles("zhugong").build());
        shuguo.createUser(User.withUsername("guanyu").password("{noop}111").roles("wujiang").build());
        shuguo.createUser(User.withUsername("zhugeliang").password("{noop}111").roles("junshi").build());

        InMemoryUserDetailsManager weiguo=new InMemoryUserDetailsManager();
        weiguo.createUser(User.withUsername("caocao").password("{noop}111").roles("zhugong").build());
        weiguo.createUser(User.withUsername("dianwei").password("{noop}111").roles("wujiang").build());
        weiguo.createUser(User.withUsername("xunyu").password("{noop}111").roles("junshi").build());

        http

                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        httpServletResponse.getWriter().write("403...");
                    }
                })
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

                        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        /**
                         * 前后端不分离：跳到登录页
                         * 401->responce header 里location就是需要重定向的url
                         */
//
                        //httpServletResponse.sendRedirect("/login.html");
                        /**
                         * 前后端不分离，返回json字符串
                         */
                        httpServletResponse.getWriter().write("401...");
                    }
                })
                .and()
                .antMatcher(contextPath+"/**")
                .authorizeRequests()
                .antMatchers(contextPath+"/zhugong/getData").hasRole("zhugong")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                /**
                 * 测试用，现在都是前后端分离
                 */
//                .loginPage("/login.html")
                .loginProcessingUrl(contextPath+"/01login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.getWriter().write("01login success");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        e.printStackTrace();
                        httpServletResponse.getWriter().write("01login failed");
                    }
                })
                .and()
                .csrf().disable()

                /**
                 * 添加连个数据源
                 */
                .userDetailsService(shuguo)
                /*
                如果只在这里使用方法配置，相当于给每个过滤器链配置局部自己的userDetailsService
                如果使用spring注入的方式想到哪与给全局的parent authenticationManager配置userDetailsService
                 */
                .userDetailsService(weiguo)
        ;
    }


    /**
     * 给全局的parent authenticationManager 配置userDetailsService
     * 两条登录流程过滤器链路都可以用这个账号登录
     * FilterChain01，FilterChain02均可，如果没有配置自己的全局parent authenticationManager
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsServiceglobal(){
        InMemoryUserDetailsManager userDetailsManager=new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("hanxiedi").password("{noop}111").roles("huangdi").build());
        return userDetailsManager;
    }

    /**
     *
     * 自己登录流程的 parent AuthenticationManager
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        InMemoryUserDetailsManager userDetailsManager=new InMemoryUserDetailsManager();
//        userDetailsManager.createUser(User.withUsername("hanxiedi").password("{noop}111").roles("huangdi").build());
//        auth.userDetailsService(userDetailsManager);
//    }
}
