package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.Optional;

/** Contrato de acesso aos dados de alunos, independente da tecnologia de persistência. */
public interface AlunoRepository {

    /** Grava o aluno. Lança {@link RegistroDuplicadoException} se o RA já existir. */
    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String ra);
}
