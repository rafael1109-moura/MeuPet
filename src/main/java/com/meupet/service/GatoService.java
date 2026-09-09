package com.meupet.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.meupet.dto.GatoRequestDTO;
import com.meupet.dto.GatoResponseDTO;
import com.meupet.model.Gato;
import com.meupet.model.Usuario;
import com.meupet.repository.GatoRepository;
import com.meupet.util.SecurityUtil;

@Service
public class GatoService {

    private final GatoRepository repository;
    private final SecurityUtil securityUtil;

    public GatoService(GatoRepository repository, SecurityUtil securityUtil) {
        this.repository = repository;
        this.securityUtil = securityUtil;
    }

    private Usuario usuarioLogado() {
        return securityUtil.getUsuarioLogado();
    }

    private Long usuarioId() {
        return usuarioLogado().getId();
    }

    public GatoResponseDTO criar(GatoRequestDTO request) {
        Gato gato = new Gato();
        preencher(gato, request);
        gato.setUsuario(usuarioLogado());

        return toResponse(repository.save(gato));
    }

    public Page<GatoResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAllByUsuarioId(usuarioId(), pageable).map(this::toResponse);
    }

    public GatoResponseDTO buscarPorId(Long id) {
        return toResponse(buscarGato(id));
    }

    public GatoResponseDTO atualizar(Long id, GatoRequestDTO request) {
        Gato gato = buscarGato(id);
        preencher(gato, request);

        return toResponse(repository.save(gato));
    }

    public void deletar(Long id) {
        repository.delete(buscarGato(id));
    }

    private Gato buscarGato(Long id) {
        return repository.findByIdAndUsuarioId(id, usuarioId())
                .orElseThrow(() -> new NoSuchElementException("Gato com id " + id + " não encontrado."));
    }

    private void preencher(Gato gato, GatoRequestDTO request) {
        gato.setNome(request.getNome());
        gato.setIdade(request.getIdade());
        gato.setRaca(request.getRaca());
        gato.setAreiaSuja(request.isAreiaSuja());
        gato.setSujo(request.isSujo());
        gato.setCastrado(request.isCastrado());
        if (request.getSexo() != null) {
            gato.setSexo(request.getSexo());
        }
        if (request.getPeso() != null) {
            gato.setPeso(request.getPeso());
        }
    }

    private GatoResponseDTO toResponse(Gato gato) {
        GatoResponseDTO response = new GatoResponseDTO();
        response.setId(gato.getId());
        response.setNome(gato.getNome());
        response.setIdade(gato.getIdade());
        response.setSexo(gato.getSexo());
        response.setPeso(gato.getPeso());
        response.setSujo(gato.isSujo());
        response.setCastrado(gato.isCastrado());
        response.setRaca(gato.getRaca());
        response.setAreiaSuja(gato.isAreiaSuja());
        return response;
    }
}