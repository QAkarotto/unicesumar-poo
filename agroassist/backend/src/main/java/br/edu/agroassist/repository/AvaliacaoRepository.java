package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, String> {}
