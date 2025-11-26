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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/reviews")
@Tag(name = "Review", description = "REST API for course reviews (with DB)")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Get all reviews", description = "Returns all reviews from the database")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ReviewResponse.class))))
    @GetMapping(produces = "application/json")
    public List<ReviewResponse> getAll() {
        LOGGER.info("Listing all reviews");
        return reviewService.findAll();
    }

    @Operation(summary = "Get review by ID", description = "Returns one review by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid ID",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ReviewResponse getById(
            @Parameter(description = "Review ID (>=1)", required = true, example = "5")
            @PathVariable @Min(1) Long id) {
        LOGGER.info("Getting review id={}", id);
        return reviewService.findById(id);
    }

    @Operation(summary = "Create review", description = "Creates and returns the new review")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Created",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ReviewResponse create(
            @Valid @RequestBody ReviewCreateRequest request) {
        LOGGER.info("Creating review for courseId={}", request.getCourseId());
        return reviewService.create(request);
    }

    @Operation(summary = "Update review", description = "Updates and returns the review by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ReviewResponse update(
            @Parameter(description = "Review ID (>=1)", required = true)
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ReviewUpdateRequest request) {
        LOGGER.info("Updating review id={}", id);
        return reviewService.update(id, request);
    }

    @Operation(summary = "Delete review", description = "Deletes a review by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deleted"),
        @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "Review ID (>=1)", required = true)
            @PathVariable @Min(1) Long id) {
        LOGGER.info("Deleting review id={}", id);
        reviewService.delete(id);
    }

    
    // ========================= NUEVOS ENDPOINTS ============================= //

    @Operation(summary = "Filter reviews by minimum rating",
            description = "Returns all reviews with rating >= minRating")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ReviewResponse.class))))
    @GetMapping(value = "/filter", produces = "application/json")
    public List<ReviewResponse> getReviewsByMinRating(
            @Parameter(description = "Minimum rating (1-5)", required = true, example = "4")
            @RequestParam int minRating) {
        LOGGER.info("Filtering reviews with minRating={}", minRating);
        return reviewService.findByMinRated(minRating);
    }

    @Operation(summary = "Search reviews by keyword",
            description = "Searches reviews that contain the given keyword in their comment")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ReviewResponse.class))))
    @GetMapping(value = "/search", produces = "application/json")
    public List<ReviewResponse> searchReviews(
            @Parameter(description = "Keyword to search in comments", example = "excelente")
            @RequestParam String keyword) {
        LOGGER.info("Searching reviews with keyword='{}'", keyword);
        return reviewService.searchByKeyword(keyword);
    }

    
    @Operation(summary = "Get all reviews paginated",
            description = "Returns reviews with pagination support")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)))
    @GetMapping(value = "/page", produces = "application/json")
    public Page<ReviewResponse> getAllPaged(@ParameterObject Pageable pageable) {
        LOGGER.info("Listing reviews with pagination, page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return reviewService.findAllPaged(pageable);
    }

    
}
