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

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public UsuarioResponseDTO criar(UsuarioRequestDTO request) throws DadoInvalidoException {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new DadoInvalidoException("O usuário '" + request.getEmail() + "' já está cadastrado.");
        }

        Usuario usuario = new Usuario(null, request.getNome(), request.getEmail(), request.getSenha());
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
        response.setSenha(usuario.getSenha());
        return response;
    }
}
