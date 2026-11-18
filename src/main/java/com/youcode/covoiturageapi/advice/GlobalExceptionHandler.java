
package com.youcode.covoiturageapi.exception;

// Logging
import lombok.extern.slf4j.Slf4j;

// Spring HTTP
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

// Spring Exception Handling
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Java
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // 1. RESOURCE NOT FOUND - HTTP 404
    // ==========================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problem.setTitle("Ressource non trouvée");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }


    // ==========================================
    // 2. FORBIDDEN ACTION - HTTP 403
    // ==========================================

    @ExceptionHandler(ForbiddenActionException.class)
    public ProblemDetail handleForbiddenAction(ForbiddenActionException ex) {

        log.warn("Action interdite : {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );

        problem.setTitle("Action interdite");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }


    // ==========================================
    // 3. BUSINESS RULE VIOLATION - HTTP 409
    // ==========================================

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {

        log.warn("Règle métier refusée : {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problem.setTitle("Règle métier non respectée");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }


    // ==========================================
    // 4. VALIDATION ERRORS - HTTP 400
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " +
                                error.getDefaultMessage()
                )
                .collect(Collectors.joining(", "));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors
        );

        problem.setTitle("Erreur de validation");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }


    // ==========================================
    // 5. UNEXPECTED SERVER ERROR - HTTP 500
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {

        log.error("Erreur serveur inattendue : ", ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne inattendue est survenue"
        );

        problem.setTitle("Erreur interne.");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }
}
