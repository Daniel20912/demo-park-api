package com.danieloliveira.demo_park_api.config;

import com.danieloliveira.demo_park_api.jwt.JwtAuthenticationEntryPoint;
import com.danieloliveira.demo_park_api.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMethodSecurity // para determinar as regras de autorizações, por níveis de perfis, nas classes do tipo controller
@EnableWebMvc // annotation necessária para trabalhar com sistemas de segurança
@Configuration
public class SpringSecurityConfig {

    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-park.html", "/docs-park/**",
            "/v3/api-docs/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // precisa desabilitar para usar uma aplicação stateless
                .formLogin(AbstractHttpConfigurer::disable) // desabilitar para o spring não esperar por um formulário de login
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic é um tipo de autenticação que é possivel de ser feita por meio de login e senha, porém não tem segurança nenhuma
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll()  // indica que o metodo de criar um usuário, é o único que não vai precisar de uma autenticação
                        .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll() // permite que todos os usuários possam tentar autenticar
                        .requestMatchers(DOCUMENTATION_OPENAPI).permitAll() // faz com que o spring security libere o acesso à documentação
                        .anyRequest().authenticated() // indica que, tirando o permitAll, todas as outras operações vão precisar de autenticação
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // diz que a política de sessão é Stateless
                .addFilterBefore(
                        jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class // adiciona os filtros
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint())) // sempre que houver uma excessão de um usuário não logado, o Spring vai na classe JwtAuthenticationEntryPoint e lança a excessão com status 401
                .build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    // referente ao tipo de criptografia de senha
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // essa é considerada a criptografia mais segura atualmente
    }

    @Bean
    // referente ao gerenciamento de autenticação
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // esse metodo retorna um objeto de gerneciamento de configurção
    }

}
