
package com.eventos.service;

import com.eventos.model.Participante;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipanteServiceTest {

    @Mock
    private ParticipanteRepository participanteRepository;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private ParticipanteService participanteService;

    private Participante participante;

    @BeforeEach
    void configurar() {
        participante = new Participante();
        participante.setId(1L);
        participante.setNome("Gabriel");
        participante.setEmail("gabriel@email.com");
    }

    @Test
    void deveCadastrarParticipanteValido() {
        when(participanteRepository.save(any(Participante.class)))
            .thenAnswer(invocacao -> invocacao.getArgument(0));

        Participante resultado =
            participanteService.cadastrar(participante);

        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getNome());
        verify(participanteRepository).save(participante);
    }

    @Test
    void naoDeveCadastrarSemNome() {
        participante.setNome("");

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> participanteService.cadastrar(participante)
        );

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
    }

    @Test
    void naoDeveCadastrarEmailInvalido() {
        participante.setEmail("email-invalido");

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> participanteService.cadastrar(participante)
        );

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {
        when(participanteRepository.existsByEmail("gabriel@email.com"))
            .thenReturn(true);

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> participanteService.cadastrar(participante)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
    }

    @Test
    void naoDeveExcluirParticipanteInscrito() {
        when(participanteRepository.findById(1L))
            .thenReturn(Optional.of(participante));

        when(inscricaoRepository.findByParticipanteId(1L))
            .thenReturn(
                Collections.singletonList(
                    new com.eventos.model.Inscricao()
                )
            );

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> participanteService.excluir(1L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        verify(participanteRepository, never()).delete(any());
    }

    @Test
    void deveExcluirParticipanteSemInscricoes() {
        when(participanteRepository.findById(1L))
            .thenReturn(Optional.of(participante));

        when(inscricaoRepository.findByParticipanteId(1L))
            .thenReturn(Collections.emptyList());

        participanteService.excluir(1L);

        verify(participanteRepository).delete(participante);
    }
}
