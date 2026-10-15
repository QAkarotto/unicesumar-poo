package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Corpo JSON padrão para erros: { "erro": "mensagem" }. */
public record ErroResponse(String erro) {

    public static ResponseEntity<ErroResponse> com(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(mensagem));
    }
}
