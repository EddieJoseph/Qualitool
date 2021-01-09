package ch.eddjos.qualitool.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @Bean
    public FilterRegistrationBean<StaticContentFilter> loggingFilter(){
        FilterRegistrationBean<StaticContentFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new StaticContentFilter());
        registrationBean.addUrlPatterns("/pictures/*","/comment/download/*");
        return registrationBean;
    }
}
