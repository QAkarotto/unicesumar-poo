
package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Leitor;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {

    boolean existsByEmail(String email);

    Optional<Leitor> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Leitor l WHERE l.id = :id")
    Optional<Leitor> buscarPorIdComBloqueio(
        @Param("id") Long id
    );
}
