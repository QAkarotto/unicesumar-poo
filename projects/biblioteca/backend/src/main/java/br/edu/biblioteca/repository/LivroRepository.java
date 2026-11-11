package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Livro;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Livro l where l.id = :id")
    Optional<Livro> buscarComBloqueio(@Param("id") Long id);
}
