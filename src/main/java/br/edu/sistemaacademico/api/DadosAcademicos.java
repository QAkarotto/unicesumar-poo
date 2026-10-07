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

@Component
public class DadosAcademicos {

    private final List<Aluno> alunos = new ArrayList<>();
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public DadosAcademicos() {
        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
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

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
        var bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        var turma2025 = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2025, Semestre.SEGUNDO)
        );
        var turma2026A = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );
        var turma2026B = new Turma(
                "ADSIS4S",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        var poo2025 = registrarOferta(turma2025.ofertarDisciplina(poo));
        var poo2026A = registrarOferta(turma2026A.ofertarDisciplina(poo));
        registrarOferta(turma2026A.ofertarDisciplina(bancoDados));
        registrarOferta(turma2026B.ofertarDisciplina(poo));

        alunos.add(paola);
        alunos.add(bruno);

        var primeiraMatricula = poo2025.matricular(paola);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        var segundaMatricula = poo2026A.matricular(paola);
        segundaMatricula.concluir(ResultadoAcademico.APROVADO);

        poo2026A.matricular(bruno);
    }

    private OfertaDisciplina registrarOferta(OfertaDisciplina oferta) {
        ofertas.add(oferta);
        return oferta;
    }

    public Aluno buscarAluno(String registroAcademico) {
        return alunos.stream()
                .filter(aluno -> aluno.getRegistroAcademico().equalsIgnoreCase(registroAcademico))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aluno não encontrado: " + registroAcademico
                ));
    }

    public Matricula buscarMatricula(String codigo) {
        return ofertas.stream()
                .flatMap(oferta -> oferta.getMatriculas().stream())
                .filter(matricula -> matricula.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Matrícula não encontrada: " + codigo
                ));
    }

    public OfertaDisciplina buscarOferta(String turmaCodigo, String disciplinaCodigo) {
        return ofertas.stream()
                .filter(oferta -> oferta.getTurma().getCodigo().equalsIgnoreCase(turmaCodigo))
                .filter(oferta -> oferta.getDisciplina().getCodigo().equalsIgnoreCase(disciplinaCodigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Oferta não encontrada para a turma " + turmaCodigo
                                + " e disciplina " + disciplinaCodigo + "."
                ));
    }
}