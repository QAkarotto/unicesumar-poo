
package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MemoriaRepository {

    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Matricula> matriculas = new ArrayList<>();
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public MemoriaRepository() {
        Aluno aluno1 = new Aluno(
                "2024001",
                "Matheus Machado",
                "matheus@email.com"
        );

        Aluno aluno2 = new Aluno(
                "2024002",
                "Ana Silva",
                "ana@email.com"
        );

        alunos.add(aluno1);
        alunos.add(aluno2);

        Aluno.Disciplina disciplina = new Aluno.Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma("T01", periodo);

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        ofertas.add(oferta);
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }

    public Optional<Aluno> buscarAlunoPorIdentificador(
            String identificador
    ) {
        return alunos.stream()
                .filter(aluno -> aluno.getIdentificadorAcademico()
                        .equalsIgnoreCase(identificador))
                .findFirst();
    }

    public Optional<Matricula> buscarMatriculaPorCodigo(
            String codigo
    ) {
        return matriculas.stream()
                .filter(matricula -> matricula.getCodigo()
                        .equalsIgnoreCase(codigo))
                .findFirst();
    }

    public Optional<OfertaDisciplina> buscarOfertaPorCodigo(
            String codigo
    ) {
        return ofertas.stream()
                .filter(oferta -> oferta.getDisciplina()
                        .getCodigo()
                        .equalsIgnoreCase(codigo))
                .findFirst();
    }

    public void salvarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public void salvarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }
}
