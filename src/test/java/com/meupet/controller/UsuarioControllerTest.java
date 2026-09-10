package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.dto.UsuarioResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.UsuarioService;
import com.meupet.util.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private UsuarioService service;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UsuarioResponseDTO buildResponse(Long id, String nome, String email) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(id); dto.setNome(nome); dto.setEmail(email);
        return dto;
    }

    @Test
    void cadastrar_deveRetornar201() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("João"); req.setEmail("joao@test.com"); req.setSenha("senha12345");

        when(service.criar(any(UsuarioRequestDTO.class)))
                .thenReturn(buildResponse(1L, "João", "joao@test.com"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@test.com"));
    }

    @Test
    void cadastrar_emailDuplicado_deveRetornar400() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("João"); req.setEmail("joao@test.com"); req.setSenha("senha12345");

        when(service.criar(any(UsuarioRequestDTO.class)))
                .thenThrow(new DadoInvalidoException("O usuário 'joao@test.com' já está cadastrado."));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O usuário 'joao@test.com' já está cadastrado."));
    }

    @Test
    void cadastrar_emailInvalido_deveRetornar400() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("João"); req.setEmail("naoemail"); req.setSenha("senha12345");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastrar_senhaMuitoCurta_deveRetornar400() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("João"); req.setEmail("joao@test.com"); req.setSenha("curta");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_encontrado_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(buildResponse(1L, "João", "joao@test.com"));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Usuário não encontrado."));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
