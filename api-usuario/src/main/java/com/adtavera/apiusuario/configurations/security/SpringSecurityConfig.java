package com.adtavera.apiusuario.configurations.security;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SpringSecurityConfig {
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/login", "/api/users/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    // Crear un objeto JSON que tenga el formato deseado
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("status", "ERROR");
                    jsonResponse.put("message", "Acceso denegado: No tiene permiso para ejecutar esta acción");

                    // Crear una ResponseEntity con el JSON y el estado HTTP
                    ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse.toString(), HttpStatus.FORBIDDEN);

                    // Establecer las cabeceras y escribir la respuesta en el OutputStream
                    ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
                    ((HttpServletResponse) response).setStatus(responseEntity.getStatusCodeValue());
                    response.getOutputStream().write(responseEntity.getBody().getBytes("UTF-8"));
                }));


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/configuration/**",
                "/h2-console/**",
                "/webjars/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/public/**",
                "/apidoc");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
