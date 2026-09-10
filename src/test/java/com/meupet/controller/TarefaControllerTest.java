package com.meupet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meupet.dto.TarefaRequestDTO;
import com.meupet.dto.TarefaResponseDTO;
import com.meupet.dto.TarefaResumoDTO;
import com.meupet.model.Tarefa.Categoria;
import com.meupet.model.Tarefa.Prioridade;
import com.meupet.service.TarefaService;
import com.meupet.util.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TarefaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private TarefaService service;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private TarefaResponseDTO buildResponse(Long id, String titulo, boolean concluida) {
        TarefaResponseDTO dto = new TarefaResponseDTO();
        dto.setId(id); dto.setTitulo(titulo); dto.setCategoria(Categoria.PESSOAL);
        dto.setPrioridade(Prioridade.MEDIA); dto.setDataPrevista(LocalDate.of(2026, 9, 16));
        dto.setConcluida(concluida); dto.setAnimalId(1L); dto.setAnimalNome("Thor");
        return dto;
    }

    @Test
    void criar_deveRetornar201() throws Exception {
        TarefaRequestDTO req = new TarefaRequestDTO();
        req.setTitulo("Passear"); req.setCategoria(Categoria.PESSOAL);
        req.setDataPrevista(LocalDate.of(2026, 9, 16)); req.setAnimalId(1L);

        when(service.criar(any(TarefaRequestDTO.class)))
                .thenReturn(buildResponse(1L, "Passear", false));

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Passear"))
                .andExpect(jsonPath("$.concluida").value(false));
    }

    @Test
    void criar_tituloVazio_deveRetornar400() throws Exception {
        TarefaRequestDTO req = new TarefaRequestDTO();
        req.setTitulo(""); req.setCategoria(Categoria.PESSOAL);
        req.setDataPrevista(LocalDate.of(2026, 9, 16));

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criar_semCategoria_deveRetornar400() throws Exception {
        TarefaRequestDTO req = new TarefaRequestDTO();
        req.setTitulo("OK"); req.setCategoria(null);
        req.setDataPrevista(LocalDate.of(2026, 9, 16));

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void concluir_deveRetornar200ComDataConclusao() throws Exception {
        TarefaResponseDTO resp = buildResponse(1L, "X", true);
        resp.setDataConclusao(LocalDate.now());

        when(service.concluir(1L)).thenReturn(resp);

        mockMvc.perform(patch("/api/tarefas/1/concluir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concluida").value(true))
                .andExpect(jsonPath("$.dataConclusao").value(LocalDate.now().toString()));
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornar404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Tarefa não encontrada."));

        mockMvc.perform(get("/api/tarefas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void resumo_deveRetornar200ComContadores() throws Exception {
        when(service.resumo(isNull()))
                .thenReturn(new TarefaResumoDTO(4, 4, 1, 0, 3));

        mockMvc.perform(get("/api/tarefas/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(4))
                .andExpect(jsonPath("$.atrasadas").value(1));
    }

    @Test
    void listar_deveRetornar200ComPagina() throws Exception {
        Page<TarefaResponseDTO> page = new PageImpl<>(
                java.util.List.of(buildResponse(1L, "Passear", false)),
                PageRequest.of(0, 10), 1);
        when(service.listar(isNull(), isNull(), isNull(), isNull(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Passear"));
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/tarefas/1"))
                .andExpect(status().isNoContent());
    }
}
