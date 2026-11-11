package br.edu.biblioteca.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public record ErroApi(Instant timestamp, int status, String mensagem, String caminho, Map<String, String> campos) {}
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    ResponseEntity<ErroApi> naoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest r) { return erro(404, e.getMessage(), r, Map.of()); }
    @ExceptionHandler(RegraNegocioException.class)
    ResponseEntity<ErroApi> conflito(RegraNegocioException e, HttpServletRequest r) { return erro(409, e.getMessage(), r, Map.of()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErroApi> validar(MethodArgumentNotValidException e, HttpServletRequest r) {
        Map<String, String> campos = new TreeMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> campos.put(f.getField(), f.getDefaultMessage()));
        return erro(400, "Confira os campos informados.", r, campos);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ErroApi> invalido(Exception e, HttpServletRequest r) { return erro(400, "JSON ou parâmetro inválido.", r, Map.of()); }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErroApi> integridade(Exception e, HttpServletRequest r) { return erro(409, "Registro duplicado ou operação incompatível com os dados existentes.", r, Map.of()); }
    @ExceptionHandler(PessimisticLockingFailureException.class)
    ResponseEntity<ErroApi> ocupado(Exception e, HttpServletRequest r) { return erro(409, "Registro em uso. Tente novamente.", r, Map.of()); }
    private ResponseEntity<ErroApi> erro(int status, String mensagem, HttpServletRequest r, Map<String, String> campos) {
        return ResponseEntity.status(status).body(new ErroApi(Instant.now(), status, mensagem, r.getRequestURI(), campos));
    }
}
