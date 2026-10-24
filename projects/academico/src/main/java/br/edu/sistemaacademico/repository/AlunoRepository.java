package br.edu.sistemaacademico.repository;
import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;

public interface AlunoRepository {
    Aluno salvar(Aluno aluno);
    Aluno buscarAlunoPorRa(String ra);
    List<Aluno> listarTodos();
}
