package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Solicitacao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Solicitacao s where s.id = :id")
    Optional<Solicitacao> buscarParaAlterar(@Param("id") String id);
}
