package br.edu.aniva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NaoAutenticadoException.class)
    public ResponseEntity<Map<String, String>> naoAutenticado(NaoAutenticadoException e) {
        return resposta(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<Map<String, String>> naoPermitido(OperacaoNaoPermitidaException e) {
        return resposta(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> regraDeNegocio(RegraNegocioException e) {
        return resposta(HttpStatus.CONFLICT, e.getMessage());
    }

    // Dados inválidos vindos da requisição (@Valid) ou rejeitados pelo domínio.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> dadosInvalidos(MethodArgumentNotValidException e) {
        var erro = e.getBindingResult().getFieldError();
        String mensagem = erro != null ? erro.getDefaultMessage() : "Dados inválidos.";
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> requisicaoInvalida(Exception e) {
        String mensagem = e instanceof IllegalArgumentException ? e.getMessage() : "Corpo da requisição inválido.";
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    private ResponseEntity<Map<String, String>> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("mensagem", mensagem));
    }
}
