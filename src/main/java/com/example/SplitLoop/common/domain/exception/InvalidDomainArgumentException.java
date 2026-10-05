package com.example.SplitLoop.common.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidDomainArgumentException extends BusinessException {

    // Constructor con mensaje personalizado/manual
    public InvalidDomainArgumentException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_DOMAIN_ARGUMENT,
                message
        );
    }

    // Constructor por defecto (opcional, usa mensaje genérico)
    public InvalidDomainArgumentException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_DOMAIN_ARGUMENT,
                "Argumento o invariante de dominio inválido"
        );
    }
}