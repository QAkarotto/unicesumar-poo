package br.edu.agroassist.domain;

import br.edu.agroassist.exception.OperacaoNaoPermitidaException;
import br.edu.agroassist.exception.RegraNegocioException;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private Perfil perfil;

    @Column(nullable = false, length = 30)
    private String telefone;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(nullable = false, length = 100)
    private String municipio;

    @Column(name = "consentimento_em", nullable = false)
    private LocalDateTime consentimentoEm;

    protected Usuario() {}

    public Usuario(
            String id,
            String nome,
            Perfil perfil,
            String telefone,
            String email,
            String municipio,
            LocalDateTime consentimentoEm) {
        this.id = Objects.requireNonNull(id);
        this.nome = Validacao.obrigatorio(nome, 120, "o nome");
        this.perfil = Objects.requireNonNull(perfil);
        this.telefone = Validacao.obrigatorio(telefone, 30, "o telefone");
        this.email = Validacao.texto(email, 160, "E-mail");
        this.municipio = Validacao.obrigatorio(municipio, 100, "o município");
        this.consentimentoEm = Objects.requireNonNull(consentimentoEm);
    }

    public static Usuario criarProdutor(
            String nome, String telefone, String email, String municipio, boolean consentimento) {
        if (!consentimento)
            throw new RegraNegocioException(
                    "RN07", "Para criar a conta, marque o consentimento de uso dos dados (LGPD).");
        return new Usuario(
                "u" + UUID.randomUUID(),
                nome,
                Perfil.PRODUTOR,
                telefone,
                email,
                municipio,
                LocalDateTime.now());
    }

    public void exigirPerfil(Perfil esperado) {
        if (perfil != esperado)
            throw new OperacaoNaoPermitidaException("Este perfil não pode realizar esta operação.");
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getMunicipio() {
        return municipio;
    }

    public LocalDateTime getConsentimentoEm() {
        return consentimentoEm;
    }
}
