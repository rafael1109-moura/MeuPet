package com.meupet.service;

import com.meupet.dto.AnimalRequestDTO;
import com.meupet.dto.AnimalRequestDTO.TipoAnimal;
import com.meupet.dto.AnimalResponseDTO;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.model.Gato;
import com.meupet.model.Gato.RacaGato;
import com.meupet.model.Usuario;
import com.meupet.repository.AnimalRepository;
import com.meupet.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.NoSuchElementException;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository repository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private AnimalService service;

    private Usuario usuarioLogado() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        return usuario;
    }

    @Test
    void criar_tipoCachorro_deveSalvarCachorro() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.CACHORRO); req.setNome("Thor"); req.setIdade(3);
        req.setRacaCachorro(RacaCachorro.Golden_Retriever);
        req.setSexo(Sexo.MACHO); req.setPeso(12.5f);

        Cachorro salvo = new Cachorro();
        ReflectionTestUtils.setField(salvo, "id", 1L); salvo.setNome("Thor");
        salvo.setRaca(RacaCachorro.Golden_Retriever);
        when(repository.save(any(Cachorro.class))).thenReturn(salvo);

        AnimalResponseDTO res = service.criar(req);

        assertEquals("Thor", res.getNome());
        assertEquals(TipoAnimal.CACHORRO, res.getTipo());
        assertEquals(RacaCachorro.Golden_Retriever, res.getRacaCachorro());
    }

    @Test
    void criar_tipoGato_deveSalvarGato() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.GATO); req.setNome("Mingau"); req.setIdade(2);
        req.setRacaGato(RacaGato.Siames);

        Gato salvo = new Gato();
        ReflectionTestUtils.setField(salvo, "id", 3L); salvo.setNome("Mingau");
        salvo.setRaca(RacaGato.Siames);
        when(repository.save(any(Gato.class))).thenReturn(salvo);

        AnimalResponseDTO res = service.criar(req);

        assertEquals("Mingau", res.getNome());
        assertEquals(TipoAnimal.GATO, res.getTipo());
        assertEquals(RacaGato.Siames, res.getRacaGato());
    }

    @Test
    void criar_tipoInvalido_deveLancarExcecao() {
        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(null); req.setNome("X"); req.setIdade(1);

        assertThrows(IllegalArgumentException.class, () -> service.criar(req));
    }

    @Test
    void criar_cachorroSemRaca_deveLancarExcecao() {
        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.CACHORRO); req.setNome("Rex"); req.setIdade(2);
        req.setRacaCachorro(null);

        assertThrows(IllegalArgumentException.class, () -> service.criar(req));
    }

    @Test
    void criar_gatoSemRaca_deveLancarExcecao() {
        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.GATO); req.setNome("Minha"); req.setIdade(1);
        req.setRacaGato(null);

        assertThrows(IllegalArgumentException.class, () -> service.criar(req));
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());
        when(repository.findByIdAndUsuarioId(99L, 1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void atualizar_cachorroDeGato_deveLancarExcecao() {
        when(securityUtil.getUsuarioLogado()).thenReturn(usuarioLogado());

        Gato gato = new Gato();
        ReflectionTestUtils.setField(gato, "id", 1L); gato.setNome("Miau");
        when(repository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(gato));

        AnimalRequestDTO req = new AnimalRequestDTO();
        req.setTipo(TipoAnimal.CACHORRO); req.setNome("Miau");
        req.setRacaCachorro(RacaCachorro.SRD);

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(1L, req));
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