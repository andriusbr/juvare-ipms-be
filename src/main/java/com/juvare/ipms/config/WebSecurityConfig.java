package com.juvare.ipms.config;

import com.juvare.ipms.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public WebSecurityConfig(AuthenticationService authenticationService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.authenticationService = authenticationService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/auth").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthorizationFilter(authenticationManagerBean(), authenticationService))
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                        .allowCredentials(true);
            }
        };
    }
}
