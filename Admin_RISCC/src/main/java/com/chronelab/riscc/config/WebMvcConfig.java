package com.chronelab.riscc.config;

import com.chronelab.riscc.util.ConfigUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/saved_file/**", "/tmp_file/**")
                .addResourceLocations(ConfigUtil.INSTANCE.getFileSavingDirectory(), ConfigUtil.INSTANCE.getFileSavingDirectoryTemp());
    }
}
