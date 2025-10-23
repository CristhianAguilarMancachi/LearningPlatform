package com.ucb.plataforma.res.reviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "ReviewUpdateRequest", description = "Payload para actualizar una reseña (parcial)")
public class ReviewUpdateRequest {

    @Min(1) @Max(5)
    @Schema(description = "Nuevo puntaje (1..5)", example = "4", minimum = "1", maximum = "5")
    private Integer rating;

    @Size(max = 150)
    @Schema(description = "Nuevo título (máx 150)", example = "Muy bueno")
    private String title;

    @Size(max = 2000)
    @Schema(description = "Nuevo comentario (máx 2000)", example = "Corrijo: muy bueno, faltaron ejemplos.")
    private String comment;

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
