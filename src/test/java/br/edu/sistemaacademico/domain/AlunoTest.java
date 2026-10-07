package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    private Aluno aluno(String registro) {
        return new Aluno(registro, "Ana Souza", "ana@email.com");
    }

    private OfertaDisciplina ofertaDePoo(String codigoTurma, int ano, Semestre semestre) {
        var turma = new Turma(codigoTurma, new PeriodoLetivo(ano, semestre));
        return turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
    }

    @Test
    @DisplayName("Deve rejeitar registro acadêmico em branco")
    void deveRejeitarRegistroEmBranco() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("   ", "Ana Souza", "ana@email.com")
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome nulo")
    void deveRejeitarNomeNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "ana@email.com")
        );
    }

    @Test
    @DisplayName("Deve rejeitar e-mail sem arroba")
    void deveRejeitarEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana.email.com")
        );
    }

    @Test
    @DisplayName("Deve considerar alunos iguais pelo registro acadêmico")
    void deveCompararAlunosPeloRegistroAcademico() {
        var primeiro = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var segundo = new Aluno("ra001", "Outro Nome", "outro@email.com");
        var terceiro = aluno("RA002");

        assertTrue(primeiro.equals(segundo));
        assertEquals(primeiro.hashCode(), segundo.hashCode());
        assertFalse(primeiro.equals(terceiro));
        assertFalse(primeiro.equals("RA001"));
        assertEquals("RA001", primeiro.getRegistroAcademico());
        assertEquals("ana@email.com", primeiro.getEmail());
        assertEquals("RA001 - Ana Souza", primeiro.toString());
    }

    @Test
    @DisplayName("Deve impedir alteração do histórico pela lista devolvida")
    void deveImpedirAlteracaoDoHistorico() {
        var aluno = aluno("RA003");
        var matricula = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.SEGUNDO).matricular(aluno);

        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().remove(matricula)
        );
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve exigir disciplina ao consultar aprovação")
    void deveExigirDisciplinaAoConsultarAprovacao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno("RA004").foiAprovadoEm(null)
        );
    }

    @Test
    @DisplayName("Deve reconhecer aprovação apenas na disciplina cursada")
    void deveReconhecerAprovacaoNaDisciplinaCursada() {
        var aluno = aluno("RA005");
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        ofertaDePoo("ESOFT4S-NA", 2026, Semestre.SEGUNDO)
                .matricular(aluno)
                .concluir(ResultadoAcademico.APROVADO);

        assertTrue(aluno.foiAprovadoEm(poo));
        assertFalse(aluno.foiAprovadoEm(bancoDados));
    }

    @Test
    @DisplayName("Não deve considerar aprovado o aluno reprovado na disciplina")
    void naoDeveConsiderarAprovadoQuemReprovou() {
        var aluno = aluno("RA006");
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        ofertaDePoo("ESOFT4S-NA", 2026, Semestre.SEGUNDO)
                .matricular(aluno)
                .concluir(ResultadoAcademico.REPROVADO);

        assertFalse(aluno.foiAprovadoEm(poo));
    }

    @Test
    @DisplayName("Deve listar apenas as matrículas em curso")
    void deveListarMatriculasEmCurso() {
        var aluno = aluno("RA007");
        var emCurso = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.SEGUNDO).matricular(aluno);

        assertEquals(1, aluno.getMatriculasEmCurso().size());
        assertEquals(emCurso, aluno.getMatriculasEmCurso().get(0));

        emCurso.concluir(ResultadoAcademico.APROVADO);

        assertTrue(aluno.getMatriculasEmCurso().isEmpty());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve recusar matrícula de outro aluno no histórico")
    void deveRecusarMatriculaDeOutroAluno() {
        var ana = aluno("RA008");
        var bruno = new Aluno("RA009", "Bruno Santos", "bruno@email.com");
        var matriculaDoBruno = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.SEGUNDO).matricular(bruno);

        assertThrows(
                IllegalStateException.class,
                () -> ana.registrarMatricula(matriculaDoBruno)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> ana.registrarMatricula(null)
        );
    }
}