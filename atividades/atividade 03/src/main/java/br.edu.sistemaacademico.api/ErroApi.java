package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;

/** Erro da camada web (recurso inexistente, requisição inválida), com o status HTTP correspondente. */
public class ErroApi extends RuntimeException {
    private final HttpStatus status;

    public ErroApi(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
