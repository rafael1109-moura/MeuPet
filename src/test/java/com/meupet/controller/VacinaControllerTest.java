package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.VacinaRequestDTO;
import com.meupet.dto.VacinaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.VacinaService;
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

@WebMvcTest(VacinaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class VacinaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private VacinaService service;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private VacinaResponseDTO buildResponse(Long id, String nome, Integer periodicidade) {
        VacinaResponseDTO dto = new VacinaResponseDTO();
        dto.setId(id); dto.setNome(nome); dto.setDescricao("Desc");
        dto.setPeriodicidadeMeses(periodicidade);
        dto.setDoencaId(1L); dto.setDoencaNome("Cinomose");
        return dto;
    }

    @Test
    void cadastrar_deveRetornar201() throws Exception {
        VacinaRequestDTO req = new VacinaRequestDTO();
        req.setNome("V8"); req.setDescricao("Proteção"); req.setDoencaId(1L); req.setPeriodicidadeMeses(12);

        when(service.criar(any(VacinaRequestDTO.class)))
                .thenReturn(buildResponse(1L, "V8", 12));

        mockMvc.perform(post("/api/vacinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("V8"))
                .andExpect(jsonPath("$.periodicidadeMeses").value(12));
    }

    @Test
    void cadastrar_nomeDuplicado_deveRetornar400() throws Exception {
        VacinaRequestDTO req = new VacinaRequestDTO();
        req.setNome("V8"); req.setDescricao("Desc"); req.setDoencaId(1L);

        when(service.criar(any(VacinaRequestDTO.class)))
                .thenThrow(new DadoInvalidoException("A vacina 'V8' já está cadastrada."));

        mockMvc.perform(post("/api/vacinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A vacina 'V8' já está cadastrada."));
    }

    @Test
    void buscarPorId_encontrado_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(buildResponse(1L, "V8", 12));

        mockMvc.perform(get("/api/vacinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("V8"));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Vacina não encontrada."));

        mockMvc.perform(get("/api/vacinas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/vacinas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cadastrar_semNome_deveRetornar400() throws Exception {
        VacinaRequestDTO req = new VacinaRequestDTO();
        req.setNome(""); req.setDescricao("Desc");

        mockMvc.perform(post("/api/vacinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
