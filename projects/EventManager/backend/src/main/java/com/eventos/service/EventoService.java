
package com.eventos.service;

import com.eventos.model.Evento;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.InscricaoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repository;
    private final InscricaoRepository inscricaoRepository;

    public EventoService(
            EventoRepository repository,
            InscricaoRepository inscricaoRepository) {
        this.repository = repository;
        this.inscricaoRepository = inscricaoRepository;
    }

    public Evento cadastrar(Evento evento) {
        validarEvento(evento);
        evento.setId(null);
        return repository.save(evento);
    }

    public List<Evento> listarTodos() {
        return repository.findAll();
    }

    public Evento buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Evento não encontrado"
            ));
    }

    @Transactional
    public Evento atualizar(Long id, Evento dados) {
        Evento evento = repository.findByIdParaInscricao(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Evento não encontrado"
            ));

        validarEvento(dados);

        long inscritos = inscricaoRepository.countByEventoId(id);

        if (dados.getCapacidade() < inscritos) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A capacidade não pode ser menor que o número de inscritos"
            );
        }

        evento.setNome(dados.getNome());
        evento.setDescricao(dados.getDescricao());
        evento.setLocal(dados.getLocal());
        evento.setDataHora(dados.getDataHora());
        evento.setCapacidade(dados.getCapacidade());

        return repository.save(evento);
    }

    @Transactional
    public void excluir(Long id) {
        Evento evento = repository.findByIdParaInscricao(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Evento não encontrado"
            ));

        if (inscricaoRepository.countByEventoId(id) > 0) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não é possível excluir um evento com inscrições"
            );
        }

        repository.delete(evento);
    }

    private void validarEvento(Evento evento) {
        if (evento.getNome() == null ||
                evento.getNome().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nome do evento é obrigatório"
            );
        }

        if (evento.getLocal() == null ||
                evento.getLocal().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Local do evento é obrigatório"
            );
        }

        if (evento.getDataHora() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Data do evento é obrigatória"
            );
        }

        if (evento.getCapacidade() == null ||
                evento.getCapacidade() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A capacidade deve ser maior que zero"
            );
        }
    }
}
