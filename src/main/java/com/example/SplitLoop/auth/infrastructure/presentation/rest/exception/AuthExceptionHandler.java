package com.example.SplitLoop.auth.infrastructure.presentation.rest.exception;

import com.example.SplitLoop.auth.application.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.auth.domain.exception.InvalidBearerTokenException;
import com.example.SplitLoop.auth.domain.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.auth.domain.exception.TokenExpiredException;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.controller.AuthController;
import com.example.SplitLoop.common.exception.ErrorResponse; // O tu DTO de respuesta genérico
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import com.example.SplitLoop.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;

//@RestControllerAdvice(assignableTypes = { AuthController.class })
//public class AuthExceptionHandler {
//
//    @ExceptionHandler({
//            InvalidBearerTokenException.class,
//            InvalidRefreshTokenException.class,
//            TokenExpiredException.class
//    })
//    public ResponseEntity<ErrorResponse> handleUnauthorizedExceptions(BusinessException ex, HttpServletRequest request) {
//        return buildResponse(HttpStatus.UNAUTHORIZED, ex, request);
//    }
//
//    @ExceptionHandler(RefreshTokenRequiredException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(BusinessException ex, HttpServletRequest request) {
//        return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
//    }
//
//    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, BusinessException ex, HttpServletRequest request) {
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(status.value())
//                .error(status.getReasonPhrase())
//                .code(ex.getErrorCode().name())
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//
//        return ResponseEntity.status(status).body(errorResponse);
//    }
//}
