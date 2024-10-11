package com.example.jwtdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * -10/11-18:15
 * -
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置跨域请求的映射规则
     * 此方法用于解决前端后端分离部署时，浏览器实施的同源策略导致的跨域问题
     * 通过设置CorsRegistry的映射规则，允许指定的前端域名进行跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加跨域配置映射
        registry.addMapping("/**")
                // 允许的跨域请求来源，可以是具体的域名，也可以使用通配符
                .allowedOriginPatterns("http://localhost:*", "https://your-production-domain.com")
                // 允许的HTTP请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的HTTP头，"*"表示允许所有的头
                .allowedHeaders("*")
                // 是否允许发送凭证信息，如Cookie等
                .allowCredentials(true)
                // 预检请求的有效期，单位为秒
                .maxAge(3600);
    }
}