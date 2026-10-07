package br.edu.sistemaacademico.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiTest {

    private final DadosAcademicos dados = new DadosAcademicos();
    private final AlunoController alunoController = new AlunoController(dados);
    private final MatriculaController matriculaController = new MatriculaController(dados);

    @Test
    @DisplayName("Deve consultar aluno existente e retornar 200")
    void deveConsultarAluno() {
        var response = alunoController.buscar("RA2026001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Paola Oliveira", response.getBody().nome());
        assertEquals(2, response.getBody().matriculas().size());
    }

    @Test
    @DisplayName("Deve retornar 404 para aluno inexistente")
    void deveRetornar404ParaAlunoInexistente() {
        assertEquals(HttpStatus.NOT_FOUND, alunoController.buscar("RA9999999").getStatusCode());
    }

    @Test
    @DisplayName("Deve consultar matrícula existente e retornar 200")
    void deveConsultarMatricula() {
        var response = matriculaController.buscar("ESOFT4S-NB-POO-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RA2026001", response.getBody().registroAcademicoAluno());
        assertEquals("APROVADO", response.getBody().resultado());
    }

    @Test
    @DisplayName("Deve retornar 404 para matrícula inexistente")
    void deveRetornar404ParaMatriculaInexistente() {
        assertEquals(HttpStatus.NOT_FOUND,
                matriculaController.buscar("MAT-INEXISTENTE").getStatusCode());
    }

    @Test
    @DisplayName("Deve criar matrícula e retornar 201")
    void deveCriarMatricula() {
        var response = matriculaController.criar(
                new CriarMatriculaRequest("RA2026002", "ESOFT4S-NB", "BD")
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ESOFT4S-NB-BD-001", response.getBody().codigo());
        assertEquals("ATIVA", response.getBody().situacao());
        assertEquals(null, response.getBody().resultado());
    }

    @Test
    @DisplayName("Deve retornar 404 quando o aluno da matrícula não existe")
    void deveRetornar404ParaAlunoDaMatriculaInexistente() {
        var response = matriculaController.criar(
                new CriarMatriculaRequest("RA9999999", "ESOFT4S-NB", "BD")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 404 quando a oferta não existe")
    void deveRetornar404ParaOfertaInexistente() {
        var response = matriculaController.criar(
                new CriarMatriculaRequest("RA2026002", "TURMA-INEXISTENTE", "BD")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 409 quando uma regra de matrícula for violada")
    void deveRetornar409ParaRegraDoDominio() {
        var response = matriculaController.criar(
                new CriarMatriculaRequest("RA2026001", "ADSIS4S", "POO")
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 400 para requisição de criação incompleta")
    void deveRejeitarRequisicaoInvalida() {
        var response = matriculaController.criar(
                new CriarMatriculaRequest("", "ESOFT4S-NB", "BD")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
