package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    private static Aluno aluno(String registroAcademico) {
        return new Aluno(registroAcademico, "Aluno " + registroAcademico, registroAcademico.toLowerCase() + "@email.com");
    }

    private static Disciplina poo() {
        return new Disciplina("POO", "Programação Orientada a Objetos", 80);
    }

    private static OfertaDisciplina ofertaDePoo(String codigoDaTurma, int ano, Semestre semestre) {
        return new Turma(codigoDaTurma, new PeriodoLetivo(ano, semestre)).ofertarDisciplina(poo());
    }

    @Test
    @DisplayName("Deve nascer ativa, sem resultado e ligada ao aluno e à oferta")
    void deveNascerAtivaESemResultado() {
        // Arrange
        var ana = aluno("RA001");
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);

        // Act
        var matricula = oferta.matricular("MAT-001", ana);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertEquals("MAT-001", matricula.getCodigo());
        assertSame(ana, matricula.getAluno());
        assertSame(oferta, matricula.getOfertaDisciplina());
        assertSame(oferta.getTurma(), matricula.getTurma());
        assertEquals("MAT-001 - RA001 - POO - ATIVA", matricula.toString());
    }

    @Test
    @DisplayName("Deve exigir código, aluno e oferta na criação da matrícula")
    void deveExigirCodigoAlunoEOferta() {
        var ana = aluno("RA001");
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);

        var semCodigo = assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("  ", ana, oferta)
        );
        var semAluno = assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", null, oferta)
        );
        var semOferta = assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", ana, (OfertaDisciplina) null)
        );

        assertEquals("O código da matrícula é obrigatório.", semCodigo.getMessage());
        assertEquals("O aluno é obrigatório.", semAluno.getMessage());
        assertEquals("A oferta da disciplina é obrigatória.", semOferta.getMessage());
    }

    @Test
    @DisplayName("Deve gerar o código em sequência quando ele não é informado")
    void deveGerarCodigoEmSequencia() {
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);

        var primeira = oferta.matricular(aluno("RA001"));
        var segunda = oferta.matricular(aluno("RA002"));

        assertEquals("MAT-1", primeira.getCodigo());
        assertEquals("MAT-2", segunda.getCodigo());
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve concluir a matrícula guardando o resultado")
    void deveConcluirGuardandoResultado() {
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        var matricula = oferta.matricular(aluno("RA001"));

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve recusar conclusão sem resultado")
    void deveRecusarConclusaoSemResultado() {
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        var matricula = oferta.matricular(aluno("RA001"));

        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertEquals("O resultado acadêmico é obrigatório.", erro.getMessage());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve trancar e cancelar apenas a matrícula ativa")
    void deveMudarSituacaoApenasQuandoAtiva() {
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        var trancada = oferta.matricular(aluno("RA001"));
        var cancelada = oferta.matricular(aluno("RA002"));

        trancada.trancar();
        cancelada.cancelar();

        assertEquals(SituacaoMatricula.TRANCADA, trancada.getSituacao());
        assertEquals(SituacaoMatricula.CANCELADA, cancelada.getSituacao());
        assertThrows(IllegalStateException.class, trancada::cancelar);
        assertThrows(IllegalStateException.class, cancelada::trancar);
        assertThrows(
                IllegalStateException.class,
                () -> trancada.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    @DisplayName("Deve recusar a conclusão de uma matrícula já concluída")
    void deveRecusarSegundaConclusao() {
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        var matricula = oferta.matricular(aluno("RA001"));
        matricula.concluir(ResultadoAcademico.REPROVADO);

        var erro = assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );

        assertEquals(
                "Não é possível concluir uma matrícula CONCLUIDA.",
                erro.getMessage()
        );
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve permitir nova matrícula na mesma disciplina depois da reprovação")
    void devePermitirNovaMatriculaAposReprovacao() {
        var paola = aluno("RA001");
        var ofertaDe2025 = ofertaDePoo("ESOFT4S-NA", 2025, Semestre.SEGUNDO);
        var ofertaDe2026 = ofertaDePoo("ESOFT4S-NB", 2026, Semestre.PRIMEIRO);
        ofertaDe2025.matricular(paola).concluir(ResultadoAcademico.REPROVADO);

        var novaMatricula = ofertaDe2026.matricular(paola);

        assertEquals(SituacaoMatricula.ATIVA, novaMatricula.getSituacao());
        assertEquals(2, paola.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve bloquear nova matrícula na disciplina em que o aluno já foi aprovado")
    void deveBloquearNovaMatriculaAposAprovacao() {
        var paola = aluno("RA001");
        var ofertaDe2026 = ofertaDePoo("ESOFT4S-NB", 2026, Semestre.PRIMEIRO);
        var ofertaDeOutraTurma = ofertaDePoo("ADSIS4S", 2026, Semestre.SEGUNDO);
        ofertaDe2026.matricular(paola).concluir(ResultadoAcademico.APROVADO);

        var erro = assertThrows(
                IllegalStateException.class,
                () -> ofertaDeOutraTurma.matricular(paola)
        );

        assertEquals("O aluno já foi aprovado nesta disciplina.", erro.getMessage());
        assertEquals(1, paola.getMatriculas().size());
        assertEquals(0, ofertaDeOutraTurma.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve permitir matrícula em outra disciplina depois da aprovação")
    void devePermitirMatriculaEmOutraDisciplina() {
        var paola = aluno("RA001");
        var turma = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var ofertaDePoo = turma.ofertarDisciplina(poo());
        var ofertaDeBancoDados = turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));
        ofertaDePoo.matricular(paola).concluir(ResultadoAcademico.APROVADO);

        var matricula = ofertaDeBancoDados.matricular(paola);

        assertEquals("BD", matricula.getOfertaDisciplina().getDisciplina().getCodigo());
        assertEquals(2, paola.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve matricular pela turma somente quando existe uma única oferta")
    void deveMatricularPelaTurmaComOfertaUnica() {
        var turmaComUmaOferta = new Turma("ESOFT4S-NA", poo(), new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turmaSemOferta = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turmaComDuasOfertas = new Turma("ADSIS4S", poo(), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmaComDuasOfertas.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));

        var matricula = new Matricula("MAT-001", aluno("RA001"), turmaComUmaOferta);

        assertEquals("POO", matricula.getOfertaDisciplina().getDisciplina().getCodigo());
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-002", aluno("RA002"), turmaSemOferta)
        );
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-003", aluno("RA003"), turmaComDuasOfertas)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-004", aluno("RA004"), (Turma) null)
        );
    }

    @Test
    @DisplayName("Deve devolver as matrículas da oferta como cópia protegida")
    void deveDevolverMatriculasDaOfertaComoCopia() {
        var oferta = ofertaDePoo("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        var matricula = oferta.matricular(aluno("RA001"));

        var matriculas = oferta.getMatriculas();

        assertThrows(
                UnsupportedOperationException.class,
                () -> matriculas.add(matricula)
        );
        assertEquals("POO - ESOFT4S-NA - 2026/1", oferta.toString());
    }
}
