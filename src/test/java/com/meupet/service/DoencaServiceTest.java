package com.meupet.service;

import com.meupet.dto.DoencaRequestDTO;
import com.meupet.dto.DoencaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Doenca;
import com.meupet.repository.DoencaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.NoSuchElementException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoencaServiceTest {

    @Mock
    private DoencaRepository repository;

    @InjectMocks
    private DoencaService service;

    @Test
    void criar_deveSalvar() throws DadoInvalidoException {
        when(repository.findByNomeIgnoreCase("Raiva")).thenReturn(Optional.empty());
        Doenca salva = new Doenca(1L, "Raiva", "Zoonose", "Prevenção");
        when(repository.save(any(Doenca.class))).thenReturn(salva);

        DoencaRequestDTO req = new DoencaRequestDTO();
        req.setNome("Raiva"); req.setDescricao("Zoonose"); req.setTratamento("Prevenção");

        DoencaResponseDTO res = service.criar(req);

        assertEquals("Raiva", res.getNome());
        assertEquals("Zoonose", res.getDescricao());
        assertEquals("Prevenção", res.getTratamento());
    }

    @Test
    void criar_nomeDuplicado_deveLancarExcecao() {
        Doenca existente = new Doenca(1L, "Raiva", "Zoonose", "Prevenção");
        when(repository.findByNomeIgnoreCase("Raiva")).thenReturn(Optional.of(existente));

        DoencaRequestDTO req = new DoencaRequestDTO();
        req.setNome("Raiva"); req.setDescricao("X");

        assertThrows(DadoInvalidoException.class, () -> service.criar(req));
    }

    @Test
    void atualizar_excluirProprioNome_deveFuncionar() throws DadoInvalidoException {
        Doenca existente = new Doenca(1L, "Raiva", "Zoonose", "Prevenção");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.findByNomeIgnoreCase("Raiva")).thenReturn(Optional.of(existente));
        when(repository.save(any(Doenca.class))).thenAnswer(inv -> inv.getArgument(0));

        DoencaRequestDTO req = new DoencaRequestDTO();
        req.setNome("Raiva"); req.setDescricao("Zoonose atualizada"); req.setTratamento("Tratamento atualizado");

        DoencaResponseDTO res = service.atualizar(1L, req);

        assertEquals("Raiva", res.getNome());
        assertEquals("Zoonose atualizada", res.getDescricao());
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deletar_deveChamarRepositoryDelete() {
        Doenca d = new Doenca(1L, "Raiva", "Zoonose", "Prevenção");
        when(repository.findById(1L)).thenReturn(Optional.of(d));

        service.deletar(1L);

        verify(repository).delete(d);
    }
}
