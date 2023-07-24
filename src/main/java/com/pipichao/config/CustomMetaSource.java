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
 *
 * 自定义受保护安全对象数据全
 **/
public class CustomMetaSource implements FilterInvocationSecurityMetadataSource {
    private AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        List<String> collection=new ArrayList<>();
        if (o instanceof FilterInvocation) {
            FilterInvocation filterInvocation = (FilterInvocation) o;
            StringBuffer requestURL = filterInvocation.getHttpRequest().getRequestURL();
            List<Map<String, String>> list = getInvocationPermission();
            for (Map<String, String> permissionmap : list) {
                permissionmap.forEach((patten,role)->{
                    if (antPathMatcher.match(patten,requestURL.toString())) {
                        collection.add(role);
                    }
                });
            }
        }
        return SecurityConfig.createList(collection.toArray(new String[0]));
    }

    /**
     * 模拟从数据库获取到的所需权限
     * @return
     */
    private List<Map<String,String>> getInvocationPermission(){
        Map<String,String> map=new HashMap<>();
        map.put("/zhuguan/**","zhuguan");
        List<Map<String,String>> list=new ArrayList<>();
        list.add(map);
        return list;
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
