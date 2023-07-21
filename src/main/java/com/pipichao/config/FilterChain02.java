package com.pipichao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
@Order(101)
public class FilterChain02 extends WebSecurityConfigurerAdapter {
    private final String contextPath="/xiyouji";
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        InMemoryUserDetailsManager haoren=new InMemoryUserDetailsManager();
        haoren.createUser(User.withUsername("tangseng").password("{noop}111").roles("zhugong").build());
        haoren.createUser(User.withUsername("sunwukong").password("{noop}111").roles("wujiang").build());


        InMemoryUserDetailsManager huairen=new InMemoryUserDetailsManager();
        huairen.createUser(User.withUsername("huangmeidawang").password("{noop}111").roles("yaoguai").build());


        http.antMatcher(contextPath+"/*")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl(contextPath+"/02login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.getWriter().write("02login success");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        e.printStackTrace();
                        httpServletResponse.getWriter().write("02login failed");
                    }
                })
                .and()
                .csrf().disable()

                /**
                 *
                 * 多个认证源
                 */

                .userDetailsService(haoren)
                .userDetailsService(huairen)

        ;
    }


    /**
     *
     * 如果配置了自己的全局 parent AuthenticationManager 则由其他配置类注入spring的全局paren在这个登录流程过滤器链里不生效
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManager filterChain02SelfGlobalUserDetailservice=new InMemoryUserDetailsManager();
        filterChain02SelfGlobalUserDetailservice.createUser(User.withUsername("rulai").password("{noop}111").roles("rulai").build());
        auth.userDetailsService(filterChain02SelfGlobalUserDetailservice);
    }
}
