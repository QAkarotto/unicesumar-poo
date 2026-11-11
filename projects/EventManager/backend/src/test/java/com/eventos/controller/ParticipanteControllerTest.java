
package com.eventos.controller;

import com.eventos.model.Participante;
import com.eventos.service.ParticipanteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipanteController.class)
class ParticipanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParticipanteService participanteService;

    @Test
    void deveListarParticipantes() throws Exception {
        Participante participante = new Participante();
        participante.setId(1L);
        participante.setNome("Gabriel");
        participante.setEmail("gabriel@email.com");

        when(participanteService.listarTodos())
            .thenReturn(List.of(participante));

        mockMvc.perform(get("/api/participantes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nome").value("Gabriel"))
            .andExpect(jsonPath("$[0].email").value("gabriel@email.com"));

        verify(participanteService).listarTodos();
    }

    @Test
    void deveBuscarParticipantePorId() throws Exception {
        Participante participante = new Participante();
        participante.setId(1L);
        participante.setNome("Gabriel");
        participante.setEmail("gabriel@email.com");

        when(participanteService.buscarPorId(1L))
            .thenReturn(participante);

        mockMvc.perform(get("/api/participantes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Gabriel"));

        verify(participanteService).buscarPorId(1L);
    }

    @Test
    void deveExcluirParticipante() throws Exception {
        mockMvc.perform(delete("/api/participantes/1"))
            .andExpect(status().isNoContent());

        verify(participanteService).excluir(1L);
    }
}
