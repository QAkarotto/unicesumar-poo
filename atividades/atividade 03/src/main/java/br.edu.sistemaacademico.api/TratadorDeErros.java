package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converte exceções em respostas HTTP com corpo JSON.
 * As regras continuam no domínio: ele lança IllegalArgumentException/IllegalStateException
 * e aqui apenas traduzimos isso para o status 422.
 */
@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger log = LoggerFactory.getLogger(TratadorDeErros.class);

    public record Erro(int status, String erro, String mensagem) {
    }

    @ExceptionHandler(ErroApi.class)
    public ResponseEntity<Erro> erroApi(ErroApi e) {
        return resposta(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Erro> regraDeDominio(RuntimeException e) {
        return resposta(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<Erro> duplicado(RegistroDuplicadoException e) {
        return resposta(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<Erro> falhaNoBanco(PersistenciaException e) {
        log.error("Falha de persistência", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Erro> jsonInvalido(HttpMessageNotReadableException e) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou JSON inválido.");
    }

    private ResponseEntity<Erro> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status)
                .body(new Erro(status.value(), status.getReasonPhrase(), mensagem));
    }
}
