package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DadosEmMemoria {
    public final Map<String, Aluno> alunos = new HashMap<>();
    public final Map<String, Turma> turmas = new HashMap<>();
    public final Map<String, Matricula> matriculas = new LinkedHashMap<>();

    public DadosEmMemoria() {
        alunos.put("RA001", new Aluno("RA001", "Kaliria Meline", "nunes.kaliria@gmail.com"));

        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        turmas.put("ESOFT4S-NA",
                new Turma("ESOFT4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.PRIMEIRO)));
    }

    public String proximoIdMatricula() {
        return String.format("MAT-%03d", matriculas.size() + 1);
    }
}