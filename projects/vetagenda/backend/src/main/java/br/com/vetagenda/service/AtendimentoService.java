package br.com.vetagenda.service;

import br.com.vetagenda.domain.Atendimento;
import br.com.vetagenda.dto.requests.AtendimentoRequest;
import br.com.vetagenda.dto.responses.AtendimentoResponse;
import br.com.vetagenda.exception.RecursoNaoEncontradoException;
import br.com.vetagenda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoService {
    private final AtendimentoRepository atendimentos;
    private final AgendamentoRepository agendamentos;
    public AtendimentoService(AtendimentoRepository atendimentos, AgendamentoRepository agendamentos) {
        this.atendimentos = atendimentos; this.agendamentos = agendamentos;
    }
    @Transactional
    public AtendimentoResponse cadastrar(AtendimentoRequest req) {
        var agendamento = agendamentos.findById(req.agendamentoId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));
        if (agendamento.getStatus().name().equals("CANCELADO")) {
            throw new IllegalStateException("Não é permitido atender um agendamento cancelado.");
        }
        if (atendimentos.existsByAgendamentoId(req.agendamentoId())) {
            throw new IllegalArgumentException("Já existe atendimento registrado para esse agendamento.");
        }
        agendamento.marcarComoRealizado();
        return AtendimentoResponse.from(atendimentos.save(new Atendimento(
            req.diagnostico(), req.procedimento(), req.observacoes(), agendamento)));
    }
}
