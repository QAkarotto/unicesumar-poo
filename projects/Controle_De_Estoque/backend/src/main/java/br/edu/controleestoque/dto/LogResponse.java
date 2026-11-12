package br.edu.controleestoque.dto;

import br.edu.controleestoque.domain.LogAuditoria;
import java.time.LocalDateTime;

public record LogResponse(
        Long id,
        String acao,
        String entidade,
        Long entidadeId,
        String descricao,
        LocalDateTime dataHora
) {

    public static LogResponse de(LogAuditoria log) {
        return new LogResponse(
                log.getId(),
                log.getAcao().name(),
                log.getEntidade().name(),
                log.getEntidadeId(),
                log.getDescricao(),
                log.getDataHora());
    }
}
