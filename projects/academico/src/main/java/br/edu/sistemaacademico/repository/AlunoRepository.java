package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.Optional;

public interface AlunoRepository {

    /** Persiste o aluno. Lança RegistroDuplicadoException se o RA já existir. */
    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String registroAcademico);
}
