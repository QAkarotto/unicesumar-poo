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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Guarda os objetos do domínio apenas em memória (sem banco de dados).
 *
 * É um @Component: o Spring cria UMA instância e entrega a mesma para todos os
 * Controllers que a pedirem no construtor (injeção de dependência). Assim os dados
 * criados por um POST continuam visíveis nos GET seguintes.
 *
 * Esta classe NÃO contém regras de matrícula: elas ficam em Matricula, Aluno e
 * OfertaDisciplina. Aqui só existem busca e armazenamento.
 */
@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos = new ConcurrentHashMap<>();
    private final Map<String, Turma> turmas = new ConcurrentHashMap<>();
    private final Map<String, Matricula> matriculas = new ConcurrentHashMap<>();

    public DadosEmMemoria() {
        carregarDadosIniciais();
    }

    public Aluno buscarAluno(String registroAcademico) {
        return alunos.get(chave(registroAcademico));
    }

    public Turma buscarTurma(String codigoTurma) {
        return turmas.get(chave(codigoTurma));
    }

    public Matricula buscarMatricula(String codigo) {
        return matriculas.get(chave(codigo));
    }

    /** Procura, dentro da turma, a oferta da disciplina informada (ou null). */
    public OfertaDisciplina buscarOferta(Turma turma, String codigoDisciplina) {
        return turma.getOfertas().stream()
                .filter(oferta -> oferta.getDisciplina().getCodigo()
                        .equalsIgnoreCase(codigoDisciplina.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Cria a matrícula usando o domínio. Se uma regra for violada, o próprio domínio
     * lança IllegalArgumentException/IllegalStateException e nada é guardado aqui.
     *
     * O código é gerado aqui (e não por oferta.matricular(aluno)) porque aquele
     * método numera por oferta, o que repetiria "MAT-1" em disciplinas diferentes.
     */
    public synchronized Matricula matricular(Aluno aluno, OfertaDisciplina oferta) {
        var codigo = "MAT-%03d".formatted(matriculas.size() + 1);
        var matricula = oferta.matricular(codigo, aluno);
        matriculas.put(chave(codigo), matricula);
        return matricula;
    }

    private void carregarDadosIniciais() {
        var paola = cadastrar(new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com"));
        var bruno = cadastrar(new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com"));
        cadastrar(new Aluno("RA2026003", "Carla Menezes", "carla.menezes@email.com"));

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = cadastrar(new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO)));
        var turma2026A = cadastrar(new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO)));
        var turma2026B = cadastrar(new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO)));

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        matricular(paola, poo2025).concluir(ResultadoAcademico.REPROVADO);  // MAT-001
        matricular(paola, poo2026A).concluir(ResultadoAcademico.APROVADO);  // MAT-002
        matricular(bruno, poo2026A);                                        // MAT-003
    }

    private Aluno cadastrar(Aluno aluno) {
        alunos.put(chave(aluno.getRegistroAcademico()), aluno);
        return aluno;
    }

    private Turma cadastrar(Turma turma) {
        turmas.put(chave(turma.getCodigo()), turma);
        return turma;
    }

    /** Ignora maiúsculas/minúsculas e espaços nas buscas. */
    private static String chave(String texto) {
        return texto.trim().toUpperCase();
    }
}
