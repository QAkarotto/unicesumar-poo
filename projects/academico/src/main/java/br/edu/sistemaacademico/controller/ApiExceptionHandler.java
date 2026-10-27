
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<?> duplicado(
            RegistroDuplicadoException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> dadosInvalidos(
            IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> jsonInvalido(
            HttpMessageNotReadableException e) {

        return ResponseEntity
                .badRequest()
                .body(Map.of("erro", "JSON inválido."));
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<?> erroPersistencia(
            PersistenciaException e) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "erro", "Erro interno de persistência."));
    }
}
