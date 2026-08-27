package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {
        System.out.println("=== AULA: INTRODUÇÃO AO JAVA MODERNO ===");

        var sistema = new SistemaAcademico();

        System.out.println("\n=================================================");
        System.out.println("=== INÍCIO DO TRABALHO PRÁTICO (SEUS TESTES) ===");
        System.out.println("=================================================");

        // DADOS DO ALUNO PARA OS TESTES
        var notaProva = 5.0;
        var notaProjeto = 7.9;
        var notaLista = 5.0;
        var totalFaltas = 30;

        // TESTE DO EXERCÍCIO 1: Cálculo de Média
        System.out.println("\n--- Teste 1: Cálculo de Média ---");
        var mediaFinal = sistema.calcularMedia(notaProva, notaProjeto, notaLista);
        System.out.println("Média em POO: " + mediaFinal);

        // TESTE DO EXERCÍCIO 2: Verificação de Status
        System.out.println("\n--- Teste 2: Verificação de Status ---");
        var status = sistema.verificarStatus(mediaFinal, totalFaltas);
        System.out.println("Status do aluno: " + status);

        // TESTE DO EXERCÍCIO 3: Orientação do Sistema
        System.out.println("\n--- Teste 3: Orientação Final ---");
        var orientacao = sistema.gerarOrientacao(status);
        System.out.println("Orientação: " + orientacao);
    }

    public double calcularMedia(double nota1, double nota2, double nota3) {
        var soma = nota1 + nota2 + nota3;
        var media = soma / 3.0;
        return media;
    }

    public String verificarStatus(double media, int faltas) {
        if (faltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (media >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

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
