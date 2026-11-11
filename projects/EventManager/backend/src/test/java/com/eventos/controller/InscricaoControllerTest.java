
package com.eventos.controller;

import com.eventos.model.Evento;
import com.eventos.model.Participante;
import com.eventos.model.Inscricao;
import com.eventos.service.InscricaoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscricaoController.class)
class InscricaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InscricaoService inscricaoService;

    private Inscricao criarInscricao() {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento de Tecnologia");

        Participante participante = new Participante();
        participante.setId(2L);
        participante.setNome("Gabriel");

        Inscricao inscricao = new Inscricao();
        inscricao.setEvento(evento);
        inscricao.setParticipante(participante);

        return inscricao;
    }

    @Test
    void deveListarInscricoes() throws Exception {
        when(inscricaoService.listarTodos())
            .thenReturn(List.of(criarInscricao()));

        mockMvc.perform(get("/api/inscricoes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].eventoNome")
                .value("Evento de Tecnologia"))
            .andExpect(jsonPath("$[0].participanteNome")
                .value("Gabriel"));

        verify(inscricaoService).listarTodos();
    }

    @Test
    void deveListarInscricoesPorEvento() throws Exception {
        when(inscricaoService.listarPorEvento(1L))
            .thenReturn(List.of(criarInscricao()));

        mockMvc.perform(get("/api/inscricoes/evento/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].eventoId").value(1));

        verify(inscricaoService).listarPorEvento(1L);
    }

    @Test
    void deveListarInscricoesPorParticipante() throws Exception {
        when(inscricaoService.listarPorParticipante(2L))
            .thenReturn(List.of(criarInscricao()));

        mockMvc.perform(get("/api/inscricoes/participante/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].participanteId").value(2));

        verify(inscricaoService).listarPorParticipante(2L);
    }

    @Test
    void deveCancelarInscricao() throws Exception {
        mockMvc.perform(delete("/api/inscricoes/1"))
            .andExpect(status().isNoContent());

        verify(inscricaoService).cancelar(1L);
    }
}
