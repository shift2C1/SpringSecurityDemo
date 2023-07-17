package com.pipichao.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangchao
 * @date: 2023/7/17 14:16
 **/

//@Configuration
//@EnableWebSecurity
public class FormLoginConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {

        /**
         * WebSecurity 是过滤器链的代理
         * HttpSecurity 是过滤器链路
         * 相当于多个HttpSecurity被 WebSecurity代理
         * filterchainproxy 代理 securityfilterchain
         */

        // TODO: 2023/7/17 未完成，继续努力

        web.addSecurityFilterChainBuilder(securityfilterchain1())
                .addSecurityFilterChainBuilder(securityfilterchain2());
    }
    @Bean(name = "securityfilterchain1")
    public HttpSecurity securityfilterchain1() throws Exception {
        ObjectPostProcessor objectPostProcessor=new ObjectPostProcessor() {
            @Override
            public Object postProcess(Object o) {
                return o;
            }
        };
        AuthenticationManagerBuilder authenticationManagerBuilder=new AuthenticationManagerBuilder(objectPostProcessor);
        HttpSecurity http=new HttpSecurity(objectPostProcessor,authenticationManagerBuilder,new HashMap<>());
        http.authorizeRequests().
                antMatchers("/getData").hasRole("xixi")
                .anyRequest().authenticated()
                .and()

                .formLogin()
                //表单提交的路径
                .loginProcessingUrl("/loginform")
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    System.out.println(authentication);
                    httpServletResponse.getWriter().write(authentication.toString());
                })
                .failureHandler((httpServletRequest, httpServletResponse, e) ->
                {
                    e.printStackTrace();
                    httpServletResponse.getWriter().write(e.toString());
                    return;
                })

                .and()
                .csrf().disable()//关闭了，可以用postman等调试，不然登录始终跳转到默认的登陆页面
        ;
//                .userDetailsService(new InMemoryUserDetailsManager(user, admin));
        return http;
    }


    @Bean(name = "securityfilterchain2")
    public HttpSecurity securityfilterchain2() throws Exception {

        ObjectPostProcessor objectPostProcessor=new ObjectPostProcessor() {
            @Override
            public Object postProcess(Object o) {
                return o;
            }
        };
        AuthenticationManagerBuilder authenticationManagerBuilder=new AuthenticationManagerBuilder(objectPostProcessor);

        HttpSecurity http=new HttpSecurity(objectPostProcessor,authenticationManagerBuilder,new HashMap<>());
        http.authorizeRequests().
                antMatchers("/getData").hasRole("xixi")
                .anyRequest().authenticated()
                .and()

                .formLogin()
                //表单提交的路径
                .loginProcessingUrl("/loginform")
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    System.out.println(authentication);
                    httpServletResponse.getWriter().write(authentication.toString());
                })
                .failureHandler((httpServletRequest, httpServletResponse, e) ->
                {
                    e.printStackTrace();
                    httpServletResponse.getWriter().write(e.toString());
                    return;
                })

                .and()
                .csrf().disable()//关闭了，可以用postman等调试，不然登录始终跳转到默认的登陆页面
        ;
//                .userDetailsService(new InMemoryUserDetailsManager(user, admin));
        return http;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(new DaoAuthenticationProvider());
    }
//    @Bean()
//    public AuthenticationProvider authenticationProvider(){
//        return ;
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("user")
                .password("123")
                .roles("USER")
                .build();
        UserDetails admin = users
                .username("admin")
                .password("123")
                .roles("USER", "ADMIN", "xixi")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
