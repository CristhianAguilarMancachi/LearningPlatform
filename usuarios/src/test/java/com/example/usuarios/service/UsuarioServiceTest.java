package com.example.usuarios.service;

import com.example.usuarios.exception.UsuarioNotFoundException;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testObtenerUsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Jose");
        usuario.setEmail("juan@gmail.com");
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Jose", resultado.getNombre());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void testObtenerUsuarioNoExistente() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.obtenerUsuario(999L);
        });
    }

    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Maria García");
        usuario.setEmail("maria@gmail.com");
        usuario.setPassword("123456");
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("Maria García", resultado.getNombre());
        verify(usuarioRepository).save(usuario);
    }
}
