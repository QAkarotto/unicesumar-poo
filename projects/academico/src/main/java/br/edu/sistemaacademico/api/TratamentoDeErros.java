package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converte as exceções lançadas pelo domínio em respostas HTTP.
 * IllegalArgumentException: dado inválido ou matrícula repetida na oferta -> 400.
 * IllegalStateException: operação impedida pelo estado atual (ex.: aluno já aprovado) -> 409.
 */
@RestControllerAdvice
public class TratamentoDeErros {

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail argumentoInvalido(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail operacaoNaoPermitida(IllegalStateException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }
}
