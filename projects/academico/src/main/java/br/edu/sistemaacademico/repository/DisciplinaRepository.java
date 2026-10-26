package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import java.util.List;

public interface DisciplinaRepository {
    Disciplina salvar(Disciplina disciplina);
    Disciplina buscarDisciplinaPorCodigo(String codigo);
    List<Disciplina> listarTodas();
}