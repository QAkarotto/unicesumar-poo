package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Cultura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CulturaRepository extends JpaRepository<Cultura, String> {}
