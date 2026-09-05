package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    @Test
    void deveRealizarMatriculaComSucesso() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();
        Matricula matricula = oferta.matricular(aluno);
        assertNotNull(matricula);
        assertEquals("MAT-1", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void deveGerarCodigoAutomaticoParaMatricula() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = oferta.matricular(aluno);

        assertEquals("MAT-1", matricula.getCodigo());
    }

    @Test
    void deveRejeitarMatriculaComCodigoNulo() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(null, aluno, oferta)
        );
    }

    @Test
    void deveRejeitarMatriculaComCodigoVazio() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("   ", aluno, oferta)
        );
    }

    @Test
    void deveRejeitarAlunoNulo() {
        OfertaDisciplina oferta = criarOferta();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", null, oferta)
        );
    }

    @Test
    void deveRejeitarOfertaNula() {
        Aluno aluno = criarAluno();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", aluno, (OfertaDisciplina) null)
        );
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        oferta.matricular(aluno);
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(aluno)
        );

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void deveConcluirMatriculaComResultadoAprovado() {
        Matricula matricula = criarMatricula();
        matricula.concluir(ResultadoAcademico.APROVADO);
        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                matricula.getSituacao()
        );
        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void deveConcluirMatriculaComResultadoReprovado() {
        Matricula matricula = criarMatricula();

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                matricula.getSituacao()
        );
        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void naoDeveConcluirMatriculaSemResultado() {
        Matricula matricula = criarMatricula();

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertEquals(
                SituacaoMatricula.ATIVA,
                matricula.getSituacao()
        );
    }

    @Test
    void deveTrancarMatriculaAtiva() {
        Matricula matricula = criarMatricula();

        matricula.trancar();

        assertEquals(
                SituacaoMatricula.TRANCADA,
                matricula.getSituacao()
        );
    }

    @Test
    void deveCancelarMatriculaAtiva() {
        Matricula matricula = criarMatricula();

        matricula.cancelar();

        assertEquals(
                SituacaoMatricula.CANCELADA,
                matricula.getSituacao()
        );
    }

    @Test
    void naoDeveTrancarMatriculaConcluida() {
        Matricula matricula = criarMatricula();
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    void naoDeveCancelarMatriculaConcluida() {
        Matricula matricula = criarMatricula();
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    void naoDeveConcluirMatriculaTrancada() {
        Matricula matricula = criarMatricula();
        matricula.trancar();

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    void naoDeveCancelarMatriculaTrancada() {
        Matricula matricula = criarMatricula();
        matricula.trancar();

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    void naoDeveConcluirMatriculaCancelada() {
        Matricula matricula = criarMatricula();
        matricula.cancelar();

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    void naoDeveTrancarMatriculaCancelada() {
        Matricula matricula = criarMatricula();
        matricula.cancelar();

        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    void naoDevePermitirNovaMatriculaNaDisciplinaAposAprovacao() {
        Aluno aluno = criarAluno();
        OfertaDisciplina primeiraOferta = criarOferta();
        Matricula primeiraMatricula = primeiraOferta.matricular(aluno);

        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        OfertaDisciplina segundaOferta = criarOutraOfertaDaMesmaDisciplina();
        assertThrows(
                IllegalStateException.class,
                () -> segundaOferta.matricular(aluno)
        );
    }

    @Test
    void devePermitirNovaMatriculaNaDisciplinaAposReprovacao() {
        Aluno aluno = criarAluno();
        OfertaDisciplina primeiraOferta = criarOferta();
        Matricula primeiraMatricula = primeiraOferta.matricular(aluno);

        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        OfertaDisciplina segundaOferta = criarOutraOfertaDaMesmaDisciplina();

        Matricula novaMatricula = segundaOferta.matricular(aluno);
        assertNotNull(novaMatricula);
        assertEquals(
                SituacaoMatricula.ATIVA,
                novaMatricula.getSituacao()
        );
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void devePermitirMatriculaEmOutraDisciplinaAposAprovacao() {
        Aluno aluno = criarAluno();

        OfertaDisciplina primeiraOferta = criarOferta();
        Matricula primeira = primeiraOferta.matricular(aluno);
        primeira.concluir(ResultadoAcademico.APROVADO);

        Disciplina outraDisciplina = new Disciplina(
                "BD",
                "Banco de Dados",
                60
        );

        Turma turma = new Turma(
                "TURMA-02",
                outraDisciplina,
                criarPeriodo()
        );

        Matricula segunda = turma.getOfertas()
                .getFirst()
                .matricular(aluno);

        assertNotNull(segunda);
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void deveObterTurmaDaMatricula() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = oferta.matricular(aluno);

        assertEquals(oferta.getTurma(), matricula.getTurma());
    }

    @Test
    void deveCriarMatriculaUtilizandoTurma() {
        Aluno aluno = criarAluno();

        Disciplina disciplina = criarDisciplina();

        Turma turma = new Turma(
                "TURMA-01",
                disciplina,
                criarPeriodo()
        );

        Matricula matricula = new Matricula(
                "MAT-1",
                aluno,
                turma
        );

        assertEquals(turma, matricula.getTurma());
        assertEquals(
                disciplina,
                matricula.getOfertaDisciplina().getDisciplina()
        );
    }

    @Test
    void deveRejeitarTurmaNulaNaMatricula() {
        Aluno aluno = criarAluno();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", aluno, (Turma) null)
        );
    }

    @Test
    void deveRejeitarMatriculaPorTurmaComMaisDeUmaOferta() {
        Aluno aluno = criarAluno();

        Turma turma = criarTurma();
        turma.ofertarDisciplina(
                new Disciplina("BD", "Banco de Dados", 60)
        );

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-1", aluno, turma)
        );
    }

    private Matricula criarMatricula() {
        return criarOferta().matricular(criarAluno());
    }

    private Aluno criarAluno() {
        return new Aluno(
                "2025001",
                "João da Silva",
                "joao@email.com"
        );
    }

    private Disciplina criarDisciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                60
        );
    }

    private OfertaDisciplina criarOferta() {
        Turma turma = criarTurma();
        return turma.getOfertas().getFirst();
    }

    private OfertaDisciplina criarOutraOfertaDaMesmaDisciplina() {
        Turma turma = new Turma(
                "TURMA-02",
                criarDisciplina(),
                criarPeriodo()
        );

        return turma.getOfertas().getFirst();
    }

    private Turma criarTurma() {
        return new Turma(
                "TURMA-01",
                criarDisciplina(),
                criarPeriodo()
        );
    }

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );
    }
}