package br.com.movieschain.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;

    /* Endpoint de cadastro de filme só é possivel ser utilizado com x-api-key */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/filmes")
                .excludePathPatterns("/api/filmes", "/api/filmes/**", "/api/auth/**");
    }
}
