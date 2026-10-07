package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class DadosEmMemoria {
    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Turma> turmas = new ArrayList<>();

    public DadosEmMemoria() {
        var paola = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );
        var bruno = new Aluno(
                "RA2026002",
                "Bruno Santos",
                "bruno.santos@email.com"
        );
        alunos.add(paola);
        alunos.add(bruno);

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmas.add(turma2025);
        turmas.add(turma2026A);
        turmas.add(turma2026B);

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        poo2025.matricular(proximoCodigo(), paola).concluir(ResultadoAcademico.REPROVADO);
        poo2026A.matricular(proximoCodigo(), paola).concluir(ResultadoAcademico.APROVADO);
        poo2026A.matricular(proximoCodigo(), bruno);
    }

    public Optional<Aluno> buscarAluno(String registroAcademico) {
        return alunos.stream()
                .filter(aluno -> aluno.getRegistroAcademico().equals(registroAcademico))
                .findFirst();
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return todasAsMatriculas()
                .filter(matricula -> matricula.getCodigo().equals(codigo))
                .findFirst();
    }

    public Optional<OfertaDisciplina> buscarOferta(String codigoTurma, String codigoDisciplina) {
        return turmas.stream()
                .filter(turma -> turma.getCodigo().equals(codigoTurma))
                .flatMap(turma -> turma.getOfertas().stream())
                .filter(oferta -> oferta.getDisciplina().getCodigo().equals(codigoDisciplina))
                .findFirst();
    }

    public String proximoCodigo() {
        return "MAT-%03d".formatted(todasAsMatriculas().count() + 1);
    }

    private Stream<Matricula> todasAsMatriculas() {
        return alunos.stream().flatMap(aluno -> aluno.getMatriculas().stream());
    }
}
