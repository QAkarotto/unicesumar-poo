package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import java.util.Optional;

/**
 * Operações de acesso aos dados de {@link Disciplina}.
 */
public interface DisciplinaRepository {

    /**
     * Grava uma nova disciplina.
     *
     * @throws RegistroDuplicadoException se já existir disciplina com o mesmo código
     */
    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorCodigo(String codigo);
}
