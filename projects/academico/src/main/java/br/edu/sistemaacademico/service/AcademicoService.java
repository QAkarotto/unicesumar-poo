
package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AcademicoService {

    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Turma> turmas = new ArrayList<>();

    public AcademicoService() {
        Aluno paola = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );

        Aluno bruno = new Aluno(
                "RA2026002",
                "Bruno Santos",
                "bruno.santos@email.com"
        );

        alunos.add(paola);
        alunos.add(bruno);

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("ESOFT4S-NA", periodo);
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDados);

        turmas.add(turma);
    }

    public Optional<Aluno> buscarAluno(String identificador) {
        return alunos.stream()
                .filter(aluno -> aluno.getRegistroAcademico()
                        .equalsIgnoreCase(identificador))
                .findFirst();
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return turmas.stream()
                .flatMap(turma -> turma.getOfertas().stream())
                .flatMap(oferta -> oferta.getMatriculas().stream())
                .filter(matricula -> matricula.getCodigo()
                        .equalsIgnoreCase(codigo))
                .findFirst();
    }

    public Matricula matricular(String identificadorAluno, String codigoTurma,
                                String codigoDisciplina) {
        Aluno aluno = buscarAluno(identificadorAluno)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aluno não encontrado: " + identificadorAluno));

        Turma turma = turmas.stream()
                .filter(t -> t.getCodigo().equalsIgnoreCase(codigoTurma))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Turma não encontrada: " + codigoTurma));

        OfertaDisciplina oferta = turma.getOfertas().stream()
                .filter(o -> o.getDisciplina().getCodigo()
                        .equalsIgnoreCase(codigoDisciplina))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não ofertada nesta turma: " + codigoDisciplina));

        return oferta.matricular(aluno);
    }
}
