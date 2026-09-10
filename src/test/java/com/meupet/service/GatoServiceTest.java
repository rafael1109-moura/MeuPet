package com.meupet.service;

import com.meupet.dto.GatoRequestDTO;
import com.meupet.dto.GatoResponseDTO;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Gato;
import com.meupet.model.Gato.RacaGato;
import com.meupet.model.Usuario;
import com.meupet.repository.GatoRepository;
import com.meupet.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.NoSuchElementException;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatoServiceTest {

    @Mock
    private GatoRepository repository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private GatoService service;

    private Usuario usuarioLogado() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        return usuario;
    }

    @Test
    void criar_deveSalvarComTodosOsDados() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        GatoRequestDTO req = new GatoRequestDTO();
        req.setNome("Miau"); req.setIdade(2); req.setRaca(RacaGato.Siames);
        req.setSexo(Sexo.FEMEA); req.setPeso(4.0f); req.setAreiaSuja(true); req.setCastrado(true);

        Gato salvo = new Gato();
        ReflectionTestUtils.setField(salvo, "id", 1L); salvo.setNome("Miau"); salvo.setIdade(2);
        salvo.setRaca(RacaGato.Siames); salvo.setAreiaSuja(true); salvo.setCastrado(true); salvo.setSujo(true);
        when(repository.save(any(Gato.class))).thenReturn(salvo);

        GatoResponseDTO res = service.criar(req);

        assertEquals("Miau", res.getNome());
        assertEquals(RacaGato.Siames, res.getRaca());
        assertTrue(res.isAreiaSuja());
        assertTrue(res.isCastrado());
        ArgumentCaptor<Gato> captor = ArgumentCaptor.forClass(Gato.class);
        verify(repository).save(captor.capture());
        assertEquals(Sexo.FEMEA, captor.getValue().getSexo());
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());
        when(repository.findByIdAndUsuarioId(99L, 1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deletar_deveChamarRepositoryDelete() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        Gato gato = new Gato();
        ReflectionTestUtils.setField(gato, "id", 1L);
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(gato));

        service.deletar(1L);

        verify(repository).delete(gato);
    }

    @Test
    void listarTodos_deveRetornarPagina() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        Gato g = new Gato();
        ReflectionTestUtils.setField(g, "id", 1L); g.setNome("Miau"); g.setRaca(RacaGato.Persa);
        Page<Gato> page = new PageImpl<>(List.of(g), PageRequest.of(0, 10), 1);
        when(repository.findAllByUsuarioId(1L, PageRequest.of(0, 10))).thenReturn(page);

        Page<GatoResponseDTO> res = service.listarTodos(PageRequest.of(0, 10));

        assertEquals(1, res.getTotalElements());
        assertEquals(RacaGato.Persa, res.getContent().get(0).getRaca());
    }
}