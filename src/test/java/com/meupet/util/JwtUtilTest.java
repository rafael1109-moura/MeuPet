package com.meupet.util;

import com.meupet.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil() {
        return new JwtUtil("segredo-de-teste-para-jwt-32-bytes", 86400000L);
    }

    private Usuario usuario() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        usuario.setNome("Dono"); usuario.setEmail("dono@meupet.com");
        return usuario;
    }

    @Test
    void gerarToken_deveConterEmailDoUsuario() {
        String token = jwtUtil().gerarToken(usuario());

        assertNotNull(token);
        assertEquals("dono@meupet.com", jwtUtil().extrairEmail(token));
    }

    @Test
    void validarToken_tokenValido_deveRetornarTrue() {
        JwtUtil util = jwtUtil();
        String token = util.gerarToken(usuario());

        assertTrue(util.validarToken(token, usuario()));
    }

    @Test
    void validarToken_tokenDeOutroUsuario_deveRetornarFalse() {
        JwtUtil util = jwtUtil();
        String token = util.gerarToken(usuario());

        Usuario outro = new Usuario();
        ReflectionTestUtils.setField(outro, "id", 2L);
        outro.setNome("Outro"); outro.setEmail("outro@meupet.com");

        assertFalse(util.validarToken(token, outro));
    }

    @Test
    void validarToken_tokenAlterado_deveRetornarFalse() {
        JwtUtil util = jwtUtil();
        String token = util.gerarToken(usuario());

        assertFalse(util.validarToken(token + "x", usuario()));
    }

    @Test
    void extrairEmail_tokenInvalido_deveLancarExcecao() {
        assertThrows(Exception.class, () -> jwtUtil().extrairEmail("token.invalido.aqui"));
    }
}