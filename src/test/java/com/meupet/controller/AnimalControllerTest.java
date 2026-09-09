package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.AnimalRequestDTO;
import com.meupet.dto.AnimalRequestDTO.TipoAnimal;
import com.meupet.dto.AnimalResponseDTO;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.service.AnimalService;
import com.meupet.service.TarefaService;
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

@WebMvcTest(AnimalController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AnimalControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private AnimalService service;
    @MockitoBean private TarefaService tarefaService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private AnimalResponseDTO buildCachorroResponse(Long id, String nome) {
        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.setId(id); dto.setNome(nome); dto.setTipo(TipoAnimal.CACHORRO);
        dto.setRacaCachorro(RacaCachorro.Golden_Retriever);
        dto.setIdade(3); dto.setSexo(Sexo.MACHO); dto.setPeso(12.5f);
        return dto;
    }

    @Test
    void criar_cachorro_deveRetornar201() throws Exception {
        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.CACHORRO); req.setNome("Thor"); req.setIdade(3);
        req.setRacaCachorro(RacaCachorro.Golden_Retriever); req.setPeso(12.5f);

        when(service.criar(any(AnimalRequestDTO.class)))
                .thenReturn(buildCachorroResponse(1L, "Thor"));

        mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("CACHORRO"));
    }

    @Test
    void criar_tipoInvalido_deveRetornar400() throws Exception {
        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(null); req.setNome("X");

        mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_encontrado_deveRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(buildCachorroResponse(1L, "Thor"));

        mockMvc.perform(get("/api/animais/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Thor"))
                .andExpect(jsonPath("$.tipo").value("CACHORRO"));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Animal não encontrado."));

        mockMvc.perform(get("/api/animais/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/animais/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void atualizar_cachorroDeGato_deveRetornar400() throws Exception {
        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.CACHORRO); req.setNome("Mingau"); req.setRacaCachorro(RacaCachorro.SRD);

        when(service.atualizar(any(Long.class), any(AnimalRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Tipo do animal informado não corresponde ao cadastro."));

        mockMvc.perform(put("/api/animais/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
