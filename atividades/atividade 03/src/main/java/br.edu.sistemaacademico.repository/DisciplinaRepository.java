package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;

import java.util.Optional;

/** Contrato de acesso aos dados de Disciplina. */
public interface DisciplinaRepository {

    /** @throws RegistroDuplicadoException se já existe disciplina com o mesmo código */
    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorCodigo(String codigo);
}
