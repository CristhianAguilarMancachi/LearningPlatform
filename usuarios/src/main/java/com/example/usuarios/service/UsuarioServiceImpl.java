package com.example.usuarios.service;

import com.example.usuarios.model.Usuario;
import java.util.List;

public interface UsuarioServiceImpl {
    Usuario crearUsuario(Usuario usuario);
    List<Usuario> listarUsuarios();
    Usuario obtenerUsuario(Long id);
    Usuario actualizarUsuario(Long id, Usuario usuario);
    void eliminarUsuario(Long id);
}