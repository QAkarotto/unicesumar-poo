import java.util.Scanner;

public class calcular_media {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println ("-------atividade 1: tipos premitivos, variaveis e operadores-------");
        System.out.println ("---- calcular media ----");
        System.out.println("Digite a nota do aluno refenrente ao 1°bi: ");
        var prova = scanner.nextDouble();

        System.out.println("Digite a nota do refenrente ao 2°bi: ");
        var projeto = scanner.nextDouble();

        System.out.println("Digite a nota da refenrente ao 3°bi: ");
        var lista = scanner.nextDouble();

        var media = (prova + projeto + lista) / 3;
        String resultado = String.format("%.2f", media);
        System.out.println("Média final: " + media);

        scanner.close();
    }
}