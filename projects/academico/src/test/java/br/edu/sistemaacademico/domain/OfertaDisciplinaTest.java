package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaDisciplinaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    private final Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

    @Test
    void naoDeveCriarOfertaComDisciplinaNula() {
        var turma = new Turma("T1", periodo);

        assertThrows(IllegalArgumentException.class, () -> new OfertaDisciplina(null, turma));
    }

    @Test
    void naoDeveCriarOfertaComTurmaNula() {
        assertThrows(IllegalArgumentException.class, () -> new OfertaDisciplina(disciplina, null));
    }

    @Test
    void deveMatricularAlunoNaOferta() {
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        var matricula = oferta.matricular(aluno);

        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertTrue(oferta.getMatriculas().contains(matricula));
    }

    @Test
    void naoDeveMatricularAlunoNulo() {
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> oferta.matricular(null));
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaDoMesmoAlunoNaMesmaOferta() {
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        oferta.matricular(aluno);

        assertThrows(IllegalStateException.class, () -> oferta.matricular(aluno));
    }

    @Test
    void devePermitirMatriculaDeAlunosDiferentesNaMesmaOferta() {
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno1 = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var aluno2 = new Aluno("RA002", "João Souza", "joao@email.com");

        oferta.matricular(aluno1);
        oferta.matricular(aluno2);

        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    void naoDevePermitirNovaMatriculaDeAlunoJaAprovadoNaDisciplinaMesmoEmOutraTurma() {
        var turma1 = new Turma("T1", periodo);
        var oferta1 = turma1.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matriculaAnterior = oferta1.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.APROVADO);

        var turma2 = new Turma("T2", periodo);
        var oferta2 = turma2.ofertarDisciplina(disciplina);

        assertThrows(IllegalStateException.class, () -> oferta2.matricular(aluno));
    }

    @Test
    void devePermitirNovaMatriculaDeAlunoReprovadoNaDisciplinaMesmoEmOutraTurma() {
        var turma1 = new Turma("T1", periodo);
        var oferta1 = turma1.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matriculaAnterior = oferta1.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.REPROVADO);

        var turma2 = new Turma("T2", periodo);
        var oferta2 = turma2.ofertarDisciplina(disciplina);

        var novaMatricula = oferta2.matricular(aluno);

        assertEquals(aluno, novaMatricula.getAluno());
        assertEquals(oferta2, novaMatricula.getOfertaDisciplina());
    }

    @Test
    void getMatriculasNaoDeveExporColecaoInternaParaModificacao() {
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matricula = oferta.matricular(aluno);

        var matriculas = oferta.getMatriculas();

        assertThrows(UnsupportedOperationException.class, () -> matriculas.add(matricula));
    }

    @Test
    void toStringDeveConterDisciplinaECodigoDaTurma() {
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);

        var texto = oferta.toString();

        assertTrue(texto.contains("Programação Orientada a Objetos"));
        assertTrue(texto.contains("T1"));
    }
}