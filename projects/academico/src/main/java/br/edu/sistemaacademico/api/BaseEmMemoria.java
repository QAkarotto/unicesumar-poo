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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BaseEmMemoria {

    private final Map<String, Aluno> alunos = new ConcurrentHashMap<>();
    private final Map<String, Turma> turmas = new ConcurrentHashMap<>();
    private final Map<String, Matricula> matriculas = new ConcurrentHashMap<>();
    private int sequenciaMatricula = 0;

    public BaseEmMemoria() {
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

        matricular(paola, poo2025).concluir(ResultadoAcademico.REPROVADO);  // MAT-001
        matricular(paola, poo2026A).concluir(ResultadoAcademico.APROVADO);  // MAT-002
        matricular(bruno, poo2026A);                                        // MAT-003
    }

    /** A regra de matrícula continua no domínio; aqui só geramos o código e guardamos. */
    public synchronized Matricula matricular(Aluno aluno, OfertaDisciplina oferta) {
        var codigo = String.format("MAT-%03d", sequenciaMatricula + 1);
        var matricula = oferta.matricular(codigo, aluno); // lança exceção se violar regra
        sequenciaMatricula++;                             // só consome o número se deu certo
        matriculas.put(matricula.getCodigo(), matricula);
        return matricula;
    }

    public Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    public Optional<OfertaDisciplina> buscarOferta(String codigoTurma, String codigoDisciplina) {
        return Optional.ofNullable(turmas.get(codigoTurma))
                .flatMap(turma -> turma.getOfertas().stream()
                        .filter(o -> o.getDisciplina().getCodigo().equals(codigoDisciplina))
                        .findFirst());
    }
}