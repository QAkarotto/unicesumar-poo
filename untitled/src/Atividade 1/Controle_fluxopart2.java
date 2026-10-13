import java.util.Scanner;

public class Controle_fluxopart2 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println ("-------atividade 3: Controle de Fluxo -------");
        System.out.println ("---- Moderno (Switch Expressions) ----");
        System.out.print("Digite a nota do refenrente ao 1°bi: ");
        var prova = scanner.nextDouble();

        System.out.print("Digite a nota do refenrente ao 2°bi: ");
        var projeto = scanner.nextDouble();

        System.out.print("Digite a nota do refenrente ao 3°bi: ");
        var lista = scanner.nextDouble();

        System.out.print("Digite o total de faltas do aluno: ");
        var faltas = scanner.nextInt();

        var media = (prova + projeto + lista) / 3;
        String status;

        if (faltas > 20) {
            status = "Reprovado por Falta";
        } else if (media >= 6.0) {
            status = "Aprovado";
        } else {
            status = "Sub";
        }

        var orientacao = switch (status) {
            case "APROVADO" ->
                    "Parabéns! Você dominou bem a materia de POO. Boas férias!";

            case "EXAME" ->
                    "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";

            case "REPROVADO_POR_FALTA" ->
                    "Reprovação automática. Frequência abaixo do mínimo exigido.";

            default ->
                    "Procure a coordenação do curso.";
        };
       String resultado = String.format("%.2f", media);
 
        System.out.println("------ Visão do aluno ------");
        System.out.println("--- O seu Resultado ---");
        System.out.println("Média: " + resultado);
        System.out.println("Faltas: " + faltas);
        System.out.println("Status: " + status);
        System.out.println("Orientação: " + orientacao);

        scanner.close();
    }
}