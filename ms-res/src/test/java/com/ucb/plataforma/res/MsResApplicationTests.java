package com.ucb.plataforma.res;

import com.ucb.plataforma.res.reviews.controller.ReviewController;
import com.ucb.plataforma.res.reviews.dto.ReviewResponse;
import com.ucb.plataforma.res.reviews.exception.GlobalExceptionHandler;
import com.ucb.plataforma.res.reviews.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean; // <- deprecado pero válido
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(controllers = ReviewController.class)
@Import(GlobalExceptionHandler.class)
class ReviewControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReviewService reviewService;

    @Test
    void getAll_shouldReturn200AndList() {
        // Simulamos la respuesta del servicio
        ReviewResponse r = new ReviewResponse();
        r.setId(1L);
        r.setCourseId(101L);
        r.setUserId(1L);
        r.setRating(5);
        r.setTitle("Excelente");
        r.setComment("Muy claro.");
        r.setCreatedAt("2025-08-28T12:00:00Z");
        r.setUpdatedAt("2025-08-28T12:00:00Z");

        Mockito.when(reviewService.findAll()).thenReturn(List.of(r));

        // Ejecutamos la llamada y validamos resultado
        client.get().uri("/reviews")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].courseId").isEqualTo(101)
                .jsonPath("$[0].rating").isEqualTo(5);
    }

	@Test
	void create_invalidPayload_shouldReturn400() {
		// JSON inválido: rating=6 (fuera de 1..5) y falta el campo obligatorio "comment"
		String invalidJson = """
				{
				"courseId": 101,
				"rating": 6,
				"title": "x"
				}
				""";

		client.post().uri("/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(invalidJson)
				.exchange()
				.expectStatus().isBadRequest()
				// Espera el media type del RFC 7807
				.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
				.expectBody()
				.jsonPath("$.status").isEqualTo(400)
				.jsonPath("$.title").isEqualTo("Bad Request")
				.jsonPath("$.detail").isEqualTo("Payload inválido")
				.jsonPath("$.errors[0].field").exists(); // opcional: valida que haya detalle de errores
	}


}
