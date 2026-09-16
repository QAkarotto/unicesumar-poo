import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlunoTest {

    @Test
    @DisplayName("Deve criar aluno e inicializar lista de matrículas vazia")
    void deveCriarAlunoCorretamente() {
        var aluno = new Aluno("ALUNO1", "Ana Souza", "ana@email.com");

        assertEquals("ALUNO1", aluno.getIdentificadorAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertEquals(0, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve adicionar matrícula ao aluno com sucesso")
    void deveAdicionarMatricula() {
        var aluno = new Aluno("ALUNO1", "Ana Souza", "ana@email.com");
        var disciplina = new Disciplina("POO001", "POO", 80);
        var turma = new Turma("ESOFT4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = new Matricula("MAT1", aluno, turma);

        aluno.adicionarMatricula(matricula);

        assertEquals(1, aluno.getMatriculas().size());
        assertEquals("MAT1", aluno.getMatriculas().get(0).getCodigo());
    }

    @Test
    @DisplayName("Deve impedir adicionar matrícula nula ou duplicada")
    void deveValidarAdicaoDeMatricula() {
        var aluno = new Aluno("ALUNO1", "Ana Souza", "ana@email.com");
        var disciplina = new Disciplina("POO001", "POO", 80);
        var turma = new Turma("ESOFT4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = new Matricula("MAT1", aluno, turma);

        aluno.adicionarMatricula(matricula);

        assertThrows(IllegalArgumentException.class, () -> aluno.adicionarMatricula(null));
        assertThrows(IllegalArgumentException.class, () -> aluno.adicionarMatricula(matricula));
        assertEquals(1, aluno.getMatriculas().size());
    }
}
    @Test
    @DisplayName("Deve adicionar novas disciplinas à turma com sucesso")
    void deveAdicionarDisciplinaComSucesso() {
        var disciplinaInicial = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        var disciplinaAdicional = new Disciplina("BD001", "Banco de Dados", 60);
        var turma = new Turma("ESOFT4S-NA", disciplinaInicial, new PeriodoLetivo(2026, Semestre.SEGUNDO));

        turma.adicionarDisciplina(disciplinaAdicional);

        assertEquals(2, turma.getDisciplinas().size());
        assertEquals("BD001", turma.getDisciplinas().get(1).getCodigo());
    }

    @Test
    @DisplayName("Deve impedir adição de disciplina duplicada pelo mesmo código")
    void deveImpedirDisciplinaDuplicada() {
        var disciplinaInicial = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        var disciplinaDuplicada = new Disciplina("poo001", "Programação Orientada a Objetos II", 60);
        var turma = new Turma("ESOFT4S-NA", disciplinaInicial, new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.adicionarDisciplina(disciplinaDuplicada)
        );
        assertEquals(1, turma.getDisciplinas().size());
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios no construtor e ao adicionar disciplina")
    void deveValidarCamposObrigatorios() {
        var disciplina = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> new Turma(null, disciplina, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("", disciplina, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("ESOFT4S-NA", null, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("ESOFT4S-NA", disciplina, null));

        var turma = new Turma("ESOFT4S-NA", disciplina, periodo);
        assertThrows(IllegalArgumentException.class, () -> turma.adicionarDisciplina(null));
    }
}
