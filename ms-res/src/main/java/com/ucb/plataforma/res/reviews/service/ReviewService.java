package com.ucb.plataforma.res.reviews.service;

import com.ucb.plataforma.res.reviews.dto.*;
import com.ucb.plataforma.res.reviews.entity.Review;
import com.ucb.plataforma.res.reviews.exception.NotFoundException;
import com.ucb.plataforma.res.reviews.mapper.ReviewMapper;
import com.ucb.plataforma.res.reviews.repository.ReviewRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Retorna todas las reseñas sin paginación.
     */
    public List<ReviewResponse> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las reseñas de forma paginada.
     */
    public Page<ReviewResponse> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(reviewMapper::toResponse);
    }

    /**
     * Retorna reseñas con calificación mínima.
     */
    public List<ReviewResponse> findHighRated(int minRating) {
        return reviewRepository.findByRatingGreaterThanEqual(minRating)
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca reseñas que contengan una palabra clave en el comentario.
     */
    public List<ReviewResponse> searchByKeyword(String keyword) {
        return reviewRepository.searchByCommentContaining(keyword)
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca una reseña por su ID.
     */
    public ReviewResponse findById(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id=" + id));
        return reviewMapper.toResponse(review);
    }

    /**
     * Crea una nueva reseña.
     */
    public ReviewResponse create(ReviewCreateRequest request) {
        Review entity = reviewMapper.toEntity(request);
        Review saved = reviewRepository.save(entity);
        return reviewMapper.toResponse(saved);
    }

    /**
     * Actualiza una reseña existente.
     */
    public ReviewResponse update(long id, ReviewUpdateRequest request) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id=" + id));

        reviewMapper.updateEntityFromDto(request, existing);
        Review saved = reviewRepository.save(existing);
        return reviewMapper.toResponse(saved);
    }

    /**
     * Elimina una reseña por su ID.
     */
    public void delete(long id) {
        if (!reviewRepository.existsById(id)) {
            throw new NotFoundException("Review not found with id=" + id);
        }
        reviewRepository.deleteById(id);
    }
}
