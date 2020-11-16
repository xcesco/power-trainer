package com.abubusoft.powertrainer.backend.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/META-INF/resources/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);

                        Resource defaultResource = location.createRelative(resourcePath);
                        Resource resource = requestedResource.exists() && requestedResource.isReadable() ? requestedResource : defaultResource;

                        return resource;
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                //https://stackoverflow.com/questions/26220083/h2-database-console-spring-boot-load-denied-by-x-frame-options
                .headers().frameOptions().disable()
                .and()
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                //	.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                //.exceptionHandling()
                //	.authenticationEntryPoint(unauthorizedHandler)
                //.and()
                .authorizeRequests()
                //	parte angular
                .antMatchers("/static/**")
                .permitAll()

                // h2-console
                .antMatchers("/h2-console/**")
                .permitAll()

                // configurazione interfaccia swagger
                .antMatchers("/api-docs", "/swagger-ui.html")
                .permitAll()

                // servizi WEB public
                .antMatchers(WebPathConstants.API_ENTRYPOINT + "/**")
                .permitAll()

                // servizi web secured
                .antMatchers("/api/v1/secured/**")
                .authenticated();

    }

}