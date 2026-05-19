/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.service;

/**
 *
 * @author D410W
 */
import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.dto.UsuarioResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Usuario;
import com.meupet.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
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
                .collect(Collectors.toList());
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setSenha(usuario.getEmail());;

        return response;
    }
}
