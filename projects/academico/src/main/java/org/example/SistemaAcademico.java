package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {

        double notaProva = 7.5;
        double notaProjeto = 6.0;
        double notaLista = 8.0;
        int totalFaltas = 5;


        double mediaFinal = calcularMedia(notaProva, notaProjeto, notaLista);
        String status = verificarStatus(mediaFinal, totalFaltas);
        String orientacao = gerarOrientacao(status);

        System.out.println("Média Final: " + mediaFinal);
        System.out.println("Status do Aluno: " + status);
        System.out.println("Orientação: " + orientacao);
    }

    public static double calcularMedia(double prova, double projeto, double lista) {
        var soma = prova + projeto + lista;
        var media = soma / 3.0;
        return media;
    }

    public static String verificarStatus(double mediaFinal, int totalFaltas) {
        if (totalFaltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (mediaFinal >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

    public static String gerarOrientacao(String status) {
        return switch (status) {
            case "APROVADO" -> "Parabéns! Você dominou Classes e Objetos. Boas férias!";
            case "EXAME" -> "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default -> "Procure a coordenação do curso.";
        };
    }
}