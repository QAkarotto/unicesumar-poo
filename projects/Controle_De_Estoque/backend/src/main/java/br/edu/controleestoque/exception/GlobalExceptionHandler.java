package br.edu.controleestoque.exception;

import br.edu.controleestoque.dto.ErroResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduz as exceções da aplicação para respostas HTTP coerentes:
 * - 404: recurso inexistente;
 * - 400: dados inválidos (validação dos DTOs ou argumento recusado pelo domínio);
 * - 409: operação não permitida pelo estado atual (regra de negócio).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse recursoNaoEncontrado(RecursoNaoEncontradoException excecao) {
        return new ErroResponse(excecao.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse dadoInvalido(IllegalArgumentException excecao) {
        return new ErroResponse(excecao.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse validacao(MethodArgumentNotValidException excecao) {
        var mensagem = excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getDefaultMessage())
                .sorted()
                .reduce((primeira, segunda) -> primeira + " " + segunda)
                .orElse("Dados inválidos.");
        return new ErroResponse(mensagem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse corpoIlegivel(HttpMessageNotReadableException excecao) {
        return new ErroResponse("Corpo da requisição inválido.");
    }

    @ExceptionHandler({IllegalStateException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResponse operacaoNaoPermitida(RuntimeException excecao) {
        if (excecao instanceof DataIntegrityViolationException) {
            return new ErroResponse("A operação viola uma restrição do banco de dados.");
        }
        return new ErroResponse(excecao.getMessage());
    }
}
