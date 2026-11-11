package com.eventos.service;

import com.eventos.model.Evento;
import com.eventos.model.Participante;
import com.eventos.model.Inscricao;

import com.eventos.repository.EventoRepository;
import com.eventos.repository.ParticipanteRepository;
import com.eventos.repository.InscricaoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscricaoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private ParticipanteRepository participanteRepository;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private InscricaoService inscricaoService;

    private Evento evento;
    private Participante participante;

    @BeforeEach
    void configurar() {
        evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento de Tecnologia");
        evento.setLocal("Auditório");
        evento.setDataHora(LocalDateTime.now().plusDays(10));
        evento.setCapacidade(2);

        participante = new Participante();
        participante.setId(1L);
        participante.setNome("Gabriel");
        participante.setEmail("gabriel@email.com");
    }

    @Test
    void deveRealizarInscricaoValida() {
        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(participanteRepository.findById(1L))
            .thenReturn(Optional.of(participante));

        when(inscricaoRepository.save(any(Inscricao.class)))
            .thenAnswer(invocacao -> invocacao.getArgument(0));

        Inscricao resultado = inscricaoService.inscrever(1L, 1L);

        assertNotNull(resultado);
        assertEquals(evento, resultado.getEvento());
        assertEquals(participante, resultado.getParticipante());

        verify(inscricaoRepository).save(any(Inscricao.class));
    }

    @Test
    void naoDevePermitirInscricaoDuplicada() {
        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(participanteRepository.findById(1L))
            .thenReturn(Optional.of(participante));

        when(inscricaoRepository.existsByEventoIdAndParticipanteId(1L, 1L))
            .thenReturn(true);

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> inscricaoService.inscrever(1L, 1L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        verify(inscricaoRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirEventoLotado() {
        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(participanteRepository.findById(1L))
            .thenReturn(Optional.of(participante));

        when(inscricaoRepository.countByEventoId(1L))
            .thenReturn(2L);

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> inscricaoService.inscrever(1L, 1L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        verify(inscricaoRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirEventoEncerrado() {
        evento.setDataHora(LocalDateTime.now().minusDays(1));

        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(participanteRepository.findById(1L))
            .thenReturn(Optional.of(participante));

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> inscricaoService.inscrever(1L, 1L)
        );

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(inscricaoRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirEventoInexistente() {
        when(eventoRepository.findByIdParaInscricao(99L))
            .thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> inscricaoService.inscrever(99L, 1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
            verify(inscricaoRepository, never()).save(any());
    }

   
    @Test
    void deveCancelarInscricaoExistente() {

    Inscricao inscricao = new Inscricao();

        inscricao.setEvento(evento);
        inscricao.setParticipante(participante);

        when(inscricaoRepository.findById(1L))
        .thenReturn(Optional.of(inscricao));

        inscricaoService.cancelar(1L);

        verify(inscricaoRepository).delete(inscricao);
    }

}
