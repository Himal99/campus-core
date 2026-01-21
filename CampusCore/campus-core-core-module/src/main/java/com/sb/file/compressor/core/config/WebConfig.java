package com.sb.file.compressor.core.config;

import com.sb.file.compressor.core.utils.SystemUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * @author Himal Rai on 1/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project fileCompressorPocBackend.
 */

@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String myExternalFilePath = "file:///" + SystemUtils.getOSPath() + "/images/";
        registry.addResourceHandler("/images/**").addResourceLocations(myExternalFilePath).setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");

        registry.addResourceHandler("/resources/static/**").addResourceLocations("/resources/");

        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(threadPoolTaskExecutor());
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set the core number of threads
        executor.setMaxPoolSize(10); // Set the maximum number of threads
        executor.setQueueCapacity(25); // Set the queue capacity
        executor.setThreadNamePrefix("AsyncThread-"); // Set the prefix of thread names
        executor.initialize();
        return executor;
    }


}
