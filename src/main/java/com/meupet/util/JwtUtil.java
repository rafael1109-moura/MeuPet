package com.meupet.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.meupet.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(@Value("${meupet.security.secret-key}") String secret,
                   @Value("${meupet.security.jwt-expiration-ms:86400000}") long expirationMs) {
        this.secretKey = derivarChave(secret);
        this.expirationMs = expirationMs;
    }

    private SecretKey derivarChave(String secret) {
        byte[] bytes = decodificarBase64(secret);
        if (bytes.length < 32) {
            bytes = sha256(secret.getBytes(StandardCharsets.UTF_8));
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    private byte[] decodificarBase64(String valor) {
        try {
            return Base64.getDecoder().decode(valor);
        } catch (IllegalArgumentException e) {
            return valor.getBytes(StandardCharsets.UTF_8);
        }
    }

    private byte[] sha256(byte[] entrada) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(entrada);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponivel.", e);
        }
    }

    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMs);

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(secretKey)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public boolean validarToken(String token, Usuario usuario) {
        try {
            String email = extrairEmail(token);
            return (email.equals(usuario.getEmail()) && !estaExpirado(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean estaExpirado(String token) {
        return extrairExpiration(token).before(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date extrairExpiration(String token) {
        return extrairClaims(token).getExpiration();
    }
}