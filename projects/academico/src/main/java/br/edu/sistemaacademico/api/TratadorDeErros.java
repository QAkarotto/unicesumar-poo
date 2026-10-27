package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.infra.ErroDeAcessoADadosException;
import br.edu.sistemaacademico.api.infra.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> regraDeNegocio(IllegalStateException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> dadoInvalido(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroResponse> registroDuplicado(RegistroDuplicadoException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(ErroDeAcessoADadosException.class)
    public ResponseEntity<ErroResponse> erroDeBanco(ErroDeAcessoADadosException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse("Erro ao acessar o banco de dados"));
    }
}