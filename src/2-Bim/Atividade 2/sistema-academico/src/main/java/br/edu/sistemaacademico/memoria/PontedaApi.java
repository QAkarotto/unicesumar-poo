package br.edu.sistemaacademico.memoria;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PontedaApi {

    private final Map<String, Aluno> alunosPorIdentificador = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculasPorCodigo = new LinkedHashMap<>();
    private final Map<String, Turma> turmasPorCodigo = new LinkedHashMap<>();

    public PontedaApi() {
        carregarDadosIniciais();
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculasPorCodigo.get(codigo));
    }

    public Optional<Aluno> buscarAluno(String identificador) {
        return Optional.ofNullable(alunosPorIdentificador.get(identificador));
    }

    public Matricula criarMatricula(
            String codigo,
            String identificadorAluno,
            String codigoTurma,
            String codigoDisciplina) {
        exigirTexto(codigo, "Código da matrícula não pode ser vazio");
        exigirTexto(identificadorAluno, "Identificador do aluno não pode ser vazio");
        exigirTexto(codigoTurma, "Código da turma não pode ser vazio");
        exigirTexto(codigoDisciplina, "Código da disciplina não pode ser vazio");

        Aluno aluno = alunosPorIdentificador.get(identificadorAluno);
        if (aluno == null) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado");
        }

        Turma turma = turmasPorCodigo.get(codigoTurma);
        if (turma == null) {
            throw new RecursoNaoEncontradoException("Turma não encontrada");
        }

        OfertaDisciplina oferta = turma.getOfertas().stream()
                .filter(item -> item.getDisciplina().getCodigo().equals(codigoDisciplina))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não ofertada nesta turma"));

        if (matriculasPorCodigo.containsKey(codigo)) {
            throw new IllegalStateException("Já existe uma matrícula com este código");
        }

        Matricula matricula = oferta.matricular(codigo, aluno);
        matriculasPorCodigo.put(matricula.getCodigo(), matricula);
        return matricula;
    }

    private void carregarDadosIniciais() {
        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        Turma turmaAnterior = registrarTurma("ADS-2025-2", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        OfertaDisciplina ofertaAnterior = turmaAnterior.ofertarDisciplina(poo);

        Aluno pedro = registrarAluno("RA-1002", "Pedro Alves", "pedro.alves@aluno.unicesumar.edu.br");
        Matricula matriculaAnterior = ofertaAnterior.matricular("MAT-000", pedro);
        matriculaAnterior.registrarResultado(ResultadoAcademico.APROVADO);
        matriculasPorCodigo.put(matriculaAnterior.getCodigo(), matriculaAnterior);

        Turma turmaAtual = registrarTurma("ADS-2026-1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina ofertaAtual = turmaAtual.ofertarDisciplina(poo);

        Aluno marina = registrarAluno("RA-1001", "Marina Costa", "marina.costa@aluno.unicesumar.edu.br");
        Matricula matriculaAtual = ofertaAtual.matricular("MAT-001", marina);
        matriculasPorCodigo.put(matriculaAtual.getCodigo(), matriculaAtual);

        registrarAluno("RA-1003", "Julia Nunes", "julia.nunes@aluno.unicesumar.edu.br");
    }

    private Turma registrarTurma(String codigo, PeriodoLetivo periodoLetivo) {
        Turma turma = new Turma(codigo, periodoLetivo);
        turmasPorCodigo.put(turma.getCodigo(), turma);
        return turma;
    }

    private Aluno registrarAluno(String identificador, String nome, String email) {
        Aluno aluno = new Aluno(identificador, nome, email);
        alunosPorIdentificador.put(aluno.getIdentificadorAcademico(), aluno);
        return aluno;
    }

    private static void exigirTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
