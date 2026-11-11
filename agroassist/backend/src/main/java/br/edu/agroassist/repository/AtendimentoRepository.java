package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendimentoRepository extends JpaRepository<Atendimento, String> {}
