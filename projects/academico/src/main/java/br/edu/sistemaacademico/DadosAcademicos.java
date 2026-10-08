
package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DadosAcademicos {

    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new LinkedHashMap<>();

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
                "Programacao Orientada a Objetos",
                80
        );

        Disciplina bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        Turma turma = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        adicionarOferta(turma.ofertarDisciplina(poo));
        adicionarOferta(turma.ofertarDisciplina(bancoDados));

        ofertas.get("ESOFT4S-NB:POO").matricular(paola);
    }

    private void adicionarOferta(OfertaDisciplina oferta) {
        String chave = oferta.getTurma().getCodigo()
                + ":" + oferta.getDisciplina().getCodigo();

        ofertas.put(chave, oferta);
    }

    public Aluno buscarAluno(String identificador) {
        return alunos.get(identificador);
    }

    public OfertaDisciplina buscarOferta(String codigo) {
        return ofertas.get(codigo);
    }

    public Matricula buscarMatricula(String codigo) {
        for (OfertaDisciplina oferta : ofertas.values()) {
            for (Matricula matricula : oferta.getMatriculas()) {
                if (matricula.getCodigo().equals(codigo)) {
                    return matricula;
                }
            }
        }

        return null;
    }

    public Matricula criarMatricula(
            String identificadorAluno,
            String codigoOferta
    ) {
        Aluno aluno = buscarAluno(identificadorAluno);
        OfertaDisciplina oferta = buscarOferta(codigoOferta);

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno nao encontrado.");
        }

        if (oferta == null) {
            throw new IllegalArgumentException("Oferta nao encontrada.");
        }

        return oferta.matricular(aluno);
    }

    public Collection<Aluno> listarAlunos() {
        return alunos.values();
    }
}
