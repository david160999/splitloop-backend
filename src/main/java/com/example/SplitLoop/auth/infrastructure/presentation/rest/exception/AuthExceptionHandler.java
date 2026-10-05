package com.example.SplitLoop.auth.infrastructure.presentation.rest.exception;

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
