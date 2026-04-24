package com.example.transferencias.controller;

import com.example.transferencias.api.TransferenciasApi;
import com.example.transferencias.api.model.TransferenciaRequest;
import com.example.transferencias.api.model.TransferenciaResponse;
import com.example.transferencias.service.TransferenciasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransferenciasController implements TransferenciasApi {

    private final TransferenciasService service;

    @Override
    public ResponseEntity<TransferenciaResponse> crearTransferencia(TransferenciaRequest transferenciaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(transferenciaRequest));
    }

    @Override
    public ResponseEntity<List<TransferenciaResponse>> listarTransferencias() {
        return ResponseEntity.ok(service.listar());
    }

    @Override
    public ResponseEntity<TransferenciaResponse> obtenerTransferenciaPorId(Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}
