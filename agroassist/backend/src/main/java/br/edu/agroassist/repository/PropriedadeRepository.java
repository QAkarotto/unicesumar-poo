package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropriedadeRepository extends JpaRepository<Propriedade, String> {}
