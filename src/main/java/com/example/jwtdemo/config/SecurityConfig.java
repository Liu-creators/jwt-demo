package com.example.jwtdemo.config;

import com.example.jwtdemo.filter.JwtRequestFilter;
import com.example.jwtdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 为了调试，暂时使用 NoOpPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
        // 生产环境应使用：return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置Spring Security的HTTP安全设置
     * @param http HttpSecurity对象，用于配置HTTP安全设置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置跨域资源共享(CORS)设置，使用自定义的配置源
        http.cors().configurationSource(corsConfigurationSource())
              // 继续下一部分配置，与CORS配置并列
              .and()
              // 禁用跨站请求伪造(CSRF)保护，因为我们的应用是无状态的，不通过Cookie传递信息
              .csrf().disable()
              // 配置请求的授权规则
              .authorizeRequests()
              // 允许所有OPTIONS请求和访问根路径下的所有资源
              .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
              // 允许所有请求访问/authenticate路径，这是登录路径
              .antMatchers("/authenticate","/refresh").permitAll()
              // 所有其他请求都需要经过身份验证
              .anyRequest().authenticated()
              // 继续配置会话管理
              .and()
              // 设置会话创建策略为无状态，应用不维护会话信息
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 在UsernamePasswordAuthenticationFilter之前添加JWT请求过滤器，用于检查JWT令牌
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置跨域请求的 CORS（Cross-Origin Resource Sharing）
     * 该方法通过 Bean 定义返回一个 CorsConfigurationSource，用于全局配置 CORS 策略
     * 主要用于解决前端开发中的跨域问题，允许特定的域名或本地开发环境进行跨域请求
     * @return CorsConfigurationSource 配置好的跨域请求源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // 创建一个新的 CORS 配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置允许跨域请求的源，包括本地开发域名和生产环境域名
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "https://your-production-domain.com"));
        // 设置允许的 HTTP 方法列表，包括常见的请求方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // 设置允许的头部列表，包括请求中需要携带的认证信息和内容类型
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        // 设置暴露给前端的头部列表，用于安全考虑，只暴露必要的头部
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        // 允许在请求中发送凭证，如 Cookies 和 HTTP 认证头
        configuration.setAllowCredentials(true);
        // 创建一个基于 URL 的 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 将 CORS 配置应用到所有 URL 路径
        source.registerCorsConfiguration("/**", configuration);
        // 返回配置好的 CORS 配置源
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/",
                "/index.html", // springboot 会默认 将 resources/static 作为根目录
                "/static/index.html" , // 这个路径 需要 resources/static/static
                "/static/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico"
        );
    }
}