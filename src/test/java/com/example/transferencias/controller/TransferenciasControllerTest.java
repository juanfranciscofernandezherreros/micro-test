package com.example.transferencias.controller;

import com.example.transferencias.api.model.TransferenciaRequest;
import com.example.transferencias.api.model.TransferenciaResponse;
import com.example.transferencias.exception.TransferenciaNotFoundException;
import com.example.transferencias.service.TransferenciasService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferenciasController.class)
class TransferenciasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferenciasService service;

    private TransferenciaResponse buildResponse(Long id) {
        TransferenciaResponse response = new TransferenciaResponse();
        response.setId(id);
        response.setCuentaOrigen("ES9121000418450200051332");
        response.setCuentaDestino("ES8023100001180000012345");
        response.setImporte(150.75);
        response.setMoneda("EUR");
        response.setConcepto("Pago de alquiler");
        response.setEstado(TransferenciaResponse.EstadoEnum.PENDIENTE);
        response.setFechaCreacion(OffsetDateTime.now());
        return response;
    }

    @Test
    void crearTransferencia_deberiaRetornar201() throws Exception {
        TransferenciaRequest request = new TransferenciaRequest();
        request.setCuentaOrigen("ES9121000418450200051332");
        request.setCuentaDestino("ES8023100001180000012345");
        request.setImporte(150.75);
        request.setMoneda("EUR");

        when(service.crear(any(TransferenciaRequest.class))).thenReturn(buildResponse(1L));

        mockMvc.perform(post("/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void listarTransferencias_deberiaRetornarLista() throws Exception {
        when(service.listar()).thenReturn(List.of(buildResponse(1L), buildResponse(2L)));

        mockMvc.perform(get("/transferencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtenerTransferenciaPorId_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(buildResponse(1L));

        mockMvc.perform(get("/transferencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerTransferenciaPorId_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenThrow(new TransferenciaNotFoundException(99L));

        mockMvc.perform(get("/transferencias/99"))
                .andExpect(status().isNotFound());
    }
}
