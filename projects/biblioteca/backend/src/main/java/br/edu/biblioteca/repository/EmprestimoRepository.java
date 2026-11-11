package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Emprestimo;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    long countByLivroIdAndDevolvidoEmIsNull(Long livroId);
    long countByLeitorIdAndDevolvidoEmIsNull(Long leitorId);
    boolean existsByLeitorIdAndDevolvidoEmIsNullAndPrevistoParaBefore(Long leitorId, LocalDate hoje);
    boolean existsByLeitorIdAndLivroIdAndDevolvidoEmIsNull(Long leitorId, Long livroId);
    @EntityGraph(attributePaths = {"livro", "leitor"})
    List<Emprestimo> findAllByOrderByIdDesc();
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Emprestimo e where e.id = :id")
    Optional<Emprestimo> buscarComBloqueio(@Param("id") Long id);
}
