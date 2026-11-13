
package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    boolean existsByIsbn(String isbn);

    Optional<Livro> findByIsbn(String isbn);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Livro l WHERE l.id = :id")
    Optional<Livro> buscarPorIdComBloqueio(@Param("id") Long id);
}
