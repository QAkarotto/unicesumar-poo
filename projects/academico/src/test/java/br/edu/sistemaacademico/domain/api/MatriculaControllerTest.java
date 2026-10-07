package br.edu.sistemaacademico.domain.api;

import br.edu.sistemaacademico.api.BaseEmMemoria;
import br.edu.sistemaacademico.api.MatriculaController;
import br.edu.sistemaacademico.api.MatriculaRequest;
import br.edu.sistemaacademico.api.MatriculaResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatriculaControllerTest {

    private final BaseEmMemoria base = new BaseEmMemoria();
    private final MatriculaController controller = new MatriculaController(base);

    @Test
    void consultaMatriculaExistente() {
        var resposta = controller.consultar("MAT-001");

        assertEquals(200, resposta.getStatusCode().value());
        var corpo = (MatriculaResponse) resposta.getBody();
        assertEquals("RA2026001", corpo.registroAcademico());
    }

    @Test
    void matriculaInexistenteRetorna404() {
        assertEquals(404, controller.consultar("MAT-999").getStatusCode().value());
    }

    @Test
    void criaMatriculaValida() {
        var pedido = new MatriculaRequest("RA2026002", "ESOFT4S-NB", "BD");

        var resposta = controller.criar(pedido);

        assertEquals(201, resposta.getStatusCode().value());
        var corpo = (MatriculaResponse) resposta.getBody();
        assertEquals("MAT-004", corpo.codigo());
    }

    @Test
    void matriculaDuplicadaRetorna422() {
        var pedido = new MatriculaRequest("RA2026002", "ESOFT4S-NB", "POO");

        assertEquals(422, controller.criar(pedido).getStatusCode().value());
    }

    @Test
    void alunoJaAprovadoRetorna422() {
        var pedido = new MatriculaRequest("RA2026001", "ADSIS4S", "POO");

        assertEquals(422, controller.criar(pedido).getStatusCode().value());
    }

    @Test
    void camposVaziosRetornam400() {
        var pedido = new MatriculaRequest("", "ESOFT4S-NB", "BD");

        assertEquals(400, controller.criar(pedido).getStatusCode().value());
    }

    @Test
    void alunoInexistenteRetorna404() {
        var pedido = new MatriculaRequest("RA0000000", "ESOFT4S-NB", "BD");

        assertEquals(404, controller.criar(pedido).getStatusCode().value());
    }

    @Test
    void ofertaInexistenteRetorna404() {
        var pedido = new MatriculaRequest("RA2026002", "ESOFT4S-NB", "XYZ");

        assertEquals(404, controller.criar(pedido).getStatusCode().value());
    }
}