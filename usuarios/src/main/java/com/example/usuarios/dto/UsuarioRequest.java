package com.example.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para creación y actualización de usuarios")
public class UsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", 
             example = "Jose Nunez", 
             requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Schema(description = "Dirección de correo electrónico única del usuario", 
             example = "jose.nu@gmail.com", 
             requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", 
             example = "123456", 
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 6)
    private String password;

    @Schema(description = "Rol o permisos del usuario en el sistema", 
             example = "USER", 
             allowableValues = {"ADMIN", "USER", "MODERATOR"},
             defaultValue = "USER")
    private String rol = "USER";

    public UsuarioRequest() {
    }

    public UsuarioRequest(String nombre, String email, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}