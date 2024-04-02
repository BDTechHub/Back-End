package com.bdtc.technews.infra.exception.error;

import com.bdtc.technews.infra.exception.validation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404Handler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleError400(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();
        return ResponseEntity.badRequest()
                .body(errors.stream()
                        .map(ValidationErrorData::new).toList());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity businessRuleExceptionHandler(BusinessRuleException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ThereIsNoBackupForThisNewsException.class)
    public ResponseEntity noBackupForNewsHandler(ThereIsNoBackupForThisNewsException exception) {
        return ResponseEntity.badRequest().body(new ValidationErrorData("newsId", exception.getMessage()));
    }

    @ExceptionHandler(ExceededTheExistingBackupLevelException.class)
    public ResponseEntity noBackupLevelForNewsHandler(ExceededTheExistingBackupLevelException exception) {
        return ResponseEntity.badRequest().body(new ValidationErrorData("level", exception.getMessage()));
    }

    @ExceptionHandler(ConflictInPathParameters.class)
    public ResponseEntity conflictPathParametersHandler(ConflictInPathParameters exception) {
        return ResponseEntity.badRequest().body(new ValidationErrorData("pathParameters", exception.getMessage()));
    }

    @ExceptionHandler(AlreadyUpVotedException.class)
    public ResponseEntity alreadyUpVotedHandler(AlreadyUpVotedException exception) {
        return ResponseEntity.badRequest().body(new ValidationErrorData("upVote", exception.getMessage()));
    }

    @ExceptionHandler(AuthClientInvalidTokenException.class)
    public ResponseEntity invalidTokenHandler(AuthClientInvalidTokenException exception) {
        return ResponseEntity.badRequest().body(new ValidationErrorData("token", exception.getMessage()));
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity permissionDeniedHandler(PermissionException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ValidationErrorData("authorization", exception.getMessage()));
    }
}
