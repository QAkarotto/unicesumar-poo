package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.stereotype.Component;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import br.edu.sistemaacademico.domain.OfertaDisciplina;

import java.util.HashMap;
import java.util.Map;

@Component
public class CadastroAcademico {

    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();
    private final Map<String, Turma> turmas = new HashMap<>();

    public CadastroAcademico() {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");

        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        turmas.put(turma2025.getCodigo(), turma2025);
        turmas.put(turma2026A.getCodigo(), turma2026A);
        turmas.put(turma2026B.getCodigo(), turma2026B);

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        var mat1 = poo2025.matricular("MAT-001", paola);
        mat1.concluir(ResultadoAcademico.REPROVADO);

        var mat2 = poo2026A.matricular("MAT-002", paola);
        mat2.concluir(ResultadoAcademico.APROVADO);

        var mat3 = poo2026A.matricular("MAT-003", bruno);

        matriculas.put(mat1.getCodigo(), mat1);
        matriculas.put(mat2.getCodigo(), mat2);
        matriculas.put(mat3.getCodigo(), mat3);
    }

    public Aluno buscarAluno(String registroAcademico) {
        return alunos.get(registroAcademico);
    }
    public Matricula buscarMatricula(String codigo) {
        return matriculas.get(codigo);
    }
    public OfertaDisciplina buscarOferta(String codigoTurma, String codigoDisciplina) {
        Turma turma = turmas.get(codigoTurma);
        if (turma == null) {
            return null;
        }
        for (OfertaDisciplina oferta : turma.getOfertas()) {
            if (oferta.getDisciplina().getCodigo().equals(codigoDisciplina)) {
                return oferta;
            }
        }
        return null;
    }

    public Matricula matricular(Aluno aluno, OfertaDisciplina oferta) {
        String codigo = String.format("MAT-%03d", matriculas.size() + 1);
        Matricula matricula = oferta.matricular(codigo, aluno);
        matriculas.put(codigo, matricula);
        return matricula;
    }
}