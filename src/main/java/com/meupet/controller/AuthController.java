package com.meupet.controller;

import com.meupet.dto.LoginRequestDTO;
import com.meupet.dto.LoginResponseDTO;
import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Usuario;
import com.meupet.service.UsuarioService;
import com.meupet.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de login e registro")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário e obter token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Usuario usuario = usuarioService.autenticar(request.getEmail(), request.getSenha())
                .orElseThrow(() -> new NoSuchElementException("E-mail ou senha inválidos."));
        return ResponseEntity.ok(montarResposta(usuario));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar um novo usuário e obter token JWT")
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO request)
            throws DadoInvalidoException {
        Usuario usuario = usuarioService.criarEntidade(request);
        return ResponseEntity.ok(montarResposta(usuario));
    }

    private LoginResponseDTO montarResposta(Usuario usuario) {
        return new LoginResponseDTO(jwtUtil.gerarToken(usuario), usuario.getId(),
                usuario.getNome(), usuario.getEmail());
    }
}