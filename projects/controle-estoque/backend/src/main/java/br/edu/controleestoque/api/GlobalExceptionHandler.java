package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.OperacaoNaoPermitidaException;
import br.edu.controleestoque.domain.RecursoNaoEncontradoException;
import br.edu.controleestoque.domain.RegraNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converte exceções em respostas HTTP coerentes:
 * 400 dados inválidos | 404 não encontrado | 409 conflito | 422 regra de negócio | 500 erro inesperado.
 * Todas as respostas usam o mesmo corpo {@link ErroResponse}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos. Verifique os campos.", req, campos);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroResponse> requisicaoMalFormada(Exception ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Requisição mal formada ou com valores de tipo inválido.", req, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> argumentoInvalido(IllegalArgumentException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), req, null);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<ErroResponse> conflito(OperacaoNaoPermitidaException ex, HttpServletRequest req) {
        return montar(HttpStatus.CONFLICT, ex.getMessage(), req, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException ex, HttpServletRequest req) {
        // O cliente recebe uma mensagem genérica; a causa real (qual restrição) fica registrada no log
        log.warn("Violação de restrição do banco em {} {}: {}", req.getMethod(), req.getRequestURI(),
                ex.getMostSpecificCause().getMessage());
        return montar(HttpStatus.CONFLICT, "A operação viola uma restrição do banco de dados.", req, null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException ex, HttpServletRequest req) {
        return montar(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), req, null);
    }

    /**
     * Rede de segurança para o que não foi tratado acima.
     * Erros do próprio Spring MVC (rota inexistente, método HTTP não suportado...) mantêm o status
     * original; qualquer outra exceção é um erro inesperado: vai para o log com a stack trace
     * e o cliente recebe 500 sem detalhes internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> inesperado(Exception ex, HttpServletRequest req) {
        if (ex instanceof ErrorResponse erroDoSpring) {
            HttpStatus status = HttpStatus.valueOf(erroDoSpring.getStatusCode().value());
            String mensagem = status == HttpStatus.NOT_FOUND
                    ? "Rota não encontrada."
                    : "Requisição não suportada pela API.";
            return montar(status, mensagem, req, null);
        }
        log.error("Erro inesperado em {} {}", req.getMethod(), req.getRequestURI(), ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado. Tente novamente mais tarde.", req, null);
    }

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem,
                                                HttpServletRequest req, Map<String, String> campos) {
        var corpo = new ErroResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                mensagem, req.getRequestURI(), campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
