package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RepositorioException.class)
    public ResponseEntity<ErroResposta> falhaNoBanco(RepositorioException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResposta("Erro interno ao acessar o banco de dados."));
    }
}
