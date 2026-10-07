package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//JSON devolvido quando algo dá erro
public record ErroResponse(String mensagem) {

    public static ResponseEntity<ErroResponse> de(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(mensagem));
    }
}
