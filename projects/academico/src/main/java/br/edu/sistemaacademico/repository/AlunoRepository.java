package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.Optional;

public interface AlunoRepository {
    Aluno salvar(Aluno aluno);
    Optional<Aluno> buscarPorRa(String ra);
}
