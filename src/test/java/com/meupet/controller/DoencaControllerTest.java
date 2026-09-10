package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.DoencaRequestDTO;
import com.meupet.dto.DoencaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.DoencaService;
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

@WebMvcTest(DoencaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class DoencaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private DoencaService service;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private DoencaResponseDTO buildResponse(Long id, String nome) {
        DoencaResponseDTO dto = new DoencaResponseDTO();
        dto.setId(id); dto.setNome(nome); dto.setDescricao("Desc"); dto.setTratamento("Tratamento");
        return dto;
    }

    @Test
    void cadastrar_deveRetornar201() throws Exception {
        DoencaRequestDTO req = new DoencaRequestDTO();
        req.setNome("Raiva"); req.setDescricao("Zoonose"); req.setTratamento("Vacinação");

        when(service.criar(any(DoencaRequestDTO.class)))
                .thenReturn(buildResponse(1L, "Raiva"));

        mockMvc.perform(post("/api/doencas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Raiva"));
    }

    @Test
    void cadastrar_nomeDuplicado_deveRetornar400() throws Exception {
        DoencaRequestDTO req = new DoencaRequestDTO();
        req.setNome("Raiva"); req.setDescricao("Desc");

        when(service.criar(any(DoencaRequestDTO.class)))
                .thenThrow(new DadoInvalidoException("A doença 'Raiva' já está cadastrada."));

        mockMvc.perform(post("/api/doencas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A doença 'Raiva' já está cadastrada."));
    }

    @Test
    void buscarPorId_encontrado_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(buildResponse(1L, "Raiva"));

        mockMvc.perform(get("/api/doencas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Raiva"));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Doença não encontrada."));

        mockMvc.perform(get("/api/doencas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/doencas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cadastrar_semNome_deveRetornar400() throws Exception {
        DoencaRequestDTO req = new DoencaRequestDTO();
        req.setNome(""); req.setDescricao("Desc");

        mockMvc.perform(post("/api/doencas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
