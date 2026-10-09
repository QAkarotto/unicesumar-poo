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

import java.util.HashMap;
import java.util.Map;

@Component
public class AcademicoMemoria {

    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new HashMap<>();

    public AcademicoMemoria() {
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

        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);

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

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        var bancoDados2026A = turma2026A.ofertarDisciplina(bancoDados);
        var poo2026B = turma2026B.ofertarDisciplina(poo);

        ofertas.put(chave("ESOFT4S-NA", "POO"), poo2025);
        ofertas.put(chave("ESOFT4S-NB", "POO"), poo2026A);
        ofertas.put(chave("ESOFT4S-NB", "BD"), bancoDados2026A);
        ofertas.put(chave("ADSIS4S", "POO"), poo2026B);

        registrar(
                "MAT-001",
                poo2025,
                paola,
                ResultadoAcademico.REPROVADO
        );

        registrar(
                "MAT-002",
                poo2026A,
                paola,
                ResultadoAcademico.APROVADO
        );

        registrar(
                "MAT-003",
                poo2026A,
                bruno,
                null
        );
    }

    public Aluno buscarAluno(String registroAcademico) {
        return alunos.get(registroAcademico);
    }

    public Matricula buscarMatricula(String codigo) {
        return matriculas.get(codigo);
    }

    public OfertaDisciplina buscarOferta(String turma, String disciplina) {
        return ofertas.get(chave(turma, disciplina));
    }

    public void registrarMatricula(
            Matricula matricula,
            String disciplina
    ) {
        matriculas.put(matricula.getCodigo(), matricula);
    }

    private void registrar(
            String codigo,
            OfertaDisciplina oferta,
            Aluno aluno,
            ResultadoAcademico resultado
    ) {
        var matricula = oferta.matricular(codigo, aluno);

        if (resultado != null) {
            matricula.concluir(resultado);
        }

        registrarMatricula(matricula, oferta.getDisciplina().getCodigo());
    }

    private static String chave(String turma, String disciplina) {
        return turma + "|" + disciplina;
    }
}
