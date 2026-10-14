package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class DadosAcademicos {


private final Map<String, Aluno> alunos = new LinkedHashMap<>();
private final Map<String, OfertaDisciplina> ofertas = new LinkedHashMap<>();
private final Map<String, Matricula> matriculas = new LinkedHashMap<>();

private int proximoId = 1;

public DadosAcademicos() {
    Aluno paola = new Aluno(
            "RA2026001",
            "Paola Oliveira",
            "paola.oliveira@email.com"
    );

    Aluno bruno = new Aluno(
            "RA2026002",
            "Bruno Santos",
            "bruno.santos@email.com"
    );

    alunos.put(paola.getIdentificadorAcademico(), paola);
    alunos.put(bruno.getIdentificadorAcademico(), bruno);

    Disciplina poo = new Disciplina(
            "POO",
            "ProgramaÃ§Ã£o Orientada a Objetos",
            80
    );

    Disciplina bancoDados = new Disciplina(
            "BD",
            "Banco de Dados",
            80
    );

    PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    Turma turma = new Turma("ESOFT4S-NB", periodo);

    ofertas.put(chave("ESOFT4S-NB", "POO"), turma.ofertarDisciplina(poo));
    ofertas.put(chave("ESOFT4S-NB", "BD"), turma.ofertarDisciplina(bancoDados));

    Matricula matriculaInicial = ofertas
            .get(chave("ESOFT4S-NB", "POO"))
            .matricular(bruno);

    matriculas.put("MAT-001", matriculaInicial);
    proximoId = 2;
}

public Optional<Aluno> buscarAluno(String identificador) {
    return Optional.ofNullable(alunos.get(identificador));
}

public Optional<Matricula> buscarMatricula(String id) {
    return Optional.ofNullable(matriculas.get(id));
}

public Optional<Matricula> criarMatricula(
        String identificadorAluno,
        String codigoTurma,
        String codigoDisciplina
) {
    Aluno aluno = alunos.get(identificadorAluno);

    if (aluno == null) {
        throw new RecursoNaoEncontradoException(
                "Aluno nÃ£o encontrado: " + identificadorAluno
        );
    }

    OfertaDisciplina oferta = ofertas.get(
            chave(codigoTurma, codigoDisciplina)
    );

    if (oferta == null) {
        throw new RecursoNaoEncontradoException(
                "Oferta nÃ£o encontrada para a turma e disciplina informadas."
        );
    }

    Matricula matricula = oferta.matricular(aluno);

    String id = String.format("MAT-%03d", proximoId++);
    matriculas.put(id, matricula);

    return Optional.of(matricula);
}

public String identificarMatricula(Matricula matricula) {
    return matriculas.entrySet()
            .stream()
            .filter(entrada -> entrada.getValue() == matricula)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow();
}

private String chave(String codigoTurma, String codigoDisciplina) {
    return codigoTurma + "|" + codigoDisciplina;
}


}

