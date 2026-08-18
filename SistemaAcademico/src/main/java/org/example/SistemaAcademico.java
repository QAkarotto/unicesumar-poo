package org.example;

public class SistemaAcademico {
    public void main() {
        System.out.println("AULA INTRODUÇÃO AO JAVA");

        double nomeVariavel = 9.5;
        int inteiro = 1;
        char caractere = 'c';
        boolean booleano = true;
        String cadeia = "cadeia";

        var media = 6.9;

        System.out.println("Variáveis criadas:");
        System.out.println("String: " + cadeia);
        System.out.println("Booleano: " + booleano);

        situacaoAprovacao(media);

        if (media >= 7.0){
            System.out.println("Aprovado!");
        } else {
            System.out.println("RECUPERAÇÃO");
        }

        var conceito = "B";
        var situacaoAluno = switch (conceito) {
            case "A" -> "Excelente desempenho";
            case "B", "C" -> "Bom desempenho";
            case "D", "F" -> "Desempenho ruim";
            default -> "Conceito inválido";
        };
            System.out.println("Conceito: " + conceito + " significado: " + situacaoAluno);
        }

        public void situacaoAprovacao(double media) {
        if (media >= 7.0){
            System.out.println("Aluno aprovado");
        } else {
            System.out.println("Aluno em recuperação");
        }
        }
    }
