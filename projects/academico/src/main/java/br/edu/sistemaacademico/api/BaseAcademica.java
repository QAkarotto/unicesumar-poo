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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Guarda em memória os objetos do domínio para a API consultar.
 * Não tem regra de negócio: só localiza objetos e delega ao domínio.
 * Os dados iniciais são os mesmos do SistemaAcademico.main.
 */
@Component
public class BaseAcademica {

    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, Turma> turmas = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();

    public BaseAcademica() {
        carregarDadosIniciais();
    }

    public synchronized Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
    }

    public synchronized Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    public synchronized Optional<OfertaDisciplina> buscarOferta(String codigoTurma,
                                                                 String codigoDisciplina) {
        return Optional.ofNullable(turmas.get(codigoTurma))
                .flatMap(turma -> turma.getOfertas().stream()
                        .filter(oferta -> oferta.getDisciplina().getCodigo().equals(codigoDisciplina))
                        .findFirst());
    }

    /**
     * Cria a matrícula pelo domínio (que valida todas as regras) e a guarda.
     * O código precisa ser único na base, por isso a checagem acontece antes.
     */
    public synchronized Matricula matricular(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        String codigoLimpo = codigo.trim();
        if (matriculas.containsKey(codigoLimpo)) {
            throw new IllegalStateException(
                    "Já existe uma matrícula com o código " + codigoLimpo + "."
            );
        }
        Matricula matricula = oferta.matricular(codigoLimpo, aluno);
        matriculas.put(matricula.getCodigo(), matricula);
        return matricula;
    }

    private void carregarDadosIniciais() {
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

        // Histórico da Paola: reprovou em 2025/2 e foi aprovada em 2026/1.
        var reprovada = matricular("MAT2025001", paola, poo2025);
        reprovada.concluir(ResultadoAcademico.REPROVADO);
        var aprovada = matricular("MAT2026001", paola, poo2026A);
        aprovada.concluir(ResultadoAcademico.APROVADO);

        matricular("MAT2026003", bruno, poo2026A);
    }
}
