package com.danieloliveira.demo_park_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class JwtUtils {
    public static String JWT_BEARER = "Bearer ";
    public static String JWT_AUTHORIZATION = "Authorization";
    public static String SECRET_KEY = "0123456789-0123456789-0123456789"; // a secrete key deve ter no minimo 32 caracteres

    // essas váriaveis armazenam o tempo que o token vai levar para expirar
    public static long EXPIRES_DAYS = 0;
    public static long EXPIRES_HOURS = 0;
    public static long EXPIRES_MINUTES = 2; // o token vai expirar em 2 minutos

    private JwtUtils() {
    }

    // Metodo para geração criptografada da chave secreta
    private static SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // prepara a chave para ser criptografada no momento que for gerar o token
    }

    // processo refente a expiração do token
    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRES_DAYS).plusHours(EXPIRES_HOURS).plusMinutes(EXPIRES_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant()); // é preciso retornar um Date pos a biblioteca JWT trabalha com Dates
    }

    // Metodo de geração do token JWT
    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date(); // data de criação do token
        Date limit = toExpireDate(issuedAt);
        String token = Jwts.builder()
                .header().add("typ", "JWT")// 'typ' é a chave, 'jwt' informa que um token do tipo JWT
                .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(limit)
                .signWith(generateKey())
                .claim("role", role) // o claim serve para quando se quer adicionar alguma informação específica ao token, pode ter quantos claims quiser
                .compact(); // trasnforma o token em um formato string

        return new JwtToken(token);
    }

    // Metodo para retornar o payload
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey()) // verifica se a assinatura que foi recibida no token confere com a assinatura gerada na classe
                    .build()
                    .parseSignedClaims(refactorToken(token)).getPayload();
        } catch (JwtException e) {
            log.error("Token inválido {}", e.getMessage());
        }
        return null;
    }

    // metodo para recuperar o username do token
    public static String getUsernameFromToken(String token) {
        return Objects.requireNonNull(getClaimsFromToken(token)).getSubject();
    }

    // Metodo para testar se o token é valido
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token)); //A instrução Bearer precisa ser removida porque o token que acompanha o cabeçalho da requisição é precedido pelo prefixo Bearer, mas as bibliotecas que processam e validam o token não reconhece ou não processam esse prefixo. Elas esperam apenas o valor puro do token JWT.
            return true;
        } catch (JwtException ex) {
            log.error("Token inválido {}", ex.getMessage());
        }
        return false;
    }

    // remove do token a instrução bearer
    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

}
