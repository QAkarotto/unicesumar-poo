package br.edu.sistemaacademico.exception;

import br.edu.sistemaacademico.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse dadoInvalido(IllegalArgumentException e) {
        return new ErroResponse(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse corpoInvalido(HttpMessageNotReadableException e) {
        return new ErroResponse("Dados da requisição inválidos.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse parametroInvalido(MethodArgumentTypeMismatchException e) {
        return new ErroResponse("Parâmetro inválido: " + e.getName());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse naoEncontrado(RecursoNaoEncontradoException e) {
        return new ErroResponse(e.getMessage());
    }

    @ExceptionHandler({RegistroDuplicadoException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResponse conflito(RuntimeException e) {
        return new ErroResponse(e.getMessage());
    }
}
