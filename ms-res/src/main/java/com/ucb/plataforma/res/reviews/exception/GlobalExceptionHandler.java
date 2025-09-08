package com.ucb.plataforma.res.reviews.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException; 
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===== Helpers mínimos =====
    private ProblemDetail basePd(HttpStatus status, String detail, ServerHttpRequest req, String title) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        if (StringUtils.hasText(title)) pd.setTitle(title);
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", req.getURI().getPath());
        pd.setProperty("method", req.getMethod() != null ? req.getMethod().name() : null);
        return pd;
    }
    private ResponseEntity<ProblemDetail> respond(HttpStatus status, ProblemDetail pd, boolean serverError, Throwable ex) {
        if (serverError) log.error("{} {} - {}", status.value(), status.getReasonPhrase(), pd.getDetail(), ex);
        else            log.warn("{} {} - {}", status.value(), status.getReasonPhrase(), pd.getDetail());
        return ResponseEntity.status(status).body(pd);
    }

    // ===== 404 de dominio =====
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex, ServerHttpRequest req) {
        ProblemDetail pd = basePd(HttpStatus.NOT_FOUND, ex.getMessage(), req, "Not Found");
        return respond(HttpStatus.NOT_FOUND, pd, false, ex);
    }

    // ===== 400: body inválido por @Valid (DTO) =====
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetail> handleBodyValidation(WebExchangeBindException ex, ServerHttpRequest req) {
        ProblemDetail pd = basePd(HttpStatus.BAD_REQUEST, "Payload inválido", req, "Bad Request");
        List<Map<String, String>> errors = ex.getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", String.valueOf(fe.getDefaultMessage())))
                .collect(Collectors.toList());
        if (!errors.isEmpty()) pd.setProperty("errors", errors);
        return respond(HttpStatus.BAD_REQUEST, pd, false, ex);
    }

    // ===== 400: path/query inválidos por @Min/@Max/@NotNull =====
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraint(ConstraintViolationException ex, ServerHttpRequest req) {
        ProblemDetail pd = basePd(HttpStatus.BAD_REQUEST, "Parámetros inválidos", req, "Bad Request");
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(v -> Map.of("param", v.getPropertyPath().toString(), "message", v.getMessage()))
                .collect(Collectors.toList());
        if (!errors.isEmpty()) pd.setProperty("errors", errors);
        return respond(HttpStatus.BAD_REQUEST, pd, false, ex);
    }

    // ===== 400: entrada/conversión (JSON malformado, tipos incorrectos, etc.) =====
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ProblemDetail> handleWebInput(ServerWebInputException ex, ServerHttpRequest req) {
        String detail = "Entrada de solicitud inválida";
        Throwable cause = ex.getCause();
        if (cause instanceof JsonProcessingException jpe) {
            detail = "JSON mal formado: " + jpe.getOriginalMessage();
        } else if (cause instanceof DecodingException && cause.getCause() instanceof JsonProcessingException jpe2) {
            detail = "JSON mal formado: " + jpe2.getOriginalMessage();
        } else if (StringUtils.hasText(ex.getReason())) {
            detail = ex.getReason();
        }
        ProblemDetail pd = basePd(HttpStatus.BAD_REQUEST, detail, req, "Bad Request");
        return respond(HttpStatus.BAD_REQUEST, pd, false, ex);
    }

    // ===== 405: método no permitido =====
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotAllowed(MethodNotAllowedException ex, ServerHttpRequest req) {
        ProblemDetail pd = basePd(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), req, "Method Not Allowed");
        pd.setProperty("allowed", ex.getSupportedMethods());
        return respond(HttpStatus.METHOD_NOT_ALLOWED, pd, false, ex);
    }

    // ===== 415: media type no soportado =====
    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<ProblemDetail> handleUnsupportedMedia(UnsupportedMediaTypeStatusException ex, ServerHttpRequest req) {
        ProblemDetail pd = basePd(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), req, "Unsupported Media Type");
        pd.setProperty("supported", ex.getSupportedMediaTypes());
        return respond(HttpStatus.UNSUPPORTED_MEDIA_TYPE, pd, false, ex);
    }

    // ===== 500: genérico =====
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, ServerHttpRequest req) {
        ProblemDetail pd = basePd(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", req, "Internal Server Error");
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, pd, true, ex);
    }
}
