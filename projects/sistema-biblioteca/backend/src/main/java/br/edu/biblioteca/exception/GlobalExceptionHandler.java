
package br.edu.biblioteca.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> naoEncontrado(
            NoSuchElementException ex,
            HttpServletRequest request) {

        return resposta(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request,
            List.of()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> regraDeNegocio(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return resposta(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request,
            List.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> detalhes = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(erro ->
                erro.getField() + ": "
                + erro.getDefaultMessage()
            )
            .toList();

        return resposta(
            HttpStatus.BAD_REQUEST,
            "Dados inválidos.",
            request,
            detalhes
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> corpoInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return resposta(
            HttpStatus.BAD_REQUEST,
            "O corpo da requisição está ausente ou contém JSON inválido.",
            request,
            List.of()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> conflitoBanco(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return resposta(
            HttpStatus.CONFLICT,
            "A operação viola uma restrição do banco de dados.",
            request,
            List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> erroInterno(
            Exception ex,
            HttpServletRequest request) {

        return resposta(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocorreu um erro interno no servidor.",
            request,
            List.of()
        );
    }

    private ResponseEntity<ApiError> resposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            List<String> detalhes) {

        ApiError erro = new ApiError(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensagem,
            request.getRequestURI(),
            detalhes
        );

        return ResponseEntity.status(status).body(erro);
    }
}
