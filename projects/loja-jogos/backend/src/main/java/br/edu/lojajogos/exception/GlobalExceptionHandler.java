package br.edu.lojajogos.exception;

import br.edu.lojajogos.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Transforma as exceções em respostas HTTP com JSON.
 * Assim os Controllers não precisam de try/catch.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: id que não existe no banco
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException e) {
        return erro(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 400: dado inválido (nome vazio, preço zero, quantidade negativa...)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> dadoInvalido(IllegalArgumentException e) {
        return erro(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 400: JSON mal formado ou com tipo errado (ex.: "preco": "abc")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException e) {
        return erro(HttpStatus.BAD_REQUEST, "JSON inválido. Confira os campos enviados.");
    }

    // 409: operação não permitida pela regra de negócio (sem estoque, cancelar duas vezes...)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> operacaoNaoPermitida(IllegalStateException e) {
        return erro(HttpStatus.CONFLICT, e.getMessage());
    }

    private ResponseEntity<ErroResponse> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), mensagem));
    }
}