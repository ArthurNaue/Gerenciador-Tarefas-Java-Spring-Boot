package com.ifsc.tarefas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsFilter 
{
    @Bean
    public WebMvcConfigurer corsConfig()
    {
        return new WebMvcConfigurer() 
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
              registry.addMapping("/**")
                .allowedOrigins("https://hoppscotch.io")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
            }
        };
    }
}
