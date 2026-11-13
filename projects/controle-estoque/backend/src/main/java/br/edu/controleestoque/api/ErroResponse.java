package br.edu.controleestoque.api;

import java.time.LocalDateTime;
import java.util.Map;

/** Corpo JSON padronizado de todas as respostas de erro da API. */
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {
}
