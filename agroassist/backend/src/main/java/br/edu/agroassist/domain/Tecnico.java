package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import br.edu.agroassist.exception.RegraNegocioException;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tecnicos")
public class Tecnico {
    @Id
    @Column(length = 40)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 120)
    private String formacao;

    @Column(nullable = false, unique = true, length = 60)
    private String registro;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToMany
    @JoinTable(
            name = "tecnico_culturas",
            joinColumns = @JoinColumn(name = "tecnico_id"),
            inverseJoinColumns = @JoinColumn(name = "cultura_id"))
    private Set<Cultura> culturas = new LinkedHashSet<>();

    protected Tecnico() {}

    public Tecnico(String id, Usuario usuario, String formacao, String registro, Set<Cultura> culturas) {
        this.id = Objects.requireNonNull(id);
        this.usuario = Objects.requireNonNull(usuario);
        usuario.exigirPerfil(Perfil.TECNICO);
        this.formacao = Validacao.obrigatorio(formacao, 120, "a formação");
        this.registro = Validacao.obrigatorio(registro, 60, "o registro profissional");
        if (culturas == null || culturas.isEmpty() || culturas.stream().anyMatch(Objects::isNull))
            throw new DadosInvalidosException("Marque pelo menos uma cultura em que o técnico é habilitado.");
        this.culturas.addAll(culturas);
        this.ativo = true;
    }

    public void exigirHabilitacao(Cultura cultura) {
        if (!ativo) throw new RegraNegocioException("RN02", "Não foi possível atribuir: registro inativo.");
        if (culturas.stream().noneMatch(c -> c.getId().equals(cultura.getId())))
            throw new RegraNegocioException(
                    "RN02", "Não foi possível atribuir: técnico não habilitado nesta cultura.");
    }

    public void alterarSituacao(boolean ativo) {
        this.ativo = ativo;
    }

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getFormacao() {
        return formacao;
    }

    public String getRegistro() {
        return registro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Set<Cultura> getCulturas() {
        return Set.copyOf(culturas);
    }
}
