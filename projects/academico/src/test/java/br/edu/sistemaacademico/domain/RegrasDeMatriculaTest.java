package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de integração cobrindo o fluxo completo de oferta, matrícula e
 * conclusão, atravessando Turma, OfertaDisciplina, Matricula e Aluno.
 */
class RegrasDeMatriculaTest {

    private final PeriodoLetivo periodo2026_1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    private final PeriodoLetivo periodo2026_2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
    private final Disciplina poo = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

    @Test
    void fluxoCompletoDeOfertaMatriculaEAprovacaoDeveFuncionar() {
        // Arrange
        var turma = new Turma("T1", periodo2026_1);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        // Act
        var oferta = turma.ofertarDisciplina(poo);
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertTrue(aluno.getMatriculas().contains(matricula));
        assertTrue(oferta.getMatriculas().contains(matricula));
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertTrue(aluno.jaFoiAprovadoEm(poo));
    }

    @Test
    void alunoAprovadoNaoPodeSeMatricularNovamenteMesmoEmTurmaDeOutroPeriodo() {
        // Arrange: aluno é aprovado em POO01 na turma do primeiro semestre
        var turmaPrimeiroSemestre = new Turma("T1", periodo2026_1);
        var ofertaPrimeiroSemestre = turmaPrimeiroSemestre.ofertarDisciplina(poo);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matriculaAprovada = ofertaPrimeiroSemestre.matricular(aluno);
        matriculaAprovada.concluir(ResultadoAcademico.APROVADO);

        // Act: mesma disciplina, ofertada em turma de outro período letivo
        var turmaSegundoSemestre = new Turma("T2", periodo2026_2);
        var ofertaSegundoSemestre = turmaSegundoSemestre.ofertarDisciplina(poo);

        // Assert
        var excecao = assertThrows(IllegalStateException.class,
                () -> ofertaSegundoSemestre.matricular(aluno));
        assertTrue(excecao.getMessage().contains("já foi aprovado"));
    }

    @Test
    void alunoReprovadoPodeSeMatricularNovamenteEEventualmenteSerAprovado() {
        // Arrange: aluno é reprovado em POO01 na turma do primeiro semestre
        var turmaPrimeiroSemestre = new Turma("T1", periodo2026_1);
        var ofertaPrimeiroSemestre = turmaPrimeiroSemestre.ofertarDisciplina(poo);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var primeiraMatricula = ofertaPrimeiroSemestre.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        // Act: aluno se matricula de novo em outra turma e é aprovado
        var turmaSegundoSemestre = new Turma("T2", periodo2026_2);
        var ofertaSegundoSemestre = turmaSegundoSemestre.ofertarDisciplina(poo);
        var segundaMatricula = assertDoesNotThrow(() -> ofertaSegundoSemestre.matricular(aluno));
        segundaMatricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(2, aluno.getMatriculas().size());
        assertTrue(aluno.jaFoiAprovadoEm(poo));
    }

    @Test
    void alunoPodeCursarDisciplinasDiferentesSimultaneamente() {
        // Arrange
        var turma = new Turma("T1", periodo2026_1);
        var bd = new Disciplina("BD01", "Banco de Dados", 60);
        var ofertaPoo = turma.ofertarDisciplina(poo);
        var ofertaBd = turma.ofertarDisciplina(bd);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        // Act
        ofertaPoo.matricular(aluno);
        ofertaBd.matricular(aluno);

        // Assert
        assertEquals(2, aluno.getMatriculas().size());
    }
}
