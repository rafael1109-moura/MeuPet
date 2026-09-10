package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.GatoRequestDTO;
import com.meupet.dto.GatoResponseDTO;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Gato.RacaGato;
import com.meupet.service.GatoService;
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

@WebMvcTest(GatoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class GatoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private GatoService service;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private GatoResponseDTO buildResponse(Long id, String nome, RacaGato raca) {
        GatoResponseDTO dto = new GatoResponseDTO();
        dto.setId(id); dto.setNome(nome); dto.setRaca(raca); dto.setIdade(2);
        dto.setAreiaSuja(false); dto.setCastrado(true); dto.setSexo(Sexo.FEMEA); dto.setPeso(4.0f);
        return dto;
    }

    @Test
    void criar_deveRetornar201() throws Exception {
        GatoRequestDTO req = new GatoRequestDTO();
        req.setNome("Miau"); req.setIdade(2); req.setRaca(RacaGato.Siames);
        req.setSexo(Sexo.FEMEA); req.setPeso(4.0f);

        when(service.criar(any(GatoRequestDTO.class)))
                .thenReturn(buildResponse(1L, "Miau", RacaGato.Siames));

        mockMvc.perform(post("/api/gatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.raca").value("Siames"));
    }

    @Test
    void buscarPorId_encontrado_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(buildResponse(1L, "Miau", RacaGato.Persa));

        mockMvc.perform(get("/api/gatos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Miau"));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Gato não encontrado."));

        mockMvc.perform(get("/api/gatos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/gatos/1"))
                .andExpect(status().isNoContent());
    }
}
