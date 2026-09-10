package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.LoginRequestDTO;
import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.model.Usuario;
import com.meupet.service.UsuarioService;
import com.meupet.util.JwtUtil;
import com.meupet.util.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private UsuarioService service;
    @MockitoBean private JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Usuario buildUsuario() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        usuario.setNome("Dono"); usuario.setEmail("dono@meupet.com");
        return usuario;
    }

    @Test
    void login_valido_deveRetornarToken() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("dono@meupet.com"); req.setSenha("senha123");

        when(service.autenticar("dono@meupet.com", "senha123"))
                .thenReturn(Optional.of(buildUsuario()));
        when(jwtUtil.gerarToken(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenReturn("token-de-teste");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-de-teste"))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.email").value("dono@meupet.com"));
    }

    @Test
    void login_credenciaisInvalidas_deveRetornar404() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("x@y.com"); req.setSenha("errada");

        when(service.autenticar("x@y.com", "errada"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_semSenha_deveRetornar400() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("dono@meupet.com");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_valido_deveRetornar200() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("Novo"); req.setEmail("novo@meupet.com"); req.setSenha("senha123");

        when(service.criarEntidade(org.mockito.ArgumentMatchers.any(UsuarioRequestDTO.class)))
                .thenReturn(buildUsuario());
        when(jwtUtil.gerarToken(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenReturn("token-de-teste");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-de-teste"));
    }

    @Test
    void register_emailDuplicado_deveRetornar400() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("Novo"); req.setEmail("novo@meupet.com"); req.setSenha("senha123");

        when(service.criarEntidade(org.mockito.ArgumentMatchers.any(UsuarioRequestDTO.class)))
                .thenThrow(new com.meupet.model.DadoInvalidoException("já cadastrado"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}