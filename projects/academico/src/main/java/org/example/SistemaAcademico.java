package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {
        System.out.println("=== AULA: INTRODUÇÃO AO JAVA MODERNO ===");
    }

    /*
     * EXERCÍCIO 1: Tipos Primitivos, var e Operadores
     *
     * Regra: A média é a soma das três notas dividida por 3.
     */
    public double calcularMedia(double nota1, double nota2, double nota3) {
        var soma = nota1 + nota2 + nota3;
        var mediaFinal = soma / 3;
        return mediaFinal;
    }

    /*
     * EXERCÍCIO 2: Controle de Fluxo Clássico (if / else)
     *
     * Regras:
     * Se faltas for MAIOR que 20 -> "REPROVADO_POR_FALTA"
     * Senão, se média for MAIOR OU IGUAL a 6.0 -> "APROVADO"
     * Caso contrário -> "EXAME"
     */
    public String verificarStatus(double media, int faltas) {
        if (faltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (media >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

    /*
     * EXERCÍCIO 3: Controle de Fluxo Moderno (Switch Expressions)
     *
     * Regra: Retornar uma mensagem de instrução baseada no status.
     */
    public String gerarOrientacao(String status) {
        var instrucao = switch (status) {
            case "APROVADO" -> "Parabéns! Você dominou Classes e Objetos. Boas férias!";
            case "EXAME" -> "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default -> "Procure a coordenação do curso.";
        };

        return instrucao;
    }
}