package com.pipichao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

/**
 * @Description TODO
 * @Author wangchao
 * @Date 2021/8/23
 */
@SpringBootApplication(exclude = {
//        SecurityAutoConfiguration.class
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
/**
 *
 * github token
 *  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
 *
 */