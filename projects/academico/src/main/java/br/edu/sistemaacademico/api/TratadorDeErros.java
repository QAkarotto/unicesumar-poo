package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.ErroDePersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {AlunoController.class, DisciplinaController.class})
public class TratadorDeErros {

    private static final Logger LOG = LoggerFactory.getLogger(TratadorDeErros.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> dadosInvalidos(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<Map<String, String>> duplicado(RegistroDuplicadoException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(ErroDePersistenciaException.class)
    public ResponseEntity<Map<String, String>> falhaNoBanco(ErroDePersistenciaException e) {
        LOG.error("Falha de persistência: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro ao acessar o banco de dados."));
    }
}
