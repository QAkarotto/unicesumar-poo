package br.com.vetagenda.repository;
import br.com.vetagenda.domain.Agendamento;
import br.com.vetagenda.domain.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    boolean existsByVeterinarioIdAndDataHoraAndStatusIn(
        Long veterinarioId, LocalDateTime dataHora, List<StatusAgendamento> status);
}