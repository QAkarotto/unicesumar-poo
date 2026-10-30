package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Armazenamento em memória das turmas, ofertas e matrículas (ainda não persistidas).
 * Aluno e Disciplina vêm do banco, por meio dos Repositories. Nenhuma regra de matrícula
 * fica aqui: ela continua no domínio. É um bean singleton injetado nos Controllers.
 *
 * O cenário inicial repete o de SistemaAcademico.main; os alunos e disciplinas usados nele
 * são carregados do banco (cadastrados por database/dados-iniciais.sql).
 */
@Component
public class DadosAcademicos {

    private final AlunoRepository alunoRepository;
    // Alunos (carregados do banco) que participam de matrículas. O objeto Aluno guarda o histórico
    // de matrículas em memória, e as regras do domínio dependem dele; por isso a mesma instância é reutilizada.
    private final Map<String, Aluno> alunosComMatriculas = new HashMap<>();
    private final Map<String, Turma> turmas = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();
    private int sequencia = 0;

    public DadosAcademicos(AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository) {
        this.alunoRepository = alunoRepository;

        var paola = carregarAlunoInicial("RA2026001");
        var bruno = carregarAlunoInicial("RA2026002");

        var poo = disciplinaRepository.buscarPorCodigo("POO")
                .orElseThrow(() -> dadosIniciaisAusentes("disciplina POO"));
        var bd = disciplinaRepository.buscarPorCodigo("BD")
                .orElseThrow(() -> dadosIniciaisAusentes("disciplina BD"));

        var turma2025 = guardar(new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO)));
        var turma2026A = guardar(new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO)));
        var turma2026B = guardar(new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO)));

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bd);
        turma2026B.ofertarDisciplina(poo);

        // MAT-001: Paola reprovada em POO; MAT-002: Paola aprovada em POO; MAT-003: Bruno cursando POO
        registrar(poo2025.matricular(proximoCodigo(), paola)).concluir(ResultadoAcademico.REPROVADO);
        registrar(poo2026A.matricular(proximoCodigo(), paola)).concluir(ResultadoAcademico.APROVADO);
        registrar(poo2026A.matricular(proximoCodigo(), bruno));
    }

    /** Matrículas (em memória) do aluno; vazio se ele ainda não tem nenhuma. */
    public synchronized List<Matricula> matriculasDoAluno(String registroAcademico) {
        var aluno = alunosComMatriculas.get(registroAcademico);
        return aluno == null ? List.of() : aluno.getMatriculas();
    }

    public synchronized Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    /**
     * Localiza aluno e oferta e delega a criação ao domínio (OfertaDisciplina.matricular),
     * que aplica as regras e lança exceção se a matrícula for proibida.
     */
    public synchronized Matricula matricular(String registroAcademico, String codigoTurma, String codigoDisciplina) {
        var aluno = alunosComMatriculas.get(registroAcademico);
        if (aluno == null) {
            aluno = alunoRepository.buscarPorRa(registroAcademico)
                    .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
            alunosComMatriculas.put(registroAcademico, aluno);
        }
        var turma = Optional.ofNullable(turmas.get(codigoTurma))
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Turma não encontrada: " + codigoTurma));
        OfertaDisciplina oferta = turma.getOfertas().stream()
                .filter(o -> o.getDisciplina().getCodigo().equalsIgnoreCase(codigoDisciplina))
                .findFirst()
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND,
                        "A disciplina " + codigoDisciplina + " não é ofertada na turma " + codigoTurma + "."));

        return registrar(oferta.matricular(proximoCodigo(), aluno));
    }

    // O código do domínio (MAT-1, MAT-2...) reinicia a cada oferta; a API precisa de um id único global.
    private String proximoCodigo() {
        return "MAT-%03d".formatted(++sequencia);
    }

    private Aluno carregarAlunoInicial(String registroAcademico) {
        var aluno = alunoRepository.buscarPorRa(registroAcademico)
                .orElseThrow(() -> dadosIniciaisAusentes("aluno " + registroAcademico));
        alunosComMatriculas.put(registroAcademico, aluno);
        return aluno;
    }

    private static IllegalStateException dadosIniciaisAusentes(String descricao) {
        return new IllegalStateException("Dado inicial não encontrado no banco: " + descricao
                + ". Execute database/schema.sql e database/dados-iniciais.sql.");
    }

    private Turma guardar(Turma t) {
        turmas.put(t.getCodigo(), t);
        return t;
    }

    private Matricula registrar(Matricula m) {
        matriculas.put(m.getCodigo(), m);
        return m;
    }
}
