package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.Optional;

/** Contrato de acesso aos dados de Aluno. Quem usa não sabe se por trás há JDBC, arquivo ou outro banco. */
public interface AlunoRepository {

    /** @throws RegistroDuplicadoException se já existe aluno com o mesmo registro acadêmico */
    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String registroAcademico);
}
