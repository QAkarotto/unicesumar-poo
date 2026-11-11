package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;
import br.edu.sistemaacademico.dto.ConcluirMatriculaRequest;
import br.edu.sistemaacademico.dto.CriarAlunoRequest;
import br.edu.sistemaacademico.dto.CriarDisciplinaRequest;
import br.edu.sistemaacademico.dto.CriarMatriculaRequest;
import br.edu.sistemaacademico.service.AlunoService;
import br.edu.sistemaacademico.service.DisciplinaService;
import br.edu.sistemaacademico.service.MatriculaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AtividadeFinalFullStackTest {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private MatriculaService matriculaService;

    @Test
    void deveCadastrarEConsultarAlunoComJpa() {
        String ra = "RA-FINAL-" + System.nanoTime();
        var aluno = alunoService.cadastrar(new CriarAlunoRequest(ra, null, "Aluno Teste Final", "final@example.com"));

        assertNotNull(aluno);
        assertEquals(ra, aluno.registroAcademico());
        assertEquals("Aluno Teste Final", aluno.nome());

        var buscado = alunoService.buscarPorRa(ra);
        assertEquals(aluno.registroAcademico(), buscado.registroAcademico());
        assertEquals("final@example.com", buscado.email());
    }

    @Test
    void deveCadastrarEConsultarDisciplinaComJpa() {
        String cod = "DISC-FINAL-" + System.nanoTime();
        var disciplina = disciplinaService.cadastrar(new CriarDisciplinaRequest(cod, "Disciplina Final JPA", 75, null));

        assertNotNull(disciplina);
        assertEquals(cod, disciplina.codigo());
        assertEquals(75, disciplina.cargaHoraria());

        var buscada = disciplinaService.buscarPorCodigo(cod);
        assertEquals(cod, buscada.codigo());
    }

    @Test
    void regraDeNegocioNaoDevePermitirMatriculaDuplicadaNaMesmaOferta() {
        String ra = "RA-DUP-" + System.nanoTime();
        alunoService.cadastrar(new CriarAlunoRequest(ra, null, "Aluno Duplicidade", "dup@example.com"));

        var m1 = matriculaService.matricular(new CriarMatriculaRequest(ra, "POO-2026-2", null, null));
        assertNotNull(m1);
        assertEquals(SituacaoMatricula.ATIVA, m1.situacao());

        // Segunda tentativa na mesma oferta deve falhar com IllegalArgumentException
        var erro = assertThrows(IllegalArgumentException.class, () ->
                matriculaService.matricular(new CriarMatriculaRequest(ra, "POO-2026-2", null, null)));
        assertEquals("O aluno já está matriculado nesta oferta.", erro.getMessage());
    }

    @Test
    void regraDeNegocioNaoDevePermitirNovaMatriculaAposAprovacao() {
        String ra = "RA-APROV-" + System.nanoTime();
        alunoService.cadastrar(new CriarAlunoRequest(ra, null, "Aluno Aprovado", "aprov@example.com"));

        // 1. Matricula na oferta de POO
        var m1 = matriculaService.matricular(new CriarMatriculaRequest(ra, "POO-2026-2", null, null));

        // 2. Conclui com APROVADO
        matriculaService.concluir(m1.id(), new ConcluirMatriculaRequest(ResultadoAcademico.APROVADO));

        // 3. Tenta matricular novamente na mesma disciplina (mesma matéria) -> deve falhar com IllegalStateException
        var erro = assertThrows(IllegalStateException.class, () ->
                matriculaService.matricular(new CriarMatriculaRequest(ra, "POO-2026-2", null, null)));
        assertEquals("O aluno já foi aprovado nesta disciplina.", erro.getMessage());
    }

    @Test
    void regraDeNegocioCicloDeVidaTransicoesDeEstadoMatricula() {
        String ra = "RA-ESTADO-" + System.nanoTime();
        alunoService.cadastrar(new CriarAlunoRequest(ra, null, "Aluno Estados", "estados@example.com"));

        var m = matriculaService.matricular(new CriarMatriculaRequest(ra, "BD-2026-2", null, null));
        assertEquals(SituacaoMatricula.ATIVA, m.situacao());
        assertNull(m.resultado());

        // Conclusão com aprovação
        var concluida = matriculaService.concluir(m.id(), new ConcluirMatriculaRequest(ResultadoAcademico.APROVADO));
        assertEquals(SituacaoMatricula.CONCLUIDA, concluida.situacao());
        assertEquals(ResultadoAcademico.APROVADO, concluida.resultado());

        // Invariante: Não é possível concluir novamente ou trancar uma matrícula já CONCLUIDA
        assertThrows(IllegalStateException.class, () ->
                matriculaService.concluir(m.id(), new ConcluirMatriculaRequest(ResultadoAcademico.REPROVADO)));

        assertThrows(IllegalStateException.class, () ->
                matriculaService.trancar(m.id()));

        assertThrows(IllegalStateException.class, () ->
                matriculaService.cancelar(m.id()));
    }

    @Test
    void regraDeNegocioCancelamentoETrancamentoDeMatricula() {
        String ra1 = "RA-CANCEL-" + System.nanoTime();
        alunoService.cadastrar(new CriarAlunoRequest(ra1, null, "Aluno Cancelar", "cancelar@example.com"));
        var m1 = matriculaService.matricular(new CriarMatriculaRequest(ra1, "WEB-2026-2", null, null));

        var cancelada = matriculaService.cancelar(m1.id());
        assertEquals(SituacaoMatricula.CANCELADA, cancelada.situacao());

        // Exclusão permitida
        matriculaService.excluir(m1.id());
        assertThrows(Exception.class, () -> matriculaService.buscarPorId(m1.id()));
    }
}
