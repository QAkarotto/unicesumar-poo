package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Leitor;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Leitor l where l.id = :id")
    Optional<Leitor> buscarComBloqueio(@Param("id") Long id);
}
