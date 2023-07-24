package com.pipichao.config;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.*;

/**
 * @Author: wangchao
 * @date: 2023/7/24 10:45
 * <p>
 * 自定义受保护安全对象数据全
 **/
public class CustomMetaSource implements FilterInvocationSecurityMetadataSource {
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        if (o instanceof FilterInvocation) {
//            FilterInvocation filterInvocation = (FilterInvocation) o;
//            String requestURL = filterInvocation.getHttpRequest().getRequestURI();
//            List<Permission> list = getInvocationPermission();
//            for (Permission permissionmap : list) {
//                if (antPathMatcher.match(permissionmap.getPartten(), requestURL)) {
//                    return SecurityConfig.createList(permissionmap.getRole());
//                }
//            }
            /**
             * 先假请求需要这个角色
             */
            /**
             * 由于inmemery userservice配置的是用角色管理的权限
             */
            return SecurityConfig.createList("ROLE_zhuguan");
        }
        return null;
    }

    /**
     * 模拟从数据库获取到的所需权限
     *
     * @return
     */
    private List<Permission> getInvocationPermission() {

        Permission zhuguan = new Permission("/zhuguan/**", "zhuguan");
        List<Permission> list = new ArrayList<>();
        list.add(zhuguan);
        return list;
    }

    class Permission {
        private String partten;
        private String role;

        public Permission(String partten, String role) {
            this.partten = partten;
            this.role = role;
        }

        public String getPartten() {
            return partten;
        }

        public void setPartten(String partten) {
            this.partten = partten;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        /**
         * 是否支持过滤器调用
         */
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
