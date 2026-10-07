package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

 // Guarda em memória os alunos e as turmas usados pela API.
 // Os dois controllers usam esta mesma instância
 // Os métodos são synchronized para que duas requisições ao mesmo tempo não mexam nas listas ao mesmo tempo.
@Component
public class DadosAcademicos {

    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Turma> turmas = new ArrayList<>();

    public DadosAcademicos() {
        criarAlunos();
        criarTurmas();
        criarMatriculasIniciais();
    }

    // dados iniciais

    private void criarAlunos() {
        alunos.add(new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com"));
        alunos.add(new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com"));
        alunos.add(new Aluno("RA2026003", "Carla Mendes", "carla.mendes@email.com"));
    }

    private void criarTurmas() {
        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        Disciplina bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        Turma turmaNB = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        turmaNB.ofertarDisciplina(poo);
        turmaNB.ofertarDisciplina(bancoDados);

        Turma turmaAdsis = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmaAdsis.ofertarDisciplina(poo);

        turmas.add(turmaNB);
        turmas.add(turmaAdsis);
    }

    private void criarMatriculasIniciais() {
        // Gera MAT-001 e MAT-002
        matricular(buscarAluno("RA2026001"), buscarOferta("ESOFT4S-NB", "POO"));
        matricular(buscarAluno("RA2026002"), buscarOferta("ESOFT4S-NB", "POO"));
    }

    //consultas

    public synchronized Aluno buscarAluno(String registroAcademico) {
        for (Aluno aluno : alunos) {
            if (aluno.getRegistroAcademico().equals(registroAcademico)) {
                return aluno;
            }
        }
        return null;
    }

    public synchronized Matricula buscarMatricula(String codigo) {
        for (Aluno aluno : alunos) {
            for (Matricula matricula : aluno.getMatriculas()) {
                if (matricula.getCodigo().equals(codigo)) {
                    return matricula;
                }
            }
        }
        return null;
    }

    public synchronized OfertaDisciplina buscarOferta(String codigoTurma, String codigoDisciplina) {
        for (Turma turma : turmas) {
            if (!turma.getCodigo().equals(codigoTurma)) {
                continue;
            }
            for (OfertaDisciplina oferta : turma.getOfertas()) {
                if (oferta.getDisciplina().getCodigo().equals(codigoDisciplina)) {
                    return oferta;
                }
            }
        }
        return null;
    }

    //criação


     //Só escolhe o código da nova matrícula e manda para o domínio, que valida as regras e lança exceção.
    public synchronized Matricula matricular(Aluno aluno, OfertaDisciplina oferta) {
        String codigo = gerarCodigoMatricula();
        return oferta.matricular(codigo, aluno);
    }

    private String gerarCodigoMatricula() {
        int total = 0;
        for (Aluno aluno : alunos) {
            total = total + aluno.getMatriculas().size();
        }
        return String.format("MAT-%03d", total + 1);
    }
}
