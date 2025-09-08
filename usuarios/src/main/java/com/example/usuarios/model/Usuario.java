package com.example.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autogenerado del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", 
             example = "Jose Nunez", 
             requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(unique = true, nullable = false)
    @Schema(description = "Dirección de correo electrónico única del usuario", 
             example = "jose.nu@gmail.com", 
             requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contraseña del usuario (se almacenará encriptada)", 
             example = "12345678", 
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 6)
    private String password;

    @Schema(description = "Rol o permisos del usuario en el sistema", 
             example = "ADMIN", 
             allowableValues = {"ADMIN", "USER", "MODERATOR"})
    private String rol; 

    public Usuario() {
    }

    public Usuario(String nombre, String email, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}