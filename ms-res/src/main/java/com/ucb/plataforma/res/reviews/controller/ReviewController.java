package com.ucb.plataforma.res.reviews.controller;

import com.ucb.plataforma.res.reviews.dto.ReviewCreateRequest;
import com.ucb.plataforma.res.reviews.dto.ReviewResponse;
import com.ucb.plataforma.res.reviews.dto.ReviewUpdateRequest;
import com.ucb.plataforma.res.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/reviews")
@Tag(name = "Review", description = "REST API para reseñas de cursos (dummy, sin BD)")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Obtener todas las reseñas", description = "Retorna una lista dummy de reseñas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ReviewResponse.class)),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"courseId\":101,\"userId\":1,\"rating\":5,\"title\":\"Excelente\",\"comment\":\"Muy claro.\",\"createdAt\":\"2025-08-28T12:00:00Z\",\"updatedAt\":\"2025-08-28T12:00:00Z\"}]"
                )
            )
        )
    })
    @GetMapping(produces = "application/json")
    public List<ReviewResponse> getAll() {
        // Posibles errores manejados por el GlobalExceptionHandler:
        // - 500 (si ocurre alguna excepción no controlada dentro del service)
        LOGGER.info("Listando reseñas (dummy)");
        return reviewService.findAll();
    }

    @Operation(summary = "Obtener reseña por ID", description = "Retorna una reseña dummy por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponse.class),
                examples = @ExampleObject(
                    value = "{\"id\":5,\"courseId\":101,\"userId\":1,\"rating\":5,\"title\":\"Reseña 5\",\"comment\":\"Comentario dummy\",\"createdAt\":\"2025-08-28T12:00:00Z\",\"updatedAt\":\"2025-08-28T12:00:00Z\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ReviewResponse getById(
        @Parameter(description = "ID de la reseña (>=1)", required = true, example = "5")
        @PathVariable @Min(1) Long id) {
        // Errores:
        // - 400 si id < 1 (ConstraintViolationException)
        // - 404 si service lanza NotFoundException
        // - 500 genérico si algo falla inesperadamente
        LOGGER.info("Obteniendo reseña id={} (dummy)", id);
        return reviewService.findById(id);
    }

    @Operation(summary = "Crear reseña", description = "Crea y retorna la reseña creada (dummy, no persiste)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Creada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta",
                    value = "{\"id\":100,\"courseId\":101,\"userId\":1,\"rating\":5,\"title\":\"Excelente\",\"comment\":\"Muy claro.\",\"createdAt\":\"2025-08-28T12:00:00Z\",\"updatedAt\":\"2025-08-28T12:00:00Z\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ReviewResponse create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la reseña. Requeridos: courseId, rating (1..5), comment (1..2000).",
            required = true,
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewCreateRequest.class),
                examples = @ExampleObject(
                    name = "Solicitud",
                    value = "{\"courseId\":101,\"userId\":1,\"rating\":5,\"title\":\"Excelente\",\"comment\":\"Muy claro.\"}"
                )
            )
        )
        @Valid @RequestBody ReviewCreateRequest request) {
        // Errores:
        // - 400 si el body viola validaciones (@Valid) -> WebExchangeBindException
        // - 415 si Content-Type no es application/json (por 'consumes')
        // - 400 si JSON mal formado/tipos erróneos -> ServerWebInputException
        LOGGER.info("Creando reseña (dummy)");
        return reviewService.create(request);
    }

    @Operation(summary = "Actualizar reseña", description = "Actualiza y retorna la reseña (dummy, no persiste)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Actualizada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ReviewResponse update(
        @Parameter(description = "ID de la reseña a actualizar (>=1)", required = true, example = "7")
        @PathVariable @Min(1) Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Campos a actualizar. rating 1..5; title máx 150; comment máx 2000.",
            required = true,
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewUpdateRequest.class),
                examples = @ExampleObject(value = "{\"rating\":4,\"comment\":\"Corrijo: muy bueno\"}")
            )
        )
        @Valid @RequestBody ReviewUpdateRequest request) {
        // Errores:
        // - 400 si id < 1 (ConstraintViolationException) o body inválido (WebExchangeBindException)
        // - 415 si Content-Type incorrecto
        // - 404 si service lanza NotFoundException
        LOGGER.info("Actualizando reseña id={} (dummy)", id);
        return reviewService.update(id, request);
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña por ID (dummy, no persiste)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Eliminada"),
        @ApiResponse(responseCode = "404", description = "No encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public void delete(
        @Parameter(description = "ID de la reseña a eliminar (>=1)", required = true, example = "9")
        @PathVariable @Min(1) Long id) {
        // Errores:
        // - 400 si id < 1
        // - 404 si service lanza NotFoundException
        LOGGER.info("Eliminando reseña id={} (dummy)", id);
        reviewService.delete(id);
    }
}
