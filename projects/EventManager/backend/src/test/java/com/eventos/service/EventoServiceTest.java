
package com.eventos.service;

import com.eventos.model.Evento;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.InscricaoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;

    @BeforeEach
    void configurar() {
        evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento de Tecnologia");
        evento.setDescricao("Palestras e workshops");
        evento.setLocal("Auditório");
        evento.setDataHora(LocalDateTime.now().plusDays(10));
        evento.setCapacidade(50);
    }

    @Test
    void deveCadastrarEventoValido() {
        when(eventoRepository.save(any(Evento.class)))
            .thenAnswer(invocacao -> invocacao.getArgument(0));

        Evento resultado = eventoService.cadastrar(evento);

        assertNotNull(resultado);
        assertEquals("Evento de Tecnologia", resultado.getNome());

        verify(eventoRepository).save(evento);
    }

    @Test
    void naoDeveCadastrarEventoSemNome() {
        evento.setNome("");

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> eventoService.cadastrar(evento)
        );

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());

        verify(eventoRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarEventoComCapacidadeInvalida() {
        evento.setCapacidade(0);

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> eventoService.cadastrar(evento)
        );

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());

        verify(eventoRepository, never()).save(any());
    }

    @Test
    void naoDeveReduzirCapacidadeAbaixoDosInscritos() {
        Evento dados = new Evento();
        dados.setNome("Evento Atualizado");
        dados.setLocal("Sala 2");
        dados.setDataHora(LocalDateTime.now().plusDays(15));
        dados.setCapacidade(5);

        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(inscricaoRepository.countByEventoId(1L))
            .thenReturn(10L);

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> eventoService.atualizar(1L, dados)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());

        verify(eventoRepository, never()).save(any());
    }

    @Test
    void naoDeveExcluirEventoComInscricoes() {
        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(inscricaoRepository.countByEventoId(1L))
            .thenReturn(3L);

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> eventoService.excluir(1L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());

        verify(eventoRepository, never()).delete(any(Evento.class));
    }

    @Test
    void deveExcluirEventoSemInscricoes() {
        when(eventoRepository.findByIdParaInscricao(1L))
            .thenReturn(Optional.of(evento));

        when(inscricaoRepository.countByEventoId(1L))
            .thenReturn(0L);

        eventoService.excluir(1L);

        verify(eventoRepository).delete(evento);
    }
}
