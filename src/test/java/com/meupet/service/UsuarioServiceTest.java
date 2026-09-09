package com.meupet.service;

import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.dto.UsuarioResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Usuario;
import com.meupet.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    void criar_deveSalvarComSenhHash() throws DadoInvalidoException {
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha12345")).thenReturn("$2a$hashed");
        Usuario salvo = new Usuario();
        salvo.setId(1L); salvo.setNome("A"); salvo.setEmail("a@b.com"); salvo.setSenha("$2a$hashed");
        when(repository.save(any(Usuario.class))).thenReturn(salvo);

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("A"); req.setEmail("a@b.com"); req.setSenha("senha12345");

        UsuarioResponseDTO res = service.criar(req);

        assertEquals(1L, res.getId());
        assertEquals("A", res.getNome());
        assertEquals("a@b.com", res.getEmail());
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertEquals("$2a$hashed", captor.getValue().getSenha());
    }

    @Test
    void criar_emailDuplicado_deveLancarExcecao() {
        Usuario existente = new Usuario();
        existente.setId(1L); existente.setEmail("a@b.com");
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.of(existente));

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("B"); req.setEmail("a@b.com"); req.setSenha("senha12345");

        assertThrows(DadoInvalidoException.class, () -> service.criar(req));
    }

    @Test
    void atualizar_senhaEmBranco_deveManterHashAntigo() throws DadoInvalidoException {
        Usuario usuario = new Usuario();
        usuario.setId(1L); usuario.setNome("A"); usuario.setEmail("a@b.com"); usuario.setSenha("hashAntigo");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("A atualizado"); req.setEmail("a@b.com"); req.setSenha("");

        UsuarioResponseDTO res = service.atualizar(1L, req);

        assertEquals("A atualizado", res.getNome());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void atualizar_senhaInformada_deveReHash() throws DadoInvalidoException {
        Usuario usuario = new Usuario();
        usuario.setId(1L); usuario.setNome("A"); usuario.setEmail("a@b.com"); usuario.setSenha("oldHash");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nova12345")).thenReturn("$2a$newHash");
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNome("A"); req.setEmail("a@b.com"); req.setSenha("nova12345");

        service.atualizar(1L, req);

        verify(passwordEncoder).encode("nova12345");
    }

    @Test
    void autenticar_credenciaisValidas_deveRetornarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L); usuario.setEmail("a@b.com"); usuario.setSenha("$2a$hash");
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha", "$2a$hash")).thenReturn(true);

        assertTrue(service.autenticar("a@b.com", "senha").isPresent());
    }

    @Test
    void autenticar_senhaInvalida_deveRetornarVazio() {
        Usuario usuario = new Usuario();
        usuario.setId(1L); usuario.setEmail("a@b.com"); usuario.setSenha("$2a$hash");
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "$2a$hash")).thenReturn(false);

        assertTrue(service.autenticar("a@b.com", "errada").isEmpty());
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancarExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void listarTodas_deveRetornarPagina() {
        Usuario u = new Usuario();
        u.setId(1L); u.setNome("A"); u.setEmail("a@b.com");
        Page<Usuario> page = new PageImpl<>(List.of(u), PageRequest.of(0, 10), 1);
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<UsuarioResponseDTO> res = service.listarTodas(PageRequest.of(0, 10));

        assertEquals(1, res.getTotalElements());
        assertEquals("a@b.com", res.getContent().get(0).getEmail());
    }
}
