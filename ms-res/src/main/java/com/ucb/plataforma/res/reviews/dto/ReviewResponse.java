package com.ucb.plataforma.res.reviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReviewResponse", description = "Respuesta pública de una reseña")
public class ReviewResponse {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del curso", example = "101")
    private Long courseId;

    @Schema(description = "ID del usuario (opcional)", example = "55")
    private Long userId;

    @Schema(description = "Puntaje (1..5)", example = "5", minimum = "1", maximum = "5")
    private Integer rating;

    @Schema(description = "Título", example = "Excelente")
    private String title;

    @Schema(description = "Comentario", example = "Muy claro el docente.")
    private String comment;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String updatedAt;

    // Getters & setters
    public Long getId() { return id; }
    public ReviewResponse setId(Long id) { this.id = id; return this; }
    public Long getCourseId() { return courseId; }
    public ReviewResponse setCourseId(Long courseId) { this.courseId = courseId; return this; }
    public Long getUserId() { return userId; }
    public ReviewResponse setUserId(Long userId) { this.userId = userId; return this; }
    public Integer getRating() { return rating; }
    public ReviewResponse setRating(Integer rating) { this.rating = rating; return this; }
    public String getTitle() { return title; }
    public ReviewResponse setTitle(String title) { this.title = title; return this; }
    public String getComment() { return comment; }
    public ReviewResponse setComment(String comment) { this.comment = comment; return this; }
    public String getCreatedAt() { return createdAt; }
    public ReviewResponse setCreatedAt(String createdAt) { this.createdAt = createdAt; return this; }
    public String getUpdatedAt() { return updatedAt; }
    public ReviewResponse setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; return this; }
}
