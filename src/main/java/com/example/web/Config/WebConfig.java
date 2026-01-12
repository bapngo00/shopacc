package com.example.web.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map các URL không có .html về các file HTML tương ứng
        registry.addViewController("/admin").setViewName("forward:/admin.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/register").setViewName("forward:/register.html");
        registry.addViewController("/product-detail").setViewName("forward:/product-detail.html");
        registry.addViewController("/order-detail").setViewName("forward:/order-detail.html");
        registry.addViewController("/index").setViewName("forward:/index.html");
    }
}
