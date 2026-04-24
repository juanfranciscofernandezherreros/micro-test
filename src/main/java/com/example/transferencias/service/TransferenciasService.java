package com.example.transferencias.service;

import com.example.transferencias.api.model.TransferenciaRequest;
import com.example.transferencias.api.model.TransferenciaResponse;
import com.example.transferencias.exception.TransferenciaNotFoundException;
import com.example.transferencias.model.EstadoTransferencia;
import com.example.transferencias.model.Transferencia;
import com.example.transferencias.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferenciasService {

    private final TransferenciaRepository repository;

    public TransferenciaResponse crear(TransferenciaRequest request) {
        Transferencia transferencia = Transferencia.builder()
                .cuentaOrigen(request.getCuentaOrigen())
                .cuentaDestino(request.getCuentaDestino())
                .importe(request.getImporte())
                .moneda(request.getMoneda())
                .concepto(request.getConcepto())
                .estado(EstadoTransferencia.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();
        return toResponse(repository.save(transferencia));
    }

    public List<TransferenciaResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransferenciaResponse obtenerPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new TransferenciaNotFoundException(id));
    }

    private TransferenciaResponse toResponse(Transferencia t) {
        TransferenciaResponse response = new TransferenciaResponse();
        response.setId(t.getId());
        response.setCuentaOrigen(t.getCuentaOrigen());
        response.setCuentaDestino(t.getCuentaDestino());
        response.setImporte(t.getImporte());
        response.setMoneda(t.getMoneda());
        response.setConcepto(t.getConcepto());
        response.setEstado(TransferenciaResponse.EstadoEnum.valueOf(t.getEstado().name()));
        response.setFechaCreacion(t.getFechaCreacion().atOffset(ZoneOffset.UTC));
        return response;
    }
}
