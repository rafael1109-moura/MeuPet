package com.meupet.util;

import com.meupet.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class SecurityUtil {

    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Usuario usuario)) {
            throw new NoSuchElementException("Usuário não autenticado.");
        }
        return usuario;
    }
}