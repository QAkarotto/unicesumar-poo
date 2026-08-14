package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {
        System.out.println("=== AULA: INTRODUÇÃO AO JAVA MODERNO ===");

        var sistema = new SistemaAcademico();

        sistema.mostrarExemplosDaAula();

        System.out.println("\n=================================================");
        System.out.println("=== INÍCIO DO TRABALHO PRÁTICO ===");
        System.out.println("=================================================");

        var notaProva = 5.5;
        var notaProjeto = 7.0;
        var notaLista = 6.5;
        var totalFaltas = 15;

        System.out.println("\n--- Teste 1: Cálculo de Média ---");
        var mediaFinal = sistema.calcularMedia(notaProva, notaProjeto, notaLista);
        System.out.println("Média em POO: " + mediaFinal);

        System.out.println("\n--- Teste 2: Verificação de Status ---");
        var status = sistema.verificarStatus(mediaFinal, totalFaltas);
        System.out.println("Status do aluno: " + status);

        System.out.println("\n--- Teste 3: Orientação Final ---");
        var orientacao = sistema.gerarOrientacao(status);
        System.out.println("Orientação: " + orientacao);
    }

    public void mostrarExemplosDaAula() {
        System.out.println("\n--- Exemplo 1: Tipos Clássicos vs var ---");

        double notaAntiga = 8.5;
        String disciplina = "Estrutura de Dados";

        var notaNova = 9.0;
        var sigla = "ED";
        System.out.println("Variáveis criadas: " + sigla + " e nota " + notaNova);

        System.out.println("\n--- Exemplo 2: Controle de Fluxo (if / else) ---");
        var mediaExemplo = 7.5;

        if (mediaExemplo >= 7.0) {
            System.out.println("A condição é VERDADEIRA: Aluno aprovado direto!");
        } else {
            System.out.println("A condição é FALSA: Aluno em exame.");
        }

        System.out.println("\n--- Exemplo 3: O Switch Moderno (Java 14+) ---");
        var conceito = "B";

        var feedbackExemplo = switch (conceito) {
            case "A" -> "Excelente desempenho!";
            case "B", "C" -> "Bom trabalho, continue assim.";
            case "D", "F" -> "Precisa revisar o conteúdo.";
            default -> "Conceito inválido.";
        };
        System.out.println("Conceito " + conceito + " significa: " + feedbackExemplo);
    }


    // EXERCÍCIO 1: média das três notas
    public double calcularMedia(double nota1, double nota2, double nota3) {
        var soma = nota1 + nota2 + nota3;
        var media = soma / 3;
        return media;
    }

    // EXERCÍCIO 2: status por faltas e média
    public String verificarStatus(double media, int faltas) {
        if (faltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (media >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

    // EXERCÍCIO 3: orientação usando switch expression
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