package br.edu.sistemaacademico.domain.api;

import br.edu.sistemaacademico.api.AlunoController;
import br.edu.sistemaacademico.api.AlunoResponse;
import br.edu.sistemaacademico.api.BaseEmMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlunoControllerTest {

    private final AlunoController controller = new AlunoController(new BaseEmMemoria());

    @Test
    void consultaAlunoExistente() {
        var resposta = controller.consultar("RA2026001");

        assertEquals(200, resposta.getStatusCode().value());
        var corpo = (AlunoResponse) resposta.getBody();
        assertEquals(2, corpo.matriculas().size());
    }

    @Test
    void alunoInexistenteRetorna404() {
        assertEquals(404, controller.consultar("RA0000000").getStatusCode().value());
    }
}