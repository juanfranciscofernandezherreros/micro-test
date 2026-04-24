package com.example.transferencias.exception;

public class TransferenciaNotFoundException extends RuntimeException {

    public TransferenciaNotFoundException(Long id) {
        super("Transferencia no encontrada con id: " + id);
    }
}
