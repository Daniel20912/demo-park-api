package com.danieloliveira.demo_park_api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
/*
    garante que apenas usuários autenticados e autorizados possam acessar recursos protegidos
    o filtro captura todas as requisições enviadas pela API e verifica se as requisições contêm um token
    se tiver o token, ele captura o token, valida ele e faz a autenticação do usuário a partir das informações contidas nesse token
    entáo o filtro libera a requisição para o metodo (por exemplo: o metodo de criar um usuário ou de atualizar a senha)

    se o token for inválido o Spring interrompe a requisição e envia um erro para o cliente
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);

        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("Token está nulo, vazio ou não iniciado com bearer");
            filterChain.doFilter(request, response);
            return;
        }

        if (!JwtUtils.isTokenValid(token)) {
            log.warn("Token está inválido ou expirado");
            filterChain.doFilter(request, response); // é preciso devolver para o processo de requisição o request e o response, porque às vezes eles podem ter sido alterados
            return;
        }

        String username = JwtUtils.getUsernameFromToken(token);

        toAuthentication(request, username);

        filterChain.doFilter(request, response);
    }

    private void toAuthentication(HttpServletRequest request, String username) {
        UserDetails userDetails = detailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // passa o objeto de requisição para a parte de autenticação do Spring Secutiry

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

