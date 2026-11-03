package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.Aluno;

import java.util.Optional;

/**
 * Define QUAIS operações de dados existem para Aluno,
 * sem dizer COMO elas são feitas (SQLite, outro banco, memória...).
 * Quem usa (o Controller) depende só desta interface.
 */
public interface AlunoRepository {

    /**
     * Grava um novo aluno.
     *
     * @throws RegistroDuplicadoException se já existir aluno com o mesmo RA
     */
    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String ra);
}
