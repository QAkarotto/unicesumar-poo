package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaDisciplinaTest {

    private Disciplina poo() {
        return new Disciplina("POO", "Programação Orientada a Objetos", 80);
    }

    private OfertaDisciplina oferta(String codigoTurma, int ano, Semestre semestre) {
        var turma = new Turma(codigoTurma, new PeriodoLetivo(ano, semestre));
        return turma.ofertarDisciplina(poo());
    }

    private Aluno aluno(String registro, String nome) {
        return new Aluno(registro, nome, registro.toLowerCase() + "@email.com");
    }

    @Test
    @DisplayName("Deve registrar a mesma matrícula no aluno e na oferta")
    void deveRegistrarMatriculaNoAlunoENaOferta() {
        // Arrange
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        // Act
        var matricula = oferta.matricular("MAT-001", aluno);

        // Assert
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertSame(matricula, oferta.getMatriculas().get(0));
        assertSame(matricula, aluno.getMatriculas().get(0));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir matrícula duplicada na mesma oferta")
    void deveImpedirMatriculaDuplicada() {
        var aluno = new Aluno("RA002", "Alexandre Gaia", "alexandre@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
        oferta.matricular("MATRICULA-001", aluno);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MATRICULA-002", aluno)
        );
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve gerar o código quando a matrícula é feita sem informá-lo")
    void deveGerarCodigoQuandoNaoInformado() {
        var oferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);

        var matricula = oferta.matricular(aluno("RA010", "Ana Souza"));

        assertNotNull(matricula.getCodigo());
        assertFalse(matricula.getCodigo().isBlank());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertEquals(1, oferta.getTotalMatriculados());
    }

    @Test
    @DisplayName("Deve gerar códigos distintos para matrículas da mesma oferta")
    void deveGerarCodigosDistintos() {
        var oferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);

        var primeira = oferta.matricular(aluno("RA011", "Ana Souza"));
        var segunda = oferta.matricular(aluno("RA012", "Bruno Santos"));

        assertNotEquals(primeira.getCodigo(), segunda.getCodigo());
        assertEquals(2, oferta.getTotalMatriculados());
    }

    @Test
    @DisplayName("Deve impedir duas matrículas com o mesmo código na oferta")
    void deveImpedirCodigoDuplicado() {
        var oferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        oferta.matricular("MAT-001", aluno("RA013", "Ana Souza"));

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MAT-001", aluno("RA014", "Bruno Santos"))
        );
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve exigir código e aluno para matricular")
    void deveExigirCodigoEAluno() {
        var oferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("   ", aluno("RA015", "Ana Souza"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MAT-001", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.possuiMatriculaDe(null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.possuiMatriculaComCodigo(null)
        );
    }

    @Test
    @DisplayName("Deve permitir nova matrícula após reprovação em outro período")
    void devePermitirNovaMatriculaAposReprovacao() {
        var aluno = aluno("RA016", "Ana Souza");
        oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO)
                .matricular(aluno)
                .concluir(ResultadoAcademico.REPROVADO);

        var novaMatricula = oferta("ESOFT5S-NA", 2027, Semestre.PRIMEIRO).matricular(aluno);

        assertEquals(SituacaoMatricula.ATIVA, novaMatricula.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve impedir nova matrícula após aprovação, mesmo em outra turma")
    void deveImpedirNovaMatriculaAposAprovacao() {
        var aluno = aluno("RA017", "Ana Souza");
        oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO)
                .matricular(aluno)
                .concluir(ResultadoAcademico.APROVADO);
        var outraTurma = oferta("ADSIS4S", 2027, Semestre.PRIMEIRO);

        assertThrows(
                IllegalStateException.class,
                () -> outraTurma.matricular(aluno)
        );
        assertEquals(0, outraTurma.getTotalMatriculados());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve localizar a matrícula de um aluno na oferta")
    void deveLocalizarMatriculaDoAluno() {
        var oferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var ana = aluno("RA018", "Ana Souza");
        var matricula = oferta.matricular("MAT-001", ana);

        assertTrue(oferta.possuiMatriculaDe(ana));
        assertTrue(oferta.possuiMatriculaComCodigo("mat-001"));
        assertSame(matricula, oferta.buscarMatricula(ana));
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.buscarMatricula(aluno("RA019", "Bruno Santos"))
        );
    }

    @Test
    @DisplayName("Deve impedir alteração da lista de matrículas devolvida")
    void deveImpedirAlteracaoDasMatriculas() {
        var oferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var matricula = oferta.matricular(aluno("RA020", "Ana Souza"));

        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().remove(matricula)
        );
    }

    @Test
    @DisplayName("Deve comparar ofertas por turma e disciplina")
    void deveCompararOfertasPorTurmaEDisciplina() {
        var oferta2026 = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var mesmaOferta = oferta("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var outroPeriodo = oferta("ESOFT4S-NA", 2027, Semestre.SEGUNDO);
        var outraDisciplina = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO))
                .ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));

        assertTrue(oferta2026.equals(mesmaOferta));
        assertEquals(oferta2026.hashCode(), mesmaOferta.hashCode());
        assertFalse(oferta2026.equals(outroPeriodo));
        assertFalse(oferta2026.equals(outraDisciplina));
        assertFalse(oferta2026.equals("POO"));
        assertEquals("Programação Orientada a Objetos (ESOFT4S-NA - 2026/2)", oferta2026.toString());
    }
}