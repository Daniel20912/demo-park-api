package com.danieloliveira.demo_park_api.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Http status 401 {}", authException.getMessage());

        /*
            quando o usuário não estiver autenticado, no cabeçalho terá o cabeçalho www-Authenticate com a instrução bearer
            informando que ele vai ter que trabalhar com um token do tipo bearer e esse token deve ser enviado para /api/v1/auth
         */
        response.setHeader("WWW-Authenticate", "Bearer realm='/api/v1/auth'");
        response.sendError(401);
    }
}
