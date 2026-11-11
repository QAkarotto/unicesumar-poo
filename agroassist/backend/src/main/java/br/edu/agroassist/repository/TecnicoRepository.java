package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Tecnico;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, String> {
    boolean existsByRegistroIgnoreCase(String registro);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Tecnico t where t.id = :id")
    Optional<Tecnico> buscarParaAlterar(@Param("id") String id);
}
