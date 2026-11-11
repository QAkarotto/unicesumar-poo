package br.edu.agroassist.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public record Erro(String codigo, String mensagem, LocalDateTime em) {}

    private ResponseEntity<Erro> resposta(HttpStatusCode status, String codigo, String mensagem) {
        return ResponseEntity.status(status).body(new Erro(codigo, mensagem, LocalDateTime.now()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Erro> regra(RegraNegocioException e) {
        return resposta(HttpStatus.CONFLICT, e.getCodigo(), e.getMessage());
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<Erro> dados(DadosInvalidosException e) {
        return resposta(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<Erro> proibido(OperacaoNaoPermitidaException e) {
        return resposta(HttpStatus.FORBIDDEN, "OPERACAO_NAO_PERMITIDA", e.getMessage());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Erro> ausente(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, "NAO_ENCONTRADO", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erro> campos(MethodArgumentNotValidException e) {
        String mensagem =
                e.getBindingResult().getFieldErrors().stream()
                        .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                        .sorted()
                        .findFirst()
                        .orElse("Revise os campos informados.");
        return resposta(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", mensagem);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Erro> formato(Exception e) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "JSON_INVALIDO",
                "Revise o JSON, os tipos dos campos e as datas (AAAA-MM-DD).");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Erro> cabecalho(MissingRequestHeaderException e) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "USUARIO_NAO_INFORMADO",
                "Informe o usuário de demonstração no cabeçalho X-Usuario-Id.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Erro> integridade(DataIntegrityViolationException e) {
        return resposta(
                HttpStatus.CONFLICT,
                "CONFLITO",
                "A operação conflita com um registro existente. Atualize a tela e confira os dados.");
    }

    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ResponseEntity<Erro> concorrencia(PessimisticLockingFailureException e) {
        return resposta(
                HttpStatus.CONFLICT,
                "CONCORRENCIA",
                "Outra operação está alterando estes dados. Atualize a tela e tente novamente.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Erro> inesperado(Exception e) {
        if (e instanceof ErrorResponse erro) {
            String mensagem = erro.getBody().getDetail();
            return resposta(
                    erro.getStatusCode(),
                    "REQUISICAO_INVALIDA",
                    mensagem == null ? "Requisição inválida." : mensagem);
        }
        LOG.error("Falha inesperada na requisição", e);
        return resposta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "ERRO_INTERNO",
                "Não foi possível concluir a operação. Verifique o backend e o banco de dados.");
    }
}
