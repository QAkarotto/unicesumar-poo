package br.edu.aniva.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

// Obra do catálogo. O catálogo é carregado pelo database/data.sql, por isso a entidade é somente leitura.

@Entity
@Table(name = "obra")
public class Obra {

    @Id
    @Column(length = 80)
    private String id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false)
    private double nota;

    @Column(nullable = false)
    private int popularidade;

    private Integer episodios;

    @Column(length = 100)
    private String estudio;

    @Column(length = 200)
    private String autor;

    @Column(length = 100)
    private String duracao;

    @Column(name = "plataformas_nota", length = 500)
    private String plataformasNota;

    @Column(length = 5000)
    private String sinopse;

    @Column(length = 500)
    private String capa;

    @Column(length = 500)
    private String banner;

    @ElementCollection
    @CollectionTable(name = "obra_tipo", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    @Column(name = "tipo", nullable = false, length = 20)
    private List<String> tipos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "obra_genero", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    @Column(name = "genero", nullable = false, length = 50)
    private List<String> generos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "obra_trope", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    @Column(name = "trope", nullable = false, length = 100)
    private List<String> tropes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "obra_aviso", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    @Column(name = "aviso", nullable = false, length = 100)
    private List<String> avisos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "obra_formato", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    private List<Formato> formatos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "obra_plataforma", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    private List<Plataforma> plataformas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "obra_personagem", joinColumns = @JoinColumn(name = "obra_id"))
    @OrderColumn(name = "posicao")
    private List<Personagem> personagens = new ArrayList<>();

    protected Obra() {
        // exigido pelo JPA
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public int getAno() { return ano; }
    public String getStatus() { return status; }
    public double getNota() { return nota; }
    public int getPopularidade() { return popularidade; }
    public Integer getEpisodios() { return episodios; }
    public String getEstudio() { return estudio; }
    public String getAutor() { return autor; }
    public String getDuracao() { return duracao; }
    public String getPlataformasNota() { return plataformasNota; }
    public String getSinopse() { return sinopse; }
    public String getCapa() { return capa; }
    public String getBanner() { return banner; }
    public List<String> getTipos() { return tipos; }
    public List<String> getGeneros() { return generos; }
    public List<String> getTropes() { return tropes; }
    public List<String> getAvisos() { return avisos; }
    public List<Formato> getFormatos() { return formatos; }
    public List<Plataforma> getPlataformas() { return plataformas; }
    public List<Personagem> getPersonagens() { return personagens; }
}
