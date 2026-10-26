package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratamentoErrosApi {

    private static final Logger log = LoggerFactory.getLogger(TratamentoErrosApi.class);

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<String> duplicado(RegistroDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<String> persistencia(PersistenciaException e) {
        log.error("Falha ao acessar o banco de dados.", e);
        return ResponseEntity.internalServerError().body("Não foi possível acessar o banco de dados.");
    }
}
