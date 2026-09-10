package com.meupet.service;

import com.meupet.dto.CachorroRequestDTO;
import com.meupet.dto.CachorroResponseDTO;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.model.Usuario;
import com.meupet.repository.CachorroRepository;
import com.meupet.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.NoSuchElementException;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachorroServiceTest {

    @Mock
    private CachorroRepository repository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private CachorroService service;

    private Usuario usuarioLogado() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        return usuario;
    }

    @Test
    void criar_deveSalvarTodosOsDados() throws Exception {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        CachorroRequestDTO req = new CachorroRequestDTO();
        req.setNome("Rex"); req.setIdade(3); req.setRaca(RacaCachorro.Golden_Retriever);
        req.setSexo(Sexo.MACHO); req.setPeso(25.0f); req.setSujo(true); req.setCastrado(false);

        Cachorro salvo = new Cachorro();
        ReflectionTestUtils.setField(salvo, "id", 1L); salvo.setNome("Rex"); salvo.setIdade(3);
        salvo.setRaca(RacaCachorro.Golden_Retriever);
        salvo.setSexo(Sexo.MACHO); salvo.setPeso(25.0f); salvo.setSujo(true);
        when(repository.save(any(Cachorro.class))).thenReturn(salvo);

        CachorroResponseDTO res = service.criar(req);

        assertEquals("Rex", res.getNome());
        assertEquals(RacaCachorro.Golden_Retriever, res.getRaca());
        assertTrue(res.isSujo());
        assertFalse(res.isCastrado());
        ArgumentCaptor<Cachorro> captor = ArgumentCaptor.forClass(Cachorro.class);
        verify(repository).save(captor.capture());
        assertEquals(Sexo.MACHO, captor.getValue().getSexo());
        assertEquals(25.0f, captor.getValue().getPeso());
    }

    @Test
    void buscarPorId_encontrado_deveRetornarDTO() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        Cachorro cachorro = new Cachorro();
        ReflectionTestUtils.setField(cachorro, "id", 1L); cachorro.setNome("Rex");
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(cachorro));

        CachorroResponseDTO res = service.buscarPorId(1L);

        assertEquals("Rex", res.getNome());
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());
        when(repository.findByIdAndUsuarioId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void atualizar_deveAtualizarCampos() throws Exception {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        Cachorro existente = new Cachorro();
        ReflectionTestUtils.setField(existente, "id", 1L); existente.setNome("Rex"); existente.setRaca(RacaCachorro.SRD);
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Cachorro.class))).thenAnswer(inv -> inv.getArgument(0));

        CachorroRequestDTO req = new CachorroRequestDTO();
        req.setNome("Rex Jr"); req.setIdade(4); req.setRaca(RacaCachorro.Pinscher);
        req.setPeso(15.0f);

        CachorroResponseDTO res = service.atualizar(1L, req);

        assertEquals("Rex Jr", res.getNome());
        assertEquals(4, res.getIdade());
        assertEquals(RacaCachorro.Pinscher, res.getRaca());
    }

    @Test
    void deletar_deveChamarRepositoryDelete() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        Cachorro cachorro = new Cachorro();
        ReflectionTestUtils.setField(cachorro, "id", 1L);
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(cachorro));

        service.deletar(1L);

        verify(repository).delete(cachorro);
    }
}