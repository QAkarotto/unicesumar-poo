package br.com.vetagenda.dto.responses;
import br.com.vetagenda.domain.Agendamento;
import java.time.LocalDateTime;
public record AgendamentoResponse(Long id, LocalDateTime dataHora, String motivo,
    String observacoes, String status, Long animalId, String animalNome,
    Long veterinarioId, String veterinarioNome) {
    public static AgendamentoResponse from(Agendamento a) {
        return new AgendamentoResponse(a.getId(), a.getDataHora(), a.getMotivo(),
            a.getObservacoes(), a.getStatus().name(), a.getAnimal().getId(),
            a.getAnimal().getNome(), a.getVeterinario().getId(), a.getVeterinario().getNome());
    }
}