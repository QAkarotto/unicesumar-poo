
package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class DadosAcademicos {

    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new LinkedHashMap<>();

    public DadosAcademicos() {
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

        Disciplina poo = new Disciplina(
                "POO", "Programação Orientada a Objetos", 80
        );

        Disciplina bd = new Disciplina(
                "BD", "Banco de Dados", 60
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026, Semestre.PRIMEIRO
        );

        Turma turma = new Turma("ADS-2026A", periodo);

        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bd);

        OfertaDisciplina ofertaPoo = turma.getOfertas().get(0);
        OfertaDisciplina ofertaBd = turma.getOfertas().get(1);

        Matricula matricula = ofertaPoo.matricular("MAT-001", paola);

        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);
        matriculas.put(matricula.getCodigo(), matricula);

        ofertas.put(chaveOferta("ADS-2026A", "POO"), ofertaPoo);
        ofertas.put(chaveOferta("ADS-2026A", "BD"), ofertaBd);
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    public Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
    }

    public Optional<OfertaDisciplina> buscarOferta(
            String codigoTurma, String codigoDisciplina) {
        return Optional.ofNullable(
                ofertas.get(chaveOferta(codigoTurma, codigoDisciplina))
        );
    }

    public boolean existeMatricula(String codigo) {
        return matriculas.containsKey(codigo);
    }

    public void adicionarMatricula(Matricula matricula) {
        matriculas.put(matricula.getCodigo(), matricula);
    }

    private String chaveOferta(String turma, String disciplina) {
        return turma + "|" + disciplina;
    }
}
