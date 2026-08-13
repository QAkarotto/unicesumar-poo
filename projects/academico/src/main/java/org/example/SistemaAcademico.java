package org.example;

public class SistemaAcademico {

    public static double calcularMedia(double prova, double projeto, double lista) {
        var media = (prova + projeto + lista) / 3;
        return media;
    }

    public static String verificarStatus(double media, int faltas) {
        if (faltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (media >= 6.0) {
            return "APROVADO";
        } else {D
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