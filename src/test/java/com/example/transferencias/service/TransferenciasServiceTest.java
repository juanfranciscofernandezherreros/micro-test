package com.example.transferencias.service;

import com.example.transferencias.api.model.TransferenciaRequest;
import com.example.transferencias.api.model.TransferenciaResponse;
import com.example.transferencias.exception.TransferenciaNotFoundException;
import com.example.transferencias.model.EstadoTransferencia;
import com.example.transferencias.model.Transferencia;
import com.example.transferencias.repository.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferenciasServiceTest {

    @Mock
    private TransferenciaRepository repository;

    @InjectMocks
    private TransferenciasService service;

    private Transferencia transferenciaGuardada;

    @BeforeEach
    void setUp() {
        transferenciaGuardada = Transferencia.builder()
                .id(1L)
                .cuentaOrigen("ES9121000418450200051332")
                .cuentaDestino("ES8023100001180000012345")
                .importe(150.75)
                .moneda("EUR")
                .concepto("Pago de alquiler")
                .estado(EstadoTransferencia.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void crear_deberiaGuardarYDevolverTransferencia() {
        when(repository.save(any(Transferencia.class))).thenReturn(transferenciaGuardada);

        TransferenciaRequest request = new TransferenciaRequest();
        request.setCuentaOrigen("ES9121000418450200051332");
        request.setCuentaDestino("ES8023100001180000012345");
        request.setImporte(150.75);
        request.setMoneda("EUR");
        request.setConcepto("Pago de alquiler");

        TransferenciaResponse response = service.crear(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCuentaOrigen()).isEqualTo("ES9121000418450200051332");
        assertThat(response.getEstado()).isEqualTo(TransferenciaResponse.EstadoEnum.PENDIENTE);
    }

    @Test
    void listar_deberiaRetornarTodasLasTransferencias() {
        when(repository.findAll()).thenReturn(List.of(transferenciaGuardada));

        List<TransferenciaResponse> lista = service.listar();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarTransferencia() {
        when(repository.findById(1L)).thenReturn(Optional.of(transferenciaGuardada));

        TransferenciaResponse response = service.obtenerPorId(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getMoneda()).isEqualTo("EUR");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(TransferenciaNotFoundException.class)
                .hasMessageContaining("99");
    }
}
