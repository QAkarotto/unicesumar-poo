
package com.eventos.controller;

import com.eventos.model.Evento;
import com.eventos.service.EventoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventoService eventoService;

    @Test
    void deveListarEventos() throws Exception {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento de Tecnologia");
        evento.setLocal("Auditório");
        evento.setDataHora(LocalDateTime.now().plusDays(10));
        evento.setCapacidade(50);

        when(eventoService.listarTodos())
            .thenReturn(List.of(evento));

        mockMvc.perform(get("/api/eventos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nome")
                .value("Evento de Tecnologia"))
            .andExpect(jsonPath("$[0].capacidade").value(50));

        verify(eventoService).listarTodos();
    }

    @Test
    void deveBuscarEventoPorId() throws Exception {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento de Tecnologia");
        evento.setLocal("Auditório");
        evento.setDataHora(LocalDateTime.now().plusDays(10));
        evento.setCapacidade(50);

        when(eventoService.buscarPorId(1L))
            .thenReturn(evento);

        mockMvc.perform(get("/api/eventos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome")
                .value("Evento de Tecnologia"));

        verify(eventoService).buscarPorId(1L);
    }

    @Test
    void deveExcluirEvento() throws Exception {
        doNothing().when(eventoService).excluir(1L);

        mockMvc.perform(delete("/api/eventos/1"))
            .andExpect(status().isNoContent());

        verify(eventoService).excluir(1L);
    }
}
