package br.com.vetagenda.dto.responses;
import br.com.vetagenda.domain.Animal;
import java.math.BigDecimal;
import java.time.LocalDate;
public record AnimalResponse(Long id, String nome, String especie, String raca, String sexo,
                             LocalDate dataNascimento, BigDecimal peso, Long tutorId, String tutorNome) {
    public static AnimalResponse from(Animal a) {
        return new AnimalResponse(a.getId(), a.getNome(), a.getEspecie(), a.getRaca(), a.getSexo(),
            a.getDataNascimento(), a.getPeso(), a.getTutor().getId(), a.getTutor().getNome());
    }
}