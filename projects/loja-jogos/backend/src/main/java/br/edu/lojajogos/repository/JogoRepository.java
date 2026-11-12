package br.edu.lojajogos.repository;

import br.edu.lojajogos.domain.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
}