package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import br.edu.sistemaacademico.repository.jdbc.ConexaoBanco;
import br.edu.sistemaacademico.repository.jdbc.JdbcAlunoRepository;
import br.edu.sistemaacademico.repository.jdbc.JdbcDisciplinaRepository;
import br.edu.sistemaacademico.support.BancoTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiTest {

    @TempDir
    Path diretorio;

    private ConexaoBanco banco;
    private DadosAcademicos dados;
    private AlunoController alunoController;
    private DisciplinaController disciplinaController;
    private MatriculaController matriculaController;

    @BeforeEach
    void prepararApi() throws Exception {
        banco = BancoTeste.preparar(diretorio.resolve("academico.db"));
        var alunos = new JdbcAlunoRepository(banco);
        var disciplinas = new JdbcDisciplinaRepository(banco);
        dados = new DadosAcademicos(alunos, disciplinas);
        alunoController = new AlunoController(alunos, dados);
        disciplinaController = new DisciplinaController(disciplinas);
        matriculaController = new MatriculaController(dados);
    }

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

    @Test
    void deveCadastrarAlunoERetornar201ComLocalizacao() {
        var response = alunoController.criar(
                new CriarAlunoRequest("RA-NOVO", "Maria Souza", "maria@email.com"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/api/alunos/RA-NOVO", response.getHeaders().getLocation().toString());
        assertNotNull(response.getBody());
        assertEquals("RA-NOVO", response.getBody().registroAcademico());
        assertEquals("Maria Souza", response.getBody().nome());
        assertEquals("maria@email.com", response.getBody().email());
        assertTrue(response.getBody().matriculas().isEmpty());
        assertEquals(HttpStatus.OK, alunoController.buscar("ra-novo").getStatusCode());
    }

    @Test
    void deveCadastrarDisciplinaERetornar201ComLocalizacao() {
        var response = disciplinaController.criar(
                new CriarDisciplinaRequest("REDES", "Redes de Computadores", 60));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/api/disciplinas/REDES", response.getHeaders().getLocation().toString());
        assertNotNull(response.getBody());
        assertEquals("REDES", response.getBody().codigo());
        assertEquals("Redes de Computadores", response.getBody().nome());
        assertEquals(60, response.getBody().cargaHoraria());
    }

    @Test
    void deveConsultarDisciplinaSemDiferenciarMaiusculas() {
        var response = disciplinaController.buscar("poo");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("POO", response.getBody().codigo());
        assertEquals("Programação Orientada a Objetos", response.getBody().nome());
        assertEquals(80, response.getBody().cargaHoraria());
    }

    @Test
    void deveRetornar404ParaDisciplinaInexistente() {
        assertEquals(HttpStatus.NOT_FOUND,
                disciplinaController.buscar("INEXISTENTE").getStatusCode());
    }

    @Test
    void deveRejeitarAlunoComDadosInvalidos() {
        assertEquals(HttpStatus.BAD_REQUEST, alunoController.criar(
                new CriarAlunoRequest("RA-NOVO", "Maria", "email-invalido")).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, alunoController.criar(
                new CriarAlunoRequest("", "Maria", "maria@email.com")).getStatusCode());
    }

    @Test
    void deveRejeitarDisciplinaComDadosInvalidos() {
        assertEquals(HttpStatus.BAD_REQUEST, disciplinaController.criar(
                new CriarDisciplinaRequest("REDES", "Redes", 0)).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, disciplinaController.criar(
                new CriarDisciplinaRequest("", "Redes", 60)).getStatusCode());
    }

    @Test
    void deveRejeitarRequisicoesNulas() {
        assertEquals(HttpStatus.BAD_REQUEST, alunoController.criar(null).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, disciplinaController.criar(null).getStatusCode());
    }

    @Test
    void deveRetornar409ParaCadastroDuplicado() {
        var tratamento = new TratamentoErrosApi();
        var erroAluno = assertThrows(RegistroDuplicadoException.class,
                () -> alunoController.criar(
                        new CriarAlunoRequest("ra2026001", "Outro aluno", "outro@email.com")));
        var erroDisciplina = assertThrows(RegistroDuplicadoException.class,
                () -> disciplinaController.criar(
                        new CriarDisciplinaRequest("poo", "Outra disciplina", 40)));

        assertEquals(HttpStatus.CONFLICT, tratamento.duplicado(erroAluno).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, tratamento.duplicado(erroDisciplina).getStatusCode());
    }

    @Test
    void deveRetornar500ParaFalhaDePersistencia() {
        var semTabelas = new JdbcDisciplinaRepository(
                new ConexaoBanco("jdbc:sqlite:" + diretorio.resolve("sem-tabelas.db")));
        var controller = new DisciplinaController(semTabelas);
        var erro = assertThrows(PersistenciaException.class, () -> controller.buscar("POO"));

        var response = new TratamentoErrosApi().persistencia(erro);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deveConsultarCadastrosComNovasInstanciasDaApi() {
        alunoController.criar(new CriarAlunoRequest("RA-NOVO", "Maria", "maria@email.com"));
        disciplinaController.criar(new CriarDisciplinaRequest("REDES", "Redes", 60));

        var novaConexao = new ConexaoBanco("jdbc:sqlite:" + diretorio.resolve("academico.db"));
        var alunos = new JdbcAlunoRepository(novaConexao);
        var disciplinas = new JdbcDisciplinaRepository(novaConexao);
        var novosDados = new DadosAcademicos(alunos, disciplinas);
        var novaApiAlunos = new AlunoController(alunos, novosDados);
        var novaApiDisciplinas = new DisciplinaController(disciplinas);

        assertEquals("Maria", novaApiAlunos.buscar("RA-NOVO").getBody().nome());
        assertEquals(60, novaApiDisciplinas.buscar("REDES").getBody().cargaHoraria());
    }

    @Test
    void devePreservarIdentidadeEHistoricoDoAlunoAoConsultarRepository() {
        var aluno = dados.buscarAluno("ra2026001");

        assertSame(aluno, dados.buscarAluno("RA2026001"));
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void deveManterMatriculasDoNovoAlunoEmMemoria() {
        alunoController.criar(new CriarAlunoRequest("RA-NOVO", "Maria", "maria@email.com"));

        var matricula = matriculaController.criar(
                new CriarMatriculaRequest("RA-NOVO", "ESOFT4S-NB", "BD"));
        var aluno = alunoController.buscar("ra-novo");

        assertEquals(HttpStatus.CREATED, matricula.getStatusCode());
        assertEquals(1, aluno.getBody().matriculas().size());
        assertTrue(aluno.getBody().matriculas().contains(matricula.getBody().codigo()));
    }
}
