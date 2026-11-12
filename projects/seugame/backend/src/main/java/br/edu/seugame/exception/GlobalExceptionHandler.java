package br.edu.seugame.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = criarProblema(status);
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        problem.setProperty("errors", errors);
        return super.handleExceptionInternal(exception, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return super.handleExceptionInternal(exception, criarProblema(status), headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception exception) {
        LOG.error("Falha inesperada ao processar requisição", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarProblema(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler({IllegalArgumentException.class, br.edu.seugame.domain.OperacaoNaoPermitidaException.class, ConflitoException.class,
            RecursoNaoEncontradoException.class, org.springframework.dao.DataIntegrityViolationException.class,
            org.springframework.security.core.AuthenticationException.class, org.springframework.security.access.AccessDeniedException.class,
            org.springframework.dao.ConcurrencyFailureException.class})
    public ResponseEntity<ProblemDetail> handleApplication(Exception exception) {
        int status=400; String detail=exception.getMessage();
        if(exception instanceof org.springframework.security.core.AuthenticationException) {status=401;detail="Usuário, e-mail ou senha incorretos.";}
        else if(exception instanceof org.springframework.security.access.AccessDeniedException) {status=403;detail="Você não pode gerenciar este recurso.";}
        else if(exception instanceof RecursoNaoEncontradoException) status=404;
        else if(exception instanceof IllegalStateException || exception instanceof ConflitoException
                || exception instanceof br.edu.seugame.domain.ParticipanteDuplicadoException) status=409;
        else if(exception instanceof org.springframework.dao.DataIntegrityViolationException) {status=409;detail="Os dados conflitam com um registro ou restrição existente.";}
        else if(exception instanceof org.springframework.dao.ConcurrencyFailureException) {status=409;detail="Conflito de atualização. Tente novamente.";}
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status),detail));
    }

    private ProblemDetail criarProblema(HttpStatusCode status) {
        String detail = switch (status.value()) {
            case 400 -> "A requisição contém dados inválidos.";
            case 404 -> "O recurso solicitado não foi encontrado.";
            case 405 -> "O método HTTP não é permitido para este recurso.";
            case 406 -> "O formato de resposta solicitado não está disponível.";
            case 415 -> "O formato do corpo da requisição não é suportado.";
            default -> status.is5xxServerError()
                    ? "Não foi possível concluir a operação. Tente novamente."
                    : "Não foi possível processar a requisição.";
        };
        return ProblemDetail.forStatusAndDetail(status, detail);
    }
}
