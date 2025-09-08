package com.ucb.plataforma.res.reviews.service;

import com.ucb.plataforma.res.reviews.dto.ReviewCreateRequest;
import com.ucb.plataforma.res.reviews.dto.ReviewResponse;
import com.ucb.plataforma.res.reviews.dto.ReviewUpdateRequest;
import com.ucb.plataforma.res.reviews.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultReviewService implements ReviewService {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT;

    @Override
    public List<ReviewResponse> findAll() {
        Instant now = Instant.now();
        List<ReviewResponse> list = new ArrayList<>();
        list.add(sample(1, 101, 1L, 5, "Excelente", "Muy claro el docente.", now, now));
        list.add(sample(2, 101, 2L, 4, "Muy bueno", "Faltaron ejemplos.", now, now));
        return list;
    }

    @Override
    public ReviewResponse findById(long id) {
        if (id < 1) throw new NotFoundException("No se encontró la reseña con id=" + id);
        Instant now = Instant.now();
        return sample(id, 101, 1L, 5, "Reseña " + id, "Comentario dummy", now, now);
    }

    @Override
    public ReviewResponse create(ReviewCreateRequest req) {
        Instant now = Instant.now();
        long newId = 100L; // dummy
        return sample(newId, req.getCourseId(), req.getUserId(), req.getRating(),
                req.getTitle(), req.getComment(), now, now);
    }

    @Override
    public ReviewResponse update(long id, ReviewUpdateRequest req) {
        // En real, buscarías existente; aquí simulamos
        Instant now = Instant.now();
        int rating = req.getRating() != null ? req.getRating() : 5;
        String title = req.getTitle() != null ? req.getTitle() : "Título previo";
        String comment = req.getComment() != null ? req.getComment() : "Comentario previo";
        return sample(id, 101, 1L, rating, title, comment, now, now);
    }

    @Override
    public void delete(long id) {
        if (id < 1) throw new NotFoundException("No se encontró la reseña con id=" + id);
        // no-op
    }

    private ReviewResponse sample(long id, long courseId, Long userId, int rating,
                                  String title, String comment, Instant created, Instant updated) {
        return new ReviewResponse()
                .setId(id)
                .setCourseId(courseId)
                .setUserId(userId)
                .setRating(rating)
                .setTitle(title)
                .setComment(comment)
                .setCreatedAt(ISO.format(created))
                .setUpdatedAt(ISO.format(updated));
    }
}
