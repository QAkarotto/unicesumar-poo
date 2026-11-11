
package com.eventos.service;

import com.eventos.model.Participante;
import com.eventos.repository.ParticipanteRepository;
import com.eventos.repository.InscricaoRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipanteService {

    private final ParticipanteRepository repository;
    private final InscricaoRepository inscricaoRepository;

    public ParticipanteService(
            ParticipanteRepository repository,
            InscricaoRepository inscricaoRepository) {

        this.repository = repository;
        this.inscricaoRepository = inscricaoRepository;
    }

    public Participante cadastrar(Participante participante) {

        validarParticipante(participante);

        if (repository.existsByEmail(participante.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email já cadastrado"
            );
        }

        participante.setId(null);
        return repository.save(participante);
    }

    public List<Participante> listarTodos() {
        return repository.findAll();
    }

    public Participante buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Participante não encontrado"
            ));
    }

    public Participante atualizar(Long id, Participante dados) {

        Participante participante = buscarPorId(id);

        validarParticipante(dados);

        if (!participante.getEmail().equals(dados.getEmail())
                && repository.existsByEmail(dados.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email já cadastrado"
            );
        }

        participante.setNome(dados.getNome());
        participante.setEmail(dados.getEmail());

        return repository.save(participante);
    }

    @Transactional
    public void excluir(Long id) {

        Participante participante = buscarPorId(id);

        if (!inscricaoRepository.findByParticipanteId(id).isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não é possível excluir participante com inscrições"
            );
        }

        repository.delete(participante);
    }

    private void validarParticipante(Participante participante) {

        if (participante == null ||
                participante.getNome() == null ||
                participante.getNome().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nome obrigatório"
            );
        }

        if (participante.getEmail() == null ||
                !participante.getEmail().matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email inválido"
            );
        }
    }
}
