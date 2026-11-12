package br.com.vetagenda.repository;
import br.com.vetagenda.domain.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByCpf(String cpf);
}