package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;

import java.util.Optional;

/** Contrato de acesso aos dados de disciplinas, independente da tecnologia de persistência. */
public interface DisciplinaRepository {

    /** Grava a disciplina. Lança {@link RegistroDuplicadoException} se o código já existir. */
    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorCodigo(String codigo);
}
