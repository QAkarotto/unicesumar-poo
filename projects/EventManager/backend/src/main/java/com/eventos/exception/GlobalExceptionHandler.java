
package com.eventos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> tratarErro(
            ResponseStatusException ex) {

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", ex.getStatusCode().value());
        resposta.put("mensagem", ex.getReason());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroInterno(
            Exception ex) {

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", 500);
        resposta.put("mensagem", "Erro interno do servidor");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resposta);
    }
}
