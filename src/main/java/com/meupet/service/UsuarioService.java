package com.meupet.service;

import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.dto.UsuarioResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Usuario;
import com.meupet.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
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
        String senhaCripto = passwordEncoder.encode(request.getSenha());

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(senhaCripto);

        Usuario salva = repository.save(usuario);
        
        return converterParaDTO(salva);
    }

    public List<UsuarioResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            return usuario;
        }
        return Optional.empty();
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());

        return response;
    }
}
