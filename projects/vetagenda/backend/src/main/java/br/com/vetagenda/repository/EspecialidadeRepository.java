package br.com.vetagenda.repository;
import br.com.vetagenda.domain.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}