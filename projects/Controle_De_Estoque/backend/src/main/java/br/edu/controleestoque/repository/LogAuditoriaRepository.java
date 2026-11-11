package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.LogAuditoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {

    List<LogAuditoria> findAllByOrderByDataHoraDescIdDesc();
}
