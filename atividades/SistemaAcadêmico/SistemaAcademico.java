package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {
        var sistema = new SistemaAcademico();

        double media = sistema.calcularMedia(7.0, 8.0, 9.0);
        String status = sistema.verificarStatus(media, 5);
        String orientacao = sistema.gerarOrientacao(status);

        System.out.println("Média: " + media + " | Status: " + status);
        System.out.println("Orientação: " + orientacao);
    }

    public double calcularMedia(double prova, double projeto, double lista) {
        var soma = prova + projeto + lista;
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
        return switch (status) {
            case "APROVADO" -> "Parabéns! Você dominou Classes e Objetos. Boas férias!";
            case "EXAME" -> "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default -> "Procure a coordenação do curso.";
        };
    }
}