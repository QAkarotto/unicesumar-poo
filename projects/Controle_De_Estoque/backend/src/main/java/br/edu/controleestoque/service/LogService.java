package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.AcaoLog;
import br.edu.controleestoque.domain.EntidadeLog;
import br.edu.controleestoque.domain.LogAuditoria;
import br.edu.controleestoque.dto.LogResponse;
import br.edu.controleestoque.repository.LogAuditoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registra a auditoria das ações. O método registrar participa da transação
 * do serviço que o chamou: se o log não for gravado, a ação inteira é desfeita.
 * Este serviço só insere e consulta; não existe operação de exclusão de logs.
 */
@Service
public class LogService {

    private final LogAuditoriaRepository repository;

    public LogService(LogAuditoriaRepository repository) {
        this.repository = repository;
    }

    public void registrar(AcaoLog acao, EntidadeLog entidade, Long entidadeId, String descricao) {
        repository.save(new LogAuditoria(acao, entidade, entidadeId, descricao));
    }

    @Transactional(readOnly = true)
    public List<LogResponse> listar() {
        return repository.findAllByOrderByDataHoraDescIdDesc().stream()
                .map(LogResponse::de)
                .toList();
    }
}
