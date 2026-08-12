// Excercicio 1
public static double calcularMedia(double prova, double projeto, double lista) {
    // Utilizando 'var' para as variáveis locais conforme o requisito
    var somaDasNotas = prova + projeto + lista;
    var mediaFinal = somaDasNotas / 3.0;

    return mediaFinal;
}

// Excercicio 2
public static String verificarStatus(double media, int faltas) {
    if (faltas > 20) {
        return "REPROVADO_POR_FALTA";
    } else if (media >= 6.0) {
        return "APROVADO";
    } else {
        return "EXAME";
    }
}

// Excercicio 3
public static String gerarOrientacao(String status) {
    // Retornando diretamente o Switch Expression (Java 14+)
    return switch (status) {
        case "APROVADO" -> "Parabéns! Você dominou Classes e Objetos. Boas férias!";
        case "EXAME" -> "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
        case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
        default -> "Procure a coordenação do curso.";
    };
}