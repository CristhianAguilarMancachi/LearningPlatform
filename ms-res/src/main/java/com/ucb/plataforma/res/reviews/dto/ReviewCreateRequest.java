package com.ucb.plataforma.res.reviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "ReviewCreateRequest", description = "Payload para crear una reseña")
public class ReviewCreateRequest {

    @NotNull
    @Schema(description = "ID del curso reseñado", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseId;

    @Schema(description = "ID del usuario que reseña (opcional)", example = "55")
    private Long userId;

    @NotNull @Min(1) @Max(5)
    @Schema(description = "Puntaje de 1 a 5", example = "4", minimum = "1", maximum = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer rating;

    @Size(max = 150)
    @Schema(description = "", example = "Excelente curso")
    private String title;

    @NotBlank @Size(max = 2000)
    @Schema(description = "Comentario (máx 2000)", example = "El curso fue muy útil y bien explicado.")
    private String comment;

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
}
