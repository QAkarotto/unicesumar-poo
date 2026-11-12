package br.com.vetagenda.service;

import br.com.vetagenda.domain.*;
import br.com.vetagenda.dto.requests.AgendamentoRequest;
import br.com.vetagenda.dto.responses.AgendamentoResponse;
import br.com.vetagenda.exception.RecursoNaoEncontradoException;
import br.com.vetagenda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentos;
    private final AnimalRepository animais;
    private final VeterinarioRepository veterinarios;

    public AgendamentoService(AgendamentoRepository agendamentos, AnimalRepository animais,
                              VeterinarioRepository veterinarios) {
        this.agendamentos = agendamentos; this.animais = animais; this.veterinarios = veterinarios;
    }

    @Transactional
    public AgendamentoResponse cadastrar(AgendamentoRequest req) {
        validarHorario(req.dataHora(), req.veterinarioId(), null);
        var animal = animais.findById(req.animalId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Animal não encontrado."));
        var veterinario = veterinarios.findById(req.veterinarioId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário não encontrado."));
        var salvo = agendamentos.save(new Agendamento(req.dataHora(), req.motivo(),
            req.observacoes(), animal, veterinario));
        return AgendamentoResponse.from(salvo);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar() {
        return agendamentos.findAll().stream().map(AgendamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscar(Long id) { return AgendamentoResponse.from(obter(id)); }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest req) {
        var atual = obter(id);
        validarHorario(req.dataHora(), req.veterinarioId(), id);
        var animal = animais.findById(req.animalId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Animal não encontrado."));
        var veterinario = veterinarios.findById(req.veterinarioId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário não encontrado."));
        atual.atualizar(req.dataHora(), req.motivo(), req.observacoes(), animal, veterinario);
        return AgendamentoResponse.from(atual);
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id) {
        var agendamento = obter(id);
        agendamento.cancelar();
        return AgendamentoResponse.from(agendamento);
    }

    private void validarHorario(LocalDateTime dataHora, Long veterinarioId, Long ignorarId) {
        if (dataHora == null || !dataHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("O agendamento deve estar em uma data e horário futuros.");
        }
        boolean conflito = agendamentos.existsByVeterinarioIdAndDataHoraAndStatusIn(
            veterinarioId, dataHora, List.of(StatusAgendamento.AGENDADO, StatusAgendamento.CONFIRMADO));
        if (conflito && (ignorarId == null || !agendamentos.findById(ignorarId)
                .map(a -> a.getVeterinario().getId().equals(veterinarioId) && a.getDataHora().equals(dataHora))
                .orElse(false))) {
            throw new IllegalArgumentException("Este veterinário já possui um agendamento nesse horário.");
        }
    }

    private Agendamento obter(Long id) {
        return agendamentos.findById(id).orElseThrow(() ->
            new RecursoNaoEncontradoException("Agendamento não encontrado: " + id));
    }
}
