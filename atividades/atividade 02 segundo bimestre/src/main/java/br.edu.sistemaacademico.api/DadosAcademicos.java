package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Armazenamento em memória (sem banco). Apenas guarda e localiza objetos do domínio;
 * nenhuma regra de matrícula fica aqui. É um bean singleton: o Spring cria uma instância
 * e a injeta nos Controllers.
 *
 * Os dados iniciais repetem o cenário do SistemaAcademico.main.
 */
@Component
public class DadosAcademicos {

    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Turma> turmas = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();
    private int sequencia = 0;

    public DadosAcademicos() {
        var paola = guardar(new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com"));
        var bruno = guardar(new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com"));

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD", "Banco de Dados", 80);

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

    public synchronized Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
    }

    public synchronized Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    /**
     * Localiza aluno e oferta e delega a criação ao domínio (OfertaDisciplina.matricular),
     * que aplica as regras e lança exceção se a matrícula for proibida.
     */
    public synchronized Matricula matricular(String registroAcademico, String codigoTurma, String codigoDisciplina) {
        var aluno = buscarAluno(registroAcademico)
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
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

    private Aluno guardar(Aluno a) {
        alunos.put(a.getRegistroAcademico(), a);
        return a;
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
