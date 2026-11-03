package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converte exceções em respostas HTTP com JSON {"mensagem": "..."}.
 * Assim os Controllers ficam curtos e todos os erros têm o mesmo formato.
 *
 * (O MatriculaController continua tratando os próprios erros, como na Atividade 02.)
 */
@RestControllerAdvice
public class TratamentoErros {

    // Dado inválido recusado pelo domínio (ex.: e-mail inválido, carga horária <= 0)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> dadoInvalido(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
    }

    // Corpo ausente ou JSON mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(new ErroResponse("JSON inválido ou ausente no corpo da requisição."));
    }

    // RA ou código de disciplina já cadastrado
    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroResponse> duplicado(RegistroDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponse(e.getMessage()));
    }

    // Falha inesperada no banco: não expõe detalhes do SQL para o cliente
    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErroResponse> erroBanco(PersistenciaException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse("Erro ao acessar o banco de dados."));
    }
}
