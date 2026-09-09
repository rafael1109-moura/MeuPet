package com.meupet.service;

import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.dto.UsuarioResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Usuario;
import com.meupet.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO criar(UsuarioRequestDTO request) throws DadoInvalidoException {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new DadoInvalidoException("O usuário '" + request.getEmail() + "' já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        return converterParaDTO(repository.save(usuario));
    }

    public Page<UsuarioResponseDTO> listarTodas(Pageable pageable) {
        return repository.findAll(pageable).map(this::converterParaDTO);
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarUsuario(id));
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO request) throws DadoInvalidoException {
        Usuario usuario = buscarUsuario(id);

        Optional<Usuario> existente = repository.findByEmail(request.getEmail());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new DadoInvalidoException("O usuário '" + request.getEmail() + "' já está cadastrado.");
        }

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        return converterParaDTO(repository.save(usuario));
    }

    public void deletar(Long id) {
        repository.delete(buscarUsuario(id));
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            return usuario;
        }
        return Optional.empty();
    }

    private Usuario buscarUsuario(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário com id " + id + " não encontrado."));
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        return response;
    }
}