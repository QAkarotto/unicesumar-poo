package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import br.edu.agroassist.exception.RegraNegocioException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "propriedades")
public class Propriedade {
    @Id
    @Column(length = 40)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produtor_id", nullable = false)
    private Usuario produtor;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 100)
    private String municipio;

    @Column(name = "area_ha", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaHa;

    @ManyToMany
    @JoinTable(
            name = "propriedade_culturas",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "cultura_id"))
    private Set<Cultura> culturas = new LinkedHashSet<>();

    protected Propriedade() {}

    public Propriedade(
            String id,
            Usuario produtor,
            String nome,
            String municipio,
            BigDecimal areaHa,
            Set<Cultura> culturas) {
        this.id = Objects.requireNonNull(id);
        this.produtor = Objects.requireNonNull(produtor);
        produtor.exigirPerfil(Perfil.PRODUTOR);
        this.nome = Validacao.obrigatorio(nome, 120, "o nome da propriedade");
        this.municipio = Validacao.obrigatorio(municipio, 100, "o município");
        if (areaHa == null
                || areaHa.signum() <= 0
                || areaHa.compareTo(new BigDecimal("9999999999.99")) > 0
                || areaHa.stripTrailingZeros().scale() > 2)
            throw new DadosInvalidosException(
                    "Informe uma área positiva em hectares, com até duas casas decimais.");
        if (culturas == null || culturas.isEmpty() || culturas.stream().anyMatch(Objects::isNull))
            throw new DadosInvalidosException("Marque pelo menos uma cultura plantada.");
        this.areaHa = areaHa;
        this.culturas.addAll(culturas);
    }

    public void exigirCultura(Cultura cultura) {
        if (cultura == null || culturas.stream().noneMatch(c -> c.getId().equals(cultura.getId())))
            throw new RegraNegocioException("RN01", "Escolha uma cultura cadastrada nesta propriedade.");
    }

    public String getId() {
        return id;
    }

    public Usuario getProdutor() {
        return produtor;
    }

    public String getNome() {
        return nome;
    }

    public String getMunicipio() {
        return municipio;
    }

    public BigDecimal getAreaHa() {
        return areaHa;
    }

    public Set<Cultura> getCulturas() {
        return Set.copyOf(culturas);
    }
}
