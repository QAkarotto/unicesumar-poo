package br.edu.sistemaacademico.api;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Erro> jsonInvalido(HttpMessageNotReadableException e) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou JSON inválido.");
    }

    private ResponseEntity<Erro> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status)
                .body(new Erro(status.value(), status.getReasonPhrase(), mensagem));
    }
}
