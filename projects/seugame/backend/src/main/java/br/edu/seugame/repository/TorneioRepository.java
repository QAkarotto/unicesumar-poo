package br.edu.seugame.repository;

import br.edu.seugame.domain.Torneio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneioRepository extends JpaRepository<Torneio, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Torneio> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select t from Torneio t where t.id = :id")
    java.util.Optional<Torneio> buscarParaAtualizar(@org.springframework.data.repository.query.Param("id") Long id);

    @Override
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths={"criador","campeao"})
    List<Torneio> findAll(org.springframework.data.jpa.domain.Specification<Torneio> filtro);
    long countByCriadorId(Long criadorId);
    long countByCriadorIdAndStatus(Long criadorId, br.edu.seugame.domain.StatusTorneio status);
    List<Torneio> findByCriadorIdOrderByCriadoEmDesc(Long criadorId);
}
