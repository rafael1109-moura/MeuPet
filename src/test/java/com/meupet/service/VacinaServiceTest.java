package com.meupet.service;

import com.meupet.dto.VacinaRequestDTO;
import com.meupet.dto.VacinaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Doenca;
import com.meupet.model.Vacina;
import com.meupet.repository.DoencaRepository;
import com.meupet.repository.VacinaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacinaServiceTest {

    @Mock
    private VacinaRepository repository;

    @Mock
    private DoencaRepository doencaRepository;

    @InjectMocks
    private VacinaService service;

    private VacinaRequestDTO novoRequest(String nome) {
        VacinaRequestDTO req = new VacinaRequestDTO();
        req.setNome(nome); req.setDescricao("Desc"); req.setDoencaId(1L); req.setPeriodicidadeMeses(12);
        return req;
    }

    @Test
    void criar_deveSalvarComPeriodicidade() throws DadoInvalidoException {
        when(repository.findByNomeIgnoreCase("V8")).thenReturn(Optional.empty());
        Doenca d = new Doenca(); d.setId(1L); d.setNome("Cinomose");
        when(doencaRepository.findById(1L)).thenReturn(Optional.of(d));
        Vacina salva = new Vacina(); salva.setId(1L); salva.setNome("V8");
        salva.setPeriodicidadeMeses(12); salva.setDoenca(d);
        when(repository.save(any(Vacina.class))).thenReturn(salva);

        VacinaResponseDTO res = service.criar(novoRequest("V8"));

        assertEquals("V8", res.getNome());
        assertEquals(12, res.getPeriodicidadeMeses());
        assertEquals(1L, res.getDoencaId());
        assertEquals("Cinomose", res.getDoencaNome());
        ArgumentCaptor<Vacina> captor = ArgumentCaptor.forClass(Vacina.class);
        verify(repository).save(captor.capture());
        assertEquals(12, captor.getValue().getPeriodicidadeMeses());
    }

    @Test
    void criar_nomeDuplicado_deveLancarExcecao() {
        Vacina existente = new Vacina();
        existente.setId(1L); existente.setNome("V8");
        when(repository.findByNomeIgnoreCase("V8")).thenReturn(Optional.of(existente));

        assertThrows(DadoInvalidoException.class, () -> service.criar(novoRequest("V8")));
    }

    @Test
    void criar_doencaNaoEncontrada_deveLancarExcecao() {
        when(repository.findByNomeIgnoreCase("V8")).thenReturn(Optional.empty());
        when(doencaRepository.findById(99L)).thenReturn(Optional.empty());

        VacinaRequestDTO req = novoRequest("V8");
        req.setDoencaId(99L);

        assertThrows(DadoInvalidoException.class, () -> service.criar(req));
    }

    @Test
    void atualizar_excluirProprioNomeNaoDeveLancarErro() throws DadoInvalidoException {
        Vacina existente = new Vacina();
        existente.setId(1L); existente.setNome("V8");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.findByNomeIgnoreCase("V8")).thenReturn(Optional.of(existente));
        Doenca d = new Doenca(); d.setId(1L);
        when(doencaRepository.findById(1L)).thenReturn(Optional.of(d));
        when(repository.save(any(Vacina.class))).thenAnswer(inv -> inv.getArgument(0));

        VacinaResponseDTO res = service.atualizar(1L, novoRequest("V8"));

        assertEquals("V8", res.getNome());
    }

    @Test
    void atualizar_nomeDeOutraVacina_deveLancarExcecao() {
        Vacina existente = new Vacina();
        existente.setId(1L); existente.setNome("V8");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Vacina outra = new Vacina();
        outra.setId(2L); outra.setNome("V3");
        when(repository.findByNomeIgnoreCase("V3")).thenReturn(Optional.of(outra));

        VacinaRequestDTO req = novoRequest("V3");
        assertThrows(DadoInvalidoException.class, () -> service.atualizar(1L, req));
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deletar_deveChamarRepositoryDelete() {
        Vacina v = new Vacina(); v.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(v));

        service.deletar(1L);

        verify(repository).delete(v);
    }
}
