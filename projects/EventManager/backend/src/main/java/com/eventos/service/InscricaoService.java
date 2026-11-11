package com.eventos.service;

import com.eventos.model.Evento;
import com.eventos.model.Participante;
import com.eventos.model.Inscricao;

import com.eventos.repository.EventoRepository;
import com.eventos.repository.ParticipanteRepository;
import com.eventos.repository.InscricaoRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;
    private final ParticipanteRepository participanteRepository;

    public InscricaoService(
            InscricaoRepository inscricaoRepository,
            EventoRepository eventoRepository,
            ParticipanteRepository participanteRepository) {

        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
        this.participanteRepository = participanteRepository;
    }

    @Transactional
    public Inscricao inscrever(Long eventoId, Long participanteId) {

        // Bloqueia inscrições concorrentes no mesmo evento.
        Evento evento = eventoRepository.findByIdParaInscricao(eventoId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento não encontrado"
            ));

        Participante participante = participanteRepository
            .findById(participanteId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Participante não encontrado"
            ));

        if (evento.getDataHora() == null ||
                !evento.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Não é possível se inscrever em eventos encerrados ou iniciados"
            );
        }

        if (inscricaoRepository.existsByEventoIdAndParticipanteId(
                eventoId, participanteId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Participante já inscrito neste evento"
            );
        }

        long inscritos = inscricaoRepository.countByEventoId(eventoId);

        if (evento.getCapacidade() == null ||
                evento.getCapacidade() <= 0 ||
                inscritos >= evento.getCapacidade()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Evento sem vagas disponíveis"
            );
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setEvento(evento);
        inscricao.setParticipante(participante);

        return inscricaoRepository.save(inscricao);
    }

    public List<Inscricao> listarTodos() {
        return inscricaoRepository.findAll(Sort.by("id"));
    }

    public List<Inscricao> listarPorEvento(Long eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento não encontrado"
            );
        }
        return inscricaoRepository.findByEventoId(eventoId);
    }

    public List<Inscricao> listarPorParticipante(Long participanteId) {
        if (!participanteRepository.existsById(participanteId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Participante não encontrado"
            );
        }
        return inscricaoRepository.findByParticipanteId(participanteId);
    }

    public void cancelar(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Inscrição não encontrada"
            ));

        inscricaoRepository.delete(inscricao);
    }
}
