package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduz as exceções do domínio em resposta HTTP.
 * O domínio continua sem saber nada de HTTP: ele só lança
 * IllegalArgumentException / IllegalStateException com a mensagem da regra.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    // 422 = a requisição está bem formada, mas viola uma regra de negócio.
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ProblemDetail> regraDeNegocio(RuntimeException erro) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(422),
                erro.getMessage()
        );
        problema.setTitle("Operação não permitida pelo domínio");
        return ResponseEntity.status(422).body(problema);
    }
}
