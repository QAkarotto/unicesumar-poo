package br.edu.sistemaacademico.domain;

/**
 * Representa a situação de uma {@link Matricula} ao longo do seu ciclo de vida.
 * <p>
 * Toda matrícula nasce em {@link #EM_CURSO} e migra, de forma definitiva,
 * para {@link #APROVADO} ou {@link #REPROVADO}.
 */
public enum SituacaoMatricula {
    EM_CURSO,
    APROVADO,
    REPROVADO
}
