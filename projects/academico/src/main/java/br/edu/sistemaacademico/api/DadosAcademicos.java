package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// Guarda os dados apenas em memória (sem banco), como pede a atividade.
// Os dois controllers usam esta mesma classe, por isso ela é um @Component.
// As regras de matrícula continuam no domínio: aqui só criamos e procuramos objetos.
@Component
public class DadosAcademicos {

    private final List<Aluno> alunos;
    private final List<OfertaDisciplina> ofertas;

    public DadosAcademicos() {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var ofertaPoo = turma.ofertarDisciplina(poo);
        var ofertaBd = turma.ofertarDisciplina(bancoDados);

        // Uma matrícula já existente, para poder consultar MAT-001
        ofertaPoo.matricular("MAT-001", bruno);

        this.alunos = List.of(paola, bruno);
        this.ofertas = List.of(ofertaPoo, ofertaBd);
    }

    public Optional<Aluno> buscarAluno(String ra) {
        return alunos.stream()
                .filter(aluno -> aluno.getRegistroAcademico().equals(ra))
                .findFirst();
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return ofertas.stream()
                .flatMap(oferta -> oferta.getMatriculas().stream())
                .filter(matricula -> matricula.getCodigo().equals(codigo))
                .findFirst();
    }

    public Optional<OfertaDisciplina> buscarOferta(String codigoTurma, String codigoDisciplina) {
        return ofertas.stream()
                .filter(oferta -> oferta.getTurma().getCodigo().equals(codigoTurma))
                .filter(oferta -> oferta.getDisciplina().getCodigo().equals(codigoDisciplina))
                .findFirst();
    }

    public String proximoCodigo() {
        long total = ofertas.stream()
                .mapToLong(oferta -> oferta.getMatriculas().size())
                .sum();
        return String.format("MAT-%03d", total + 1);
    }
}
