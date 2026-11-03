package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;

import java.util.Optional;

public interface DisciplinaRepository {

    /** Persiste a disciplina. Lança RegistroDuplicadoException se o código já existir. */
    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorCodigo(String codigo);
}
