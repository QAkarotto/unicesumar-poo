package br.com.vetagenda.dto.responses;
import br.com.vetagenda.domain.Atendimento;
public record AtendimentoResponse(Long id, String diagnostico, String procedimento,
                                   String observacoes, Long agendamentoId) {
    public static AtendimentoResponse from(Atendimento a) {
        return new AtendimentoResponse(a.getId(), a.getDiagnostico(), a.getProcedimento(),
            a.getObservacoes(), a.getAgendamento().getId());
    }
}