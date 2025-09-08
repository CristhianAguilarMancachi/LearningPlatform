package com.example.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para operaciones de usuario")
public class UsuarioResponse {

    @Schema(description = "ID único autogenerado del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Jose Nunez")
    private String nombre;

    @Schema(description = "Dirección de correo electrónico del usuario", example = "jose.nu@gmail.com")
    private String email;

    @Schema(description = "Rol del usuario en el sistema", example = "USER")
    private String rol;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String nombre, String email, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "UsuarioResponse{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}