package org.example.rout24.exception;

import org.example.rout24.constants.Messages;
import org.example.rout24.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<ApiResponse<?>> serverError(Exception ex){
        return ResponseEntity.status(500)
                .body(ApiResponse.error(Messages.SERVER_ERROR));
    }

    public ResponseEntity<ApiResponse<?>> badRequest(BadRequestException ex){
        return ResponseEntity.status(400)
                .body(ApiResponse.error(ex.getMessage()));
    }

    public ResponseEntity<ApiResponse<?>> dataNotFound(DataNotFoundException ex){
        return ResponseEntity.status(404)
                .body(ApiResponse.error(ex.getMessage()));
    }
}
