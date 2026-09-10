package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.CachorroRequestDTO;
import com.meupet.dto.CachorroResponseDTO;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.service.CachorroService;
import com.meupet.util.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CachorroController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class CachorroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CachorroService service;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private CachorroResponseDTO buildResponse(Long id, String nome, RacaCachorro raca) {
        CachorroResponseDTO dto = new CachorroResponseDTO();
        dto.setId(id); dto.setNome(nome); dto.setRaca(raca); dto.setIdade(3);
        dto.setSexo(Sexo.MACHO); dto.setPeso(12.5f);
        dto.setDataLastBanho(LocalDate.of(2026, 5, 10));
        dto.setDataLastTosa(LocalDate.of(2026, 4, 15));
        dto.setDataUltimoPasseio(LocalDate.of(2026, 5, 16));
        return dto;
    }

    @Test
    void criar_deveRetornar201ComJson() throws Exception {
        CachorroRequestDTO req = new CachorroRequestDTO();
        req.setNome("Rex"); req.setIdade(3); req.setRaca(RacaCachorro.Golden_Retriever);
        req.setSexo(Sexo.MACHO); req.setPeso(25.0f);

        when(service.criar(any(CachorroRequestDTO.class)))
                .thenReturn(buildResponse(1L, "Rex", RacaCachorro.Golden_Retriever));

        mockMvc.perform(post("/api/cachorros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Rex"))
                .andExpect(jsonPath("$.raca").value("Golden_Retriever"));
    }

    @Test
    void criar_dadosInvalidos_deveRetornar400() throws Exception {
        CachorroRequestDTO req = new CachorroRequestDTO();
        req.setNome(""); req.setIdade(null); req.setRaca(null);

        mockMvc.perform(post("/api/cachorros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_encontrado_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(buildResponse(1L, "Rex", RacaCachorro.SRD));

        mockMvc.perform(get("/api/cachorros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rex"));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Cachorro não encontrado."));

        mockMvc.perform(get("/api/cachorros/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cachorro não encontrado."));
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/cachorros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void atualizar_deveRetornar200ComDadosAtualizados() throws Exception {
        CachorroRequestDTO req = new CachorroRequestDTO();
        req.setNome("Rex Jr"); req.setIdade(4); req.setRaca(RacaCachorro.Pinscher);
        req.setPeso(15.0f);

        when(service.atualizar(any(Long.class), any(CachorroRequestDTO.class)))
                .thenReturn(buildResponse(1L, "Rex Jr", RacaCachorro.Pinscher));

        mockMvc.perform(put("/api/cachorros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rex Jr"));
    }
}
