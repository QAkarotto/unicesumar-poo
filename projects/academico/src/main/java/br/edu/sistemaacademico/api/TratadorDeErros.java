package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Converte falhas inesperadas de acesso ao banco em HTTP 500, sem expor detalhes do SQL. */
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RepositorioException.class)
    public ResponseEntity<Map<String, Object>> falhaNoBanco(RepositorioException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "error", "Internal Server Error",
                "message", "Erro ao acessar o banco de dados."));
    }
}
