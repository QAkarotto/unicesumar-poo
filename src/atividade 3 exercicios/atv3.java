public class atv3 {

    public static void main(String[] args) {
        System.out.println("=== AULA: INTRODUÇÃO AO JAVA MODERNO ===");

        var sistema = new SistemaAcademico();

        sistema.mostrarExemplosDaAula();


        System.out.println("\n=================================================");
        System.out.println("=== INÍCIO DO TRABALHO PRÁTICO (SEUS TESTES) ===");
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

        // Jeito clássico (tipagem explícita)
        double notaAntiga = 8.5;
        String disciplina = "Estrutura de Dados";

        // Jeito moderno (inferência com var - o Java adivinha o tipo pelo valor)
        var notaNova = 9.0;
        var sigla = "ED";
        System.out.println("Variáveis criadas: " + sigla + " e nota " + notaNova);


        System.out.println("\n--- Exemplo 2: Controle de Fluxo (if / else) ---");
        var mediaExemplo = 7.5;

        // O if verifica se a condição dentro dos parênteses é verdadeira
        if (mediaExemplo >= 7.0) {
            System.out.println("A condição é VERDADEIRA: Aluno aprovado direto!");
        } else {
            System.out.println("A condição é FALSA: Aluno em exame.");
        }


        System.out.println("\n--- Exemplo 3: O Switch Moderno (Java 14+) ---");
        var conceito = "B";

        // O switch moderno usa a setinha (->) e não precisa da palavra "break"
        var feedbackExemplo = switch (conceito) {
            case "A" -> "Excelente desempenho!";
            case "B", "C" -> "Bom trabalho, continue assim."; // Agrupando casos
            case "D", "F" -> "Precisa revisar o conteúdo.";
            default -> "Conceito inválido."; // O default salva se nenhuma opção bater
        };
        System.out.println("Conceito " + conceito + " significa: " + feedbackExemplo);
    }


    // ========================================================================
    // SEÇÃO DOS EXERCÍCIOS (Preencha os blocos com TODO)
    // ========================================================================

    /*
     * EXERCÍCIO 1: Tipos Primitivos, var e Operadores
     * Regra: A média é a soma das três notas dividida por 3.
     */
    public double calcularMedia(double Nota1, double Nota2, double Nota3) {
        var Soma = Nota1 + Nota2 + Nota3;
        var Media = Soma / 3;
        return Media;
    }

    /*
     * EXERCÍCIO 2: Controle de Fluxo Clássico (if / else)
     * Regras:
     * - Se faltas for MAIOR que 20 -> Retorna "REPROVADO_POR_FALTA"
     * - Senão, se a média for MAIOR OU IGUAL a 6.0 -> Retorna "APROVADO"
     * - Caso contrário (média menor que 6.0 e faltas OK) -> Retorna "EXAME"
     */
    public String verificarStatus(double Media, int Faltas) {
        if (Faltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (Media >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

    /*
     * EXERCÍCIO 3: Controle de Fluxo Moderno (Switch Expressions)
     * Regra: Retornar uma mensagem de instrução baseada no status.
     */
    public String gerarOrientacao(String Status) {
        var Instrucao = switch (Status) {
            case "APROVADO" -> "Parabéns! Você dominou Classes e Objetos. Boas férias!";
            case "EXAME" -> "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default -> "Procure a coordenação do curso.";
        };
        return Instrucao;
    }
}