package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.persistencia.PersistenciaException;
import br.edu.sistemaacademico.persistencia.RegistroDuplicadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratamentoErros {
    private static final Logger LOG = LoggerFactory.getLogger(TratamentoErros.class);

    public record ErroResponse(String mensagem) {}

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException erro) {
        return ResponseEntity.status(404).body(new ErroResponse(erro.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> invalido(IllegalArgumentException erro) {
        return ResponseEntity.badRequest().body(new ErroResponse(erro.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> conflito(IllegalStateException erro) {
        return ResponseEntity.status(409).body(new ErroResponse(erro.getMessage()));
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroResponse> duplicado(RegistroDuplicadoException erro) {
        return ResponseEntity.status(409).body(new ErroResponse(erro.getMessage()));
    }

    // o detalhe do SQLException fica no log, nao vai pra resposta
    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErroResponse> falhaNoBanco(PersistenciaException erro) {
        LOG.error(erro.getMessage(), erro);
        return ResponseEntity.status(500).body(new ErroResponse(erro.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido() {
        return ResponseEntity.badRequest().body(new ErroResponse("Corpo JSON ausente ou inválido."));
    }
}
