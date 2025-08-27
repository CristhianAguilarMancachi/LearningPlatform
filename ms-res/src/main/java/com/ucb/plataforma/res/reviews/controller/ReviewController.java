package com.ucb.plataforma.res.reviews.controller;

import com.ucb.plataforma.res.reviews.entity.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private static final Map<Long, Review> DB = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GEN = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody Review req) {
        long id = ID_GEN.getAndIncrement();
        Instant now = Instant.now();

        Review r = new Review();
        r.setId(id);
        r.setCourseId(req.getCourseId());      // lo pones manual en el body
        r.setUserId(req.getUserId());          // opcional
        r.setRating(req.getRating());
        r.setTitle(req.getTitle());
        r.setComment(req.getComment());
        r.setCreatedAt(now);
        r.setUpdatedAt(now);

        DB.put(id, r);
        return ResponseEntity.ok(r);
    }

    @GetMapping
    public List<Review> findAll() {
        return new ArrayList<>(DB.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> findById(@PathVariable Long id) {
        Review r = DB.get(id);
        return (r == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(r);
    }

    @GetMapping("/course/{courseId}")
    public List<Review> findByCourse(@PathVariable Long courseId) {
        return DB.values().stream()
                .filter(r -> Objects.equals(r.getCourseId(), courseId))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody Review req) {
        Review r = DB.get(id);
        if (r == null) return ResponseEntity.notFound().build();

        if (req.getRating() != null) r.setRating(req.getRating());
        if (req.getTitle() != null) r.setTitle(req.getTitle());
        if (req.getComment() != null) r.setComment(req.getComment());
        r.setUpdatedAt(Instant.now());

        return ResponseEntity.ok(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return (DB.remove(id) == null) ? ResponseEntity.notFound().build()
                                       : ResponseEntity.noContent().build();
    }
}
