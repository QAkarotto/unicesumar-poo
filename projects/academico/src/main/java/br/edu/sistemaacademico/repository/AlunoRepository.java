package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.util.Optional;

/**
 * Operações de acesso aos dados de {@link Aluno}.
 * <p>
 * Quem usa esta interface (o Controller) não sabe se os dados estão em SQLite,
 * em outro banco ou em memória: essa decisão fica na implementação.
 */
public interface AlunoRepository {

    /**
     * Grava um novo aluno.
     *
     * @throws RegistroDuplicadoException se já existir aluno com o mesmo registro acadêmico
     */
    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String registroAcademico);
}
