
package br.edu.biblioteca.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String caminho,
    List<String> detalhes
) {
}
