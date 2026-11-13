
package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.SituacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByLeitorId(Long leitorId);

    List<Emprestimo> findByLivroId(Long livroId);

    List<Emprestimo> findBySituacao(SituacaoEmprestimo situacao);

    long countByLeitorIdAndSituacao(
            Long leitorId,
            SituacaoEmprestimo situacao
    );

    boolean existsByLivroId(Long livroId);

    boolean existsByLeitorId(Long leitorId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Emprestimo e WHERE e.id = :id")
    Optional<Emprestimo> buscarPorIdComBloqueio(@Param("id") Long id);
}
