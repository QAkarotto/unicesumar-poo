package br.com.vetagenda.repository;
import br.com.vetagenda.domain.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    boolean existsByAgendamentoId(Long agendamentoId);
}