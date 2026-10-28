package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository {

    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorIdentificador(String identificador);

    List<Aluno> listarTodos();
}
