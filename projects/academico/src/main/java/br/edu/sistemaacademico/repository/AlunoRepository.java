package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository {

    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String ra);

    default Optional<Aluno> buscarPorRegistroAcademico(String registroAcademico) {
        return buscarPorRa(registroAcademico);
    }

    List<Aluno> listarTodos();

    boolean existePorRa(String ra);
}
