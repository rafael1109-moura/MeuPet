package com.meupet.service;

import com.meupet.dto.TarefaRequestDTO;
import com.meupet.dto.TarefaResponseDTO;
import com.meupet.dto.TarefaResumoDTO;
import com.meupet.model.Animal;
import com.meupet.model.Tarefa;
import com.meupet.model.Tarefa.Categoria;
import com.meupet.model.Tarefa.Prioridade;
import com.meupet.model.Cachorro;
import com.meupet.model.Usuario;
import com.meupet.repository.AnimalRepository;
import com.meupet.repository.TarefaRepository;
import com.meupet.repository.VacinaRepository;
import com.meupet.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.NoSuchElementException;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock private TarefaRepository repository;
    @Mock private AnimalRepository animalRepository;
    @Mock private VacinaRepository vacinaRepository;
    @Mock private SecurityUtil securityUtil;
    @InjectMocks private TarefaService service;

    private Usuario usuarioLogado() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        return usuario;
    }

    private Tarefa tarefa(String titulo, LocalDate dataPrevista, boolean concluida) {
        Tarefa t = new Tarefa();
        t.setTitulo(titulo); t.setCategoria(Categoria.PESSOAL);
        t.setDataPrevista(dataPrevista); t.setConcluida(concluida);
        t.setPrioridade(Prioridade.MEDIA); t.setId(1L);
        return t;
    }

    @BeforeEach
    void login() {
        lenient().when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());
    }

    @Test
    void criar_deveSalvarComConcluidaFalse() {
        Animal animal = new Cachorro(); ReflectionTestUtils.setField(animal, "id", 1L); animal.setNome("Thor");
        when(animalRepository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(animal));
        Tarefa salva = tarefa("Passear", LocalDate.of(2026, 9, 16), false);
        salva.setId(10L);
        salva.setAnimal(animal);
        when(repository.save(any(Tarefa.class))).thenReturn(salva);

        TarefaRequestDTO req = new TarefaRequestDTO();
        req.setTitulo("Passear"); req.setCategoria(Categoria.PESSOAL);
        req.setDataPrevista(LocalDate.of(2026, 9, 16)); req.setAnimalId(1L);

        TarefaResponseDTO res = service.criar(req);

        assertFalse(res.isConcluida());
        assertEquals("Passear", res.getTitulo());
        assertEquals(1L, res.getAnimalId());
    }

    @Test
    void concluir_deveSetarDataConclusao() {
        Tarefa t = tarefa("X", LocalDate.of(2026, 9, 1), false);
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(t));
        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        TarefaResponseDTO res = service.concluir(1L);

        assertTrue(res.isConcluida());
        assertEquals(LocalDate.now(), res.getDataConclusao());
    }

    @Test
    void reabrir_deveLimparDataConclusao() {
        Tarefa t = tarefa("X", LocalDate.of(2026, 9, 1), true);
        t.setDataConclusao(LocalDate.of(2026, 8, 15));
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(t));
        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        TarefaResponseDTO res = service.reabrir(1L);

        assertFalse(res.isConcluida());
        assertNull(res.getDataConclusao());
    }

    @Test
    void criar_vacinaIdEmNaoVacinacao_deveLancarExcecao() {
        TarefaRequestDTO req = new TarefaRequestDTO();
        req.setTitulo("Test"); req.setCategoria(Categoria.PESSOAL);
        req.setDataPrevista(LocalDate.of(2026, 9, 1)); req.setVacinaId(2L);

        assertThrows(NoSuchElementException.class, () -> service.criar(req));
    }

    @Test
    void resumo_deveRetornarContadores() {
        when(animalRepository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(new Cachorro()));
        when(repository.buscarFiltradas(eq(1L), eq(1L), isNull(), isNull(), isNull(), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), Pageable.unpaged(), 3));
        when(repository.countByConcluidaFalseAndAnimalIdAndUsuarioId(1L, 1L)).thenReturn(3L);
        when(repository.countByConcluidaFalseAndDataPrevistaBeforeAndAnimalIdAndUsuarioId(LocalDate.now(), 1L, 1L)).thenReturn(1L);
        when(repository.countByConcluidaTrueAndAnimalIdAndUsuarioId(1L, 1L)).thenReturn(0L);
        when(repository.countByConcluidaFalseAndDataPrevistaGreaterThanEqualAndAnimalIdAndUsuarioId(
                        LocalDate.now(), 1L, 1L))
                .thenReturn(2L);

        TarefaResumoDTO res = service.resumo(1L);

        assertEquals(3, res.getTotal());
        assertEquals(3, res.getPendentes());
        assertEquals(1, res.getAtrasadas());
        assertEquals(0, res.getConcluidas());
        assertEquals(2, res.getProximas());
    }

    @Test
    void resumo_semAnimal_deveUsarContagensGlobaisDoUsuario() {
        when(repository.buscarFiltradas(eq(1L), isNull(), isNull(), isNull(), isNull(), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), Pageable.unpaged(), 3));
        when(repository.countByConcluidaFalseAndUsuarioId(1L)).thenReturn(3L);
        when(repository.countByConcluidaFalseAndDataPrevistaBeforeAndUsuarioId(LocalDate.now(), 1L)).thenReturn(1L);
        when(repository.countByConcluidaTrueAndUsuarioId(1L)).thenReturn(0L);
        when(repository.countByConcluidaFalseAndDataPrevistaGreaterThanEqualAndUsuarioId(LocalDate.now(), 1L)).thenReturn(2L);

        TarefaResumoDTO res = service.resumo(null);

        assertEquals(3, res.getTotal());
        assertEquals(3, res.getPendentes());
        assertEquals(1, res.getAtrasadas());
        assertEquals(0, res.getConcluidas());
        assertEquals(2, res.getProximas());
    }

    @Test
    void resumo_animalNaoEncontrado_deveLancarExcecao() {
        when(animalRepository.findByIdAndUsuarioId(99L, 1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.resumo(99L));
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(repository.findByIdAndUsuarioId(99L, 1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deletar_deveChamarRepositoryDelete() {
        Tarefa t = tarefa("X", LocalDate.now(), false);
        t.setId(5L);
        when(repository.findByIdAndUsuarioId(5L, 1L)).thenReturn(Optional.of(t));

        service.deletar(5L);

        verify(repository).delete(t);
    }
}