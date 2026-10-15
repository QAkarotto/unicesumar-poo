package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();
    private final List<Turma> turmas = new ArrayList<>();
    private int ultimoNumero = 0;

    public DadosEmMemoria() {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        alunos.put(paola.getIdentificadorAcademico(), paola);
        alunos.put(bruno.getIdentificadorAcademico(), bruno);

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmas.addAll(List.of(turma2025, turma2026A, turma2026B));

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        // MAT-001: Paola reprovada em POO (2025/2)
        var m1 = poo2025.matricular(paola);
        m1.concluir(ResultadoAcademico.REPROVADO);
        registrar(m1);

        // MAT-002: Paola aprovada em POO (2026/1)
        var m2 = poo2026A.matricular(paola);
        m2.concluir(ResultadoAcademico.APROVADO);
        registrar(m2);

        // MAT-003: Bruno cursando POO (2026/1)
        registrar(poo2026A.matricular(bruno));
    }

    public Optional<Aluno> buscarAluno(String identificador) {
        return Optional.ofNullable(alunos.get(identificador));
    }

    public Optional<Matricula> buscarMatricula(String id) {
        return Optional.ofNullable(matriculas.get(id));
    }

    public Optional<OfertaDisciplina> buscarOferta(String codigoTurma, String codigoDisciplina) {
        for (Turma turma : turmas) {
            if (turma.getCodigo().equals(codigoTurma)) {
                for (OfertaDisciplina oferta : turma.getOfertas()) {
                    if (oferta.getDisciplina().getCodigo().equals(codigoDisciplina)) {
                        return Optional.of(oferta);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public synchronized String registrar(Matricula matricula) {
        String id = String.format("MAT-%03d", ++ultimoNumero);
        matriculas.put(id, matricula);
        return id;
    }
}