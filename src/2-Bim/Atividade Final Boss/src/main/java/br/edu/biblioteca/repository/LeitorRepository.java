package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {

    Optional<Leitor> findByEmail(String email);

    boolean existsByEmail(String email);
}
