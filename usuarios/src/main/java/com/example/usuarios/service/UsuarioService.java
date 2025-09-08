package com.example.usuarios.service;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UsuarioServiceImpl {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioDetails) {
        Usuario usuario = obtenerUsuario(id);
        
        // Validar que el nuevo email no exista (si se está cambiando)
        if (usuarioDetails.getEmail() != null && 
            !usuarioDetails.getEmail().equals(usuario.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioDetails.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + usuarioDetails.getEmail());
        }

        // Actualizar solo los campos que no son nulos
        if (usuarioDetails.getNombre() != null) {
            usuario.setNombre(usuarioDetails.getNombre());
        }
        if (usuarioDetails.getEmail() != null) {
            usuario.setEmail(usuarioDetails.getEmail());
        }
        if (usuarioDetails.getPassword() != null) {
            usuario.setPassword(usuarioDetails.getPassword());
        }
        if (usuarioDetails.getRol() != null) {
            usuario.setRol(usuarioDetails.getRol());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Método adicional para verificar existencia por email
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}