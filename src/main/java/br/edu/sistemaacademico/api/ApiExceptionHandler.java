package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
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

    // 409 = conflito com um registro que já existe no banco.
    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ProblemDetail> duplicado(RegistroDuplicadoException erro) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), erro.getMessage());
        problema.setTitle("Registro duplicado");
        return ResponseEntity.status(409).body(problema);
    }

    // 500 = falha inesperada de acesso ao banco (detalhes não vazam para o cliente).
    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ProblemDetail> erroDeBanco(PersistenciaException erro) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(500), "Erro ao acessar o banco de dados.");
        problema.setTitle("Falha de persistência");
        return ResponseEntity.status(500).body(problema);
    }
}
