package br.edu.agroassist.repository;

import br.edu.agroassist.domain.SequenciaProtocolo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface SequenciaProtocoloRepository extends JpaRepository<SequenciaProtocolo, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SequenciaProtocolo s where s.id = 1")
    Optional<SequenciaProtocolo> buscarParaIncrementar();
}
