package br.edu.sistemaacademico.domain;

/**
 * Semestres letivos aceitos pelo sistema.
 *
 * <p>Por ser um {@code enum}, o próprio compilador garante que um
 * {@link PeriodoLetivo} só possa referenciar um destes dois valores,
 * eliminando a necessidade de validar strings ou números "mágicos".</p>
 */
public enum Semestre {

    PRIMEIRO,
    SEGUNDO
}
