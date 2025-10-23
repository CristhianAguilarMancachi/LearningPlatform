package com.ucb.plataforma.res.reviews.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Entity
@Table(name = "review")
@Schema(name = "Review", description = "Reseña de un curso")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado de la reseña", example = "123")
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "ID del curso reseñado", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseId;

    @Schema(description = "ID del usuario que reseña (opcional)", example = "55")
    private Long userId;

    @NotNull
    @Min(1) @Max(5)
    @Column(nullable = false)
    @Schema(description = "Puntaje de 1 a 5", example = "4", minimum = "1", maximum = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer rating;

    @Size(max = 150)
    @Schema(description = "Título breve de la reseña (máx 150 caracteres)", example = "Excelente curso")
    private String title;

    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    @Schema(description = "Comentario detallado (máx 2000 caracteres)", 
            example = "El curso fue muy útil y bien explicado.")
    private String comment;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Fecha de creación (UTC)", example = "2025-08-28T12:00:00Z")
    private Instant createdAt;

    @Column(nullable = false)
    @Schema(description = "Fecha de última actualización (UTC)", example = "2025-08-28T12:00:00Z")
    private Instant updatedAt;

    // 🔹 Constructor vacío obligatorio para JPA
    public Review() {}

    // 🔹 Constructor completo
    public Review(Long id, Long courseId, Long userId, Integer rating,
                  String title, String comment, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 🔹 Hooks automáticos de timestamps
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // 🔹 Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
