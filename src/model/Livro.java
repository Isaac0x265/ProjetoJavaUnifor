package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um livro do acervo.
 */
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idLivro;            // PK
    private String  titulo;
    private String  autor;
    private Integer anoPublicacao;
    private Integer quantidadeEstoque;

    /* --------- Construtores --------- */

    public Livro() { }

    public Livro(Integer idLivro,
                 String titulo,
                 String autor,
                 Integer anoPublicacao,
                 Integer quantidadeEstoque) {

        this.idLivro = idLivro;
        setTitulo(titulo);
        setAutor(autor);
        setAnoPublicacao(anoPublicacao);
        setQuantidadeEstoque(quantidadeEstoque);
    }

    /* --------- Getters & Setters --------- */

    public Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = Objects.requireNonNull(titulo, "Título não pode ser nulo");
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = Objects.requireNonNullElse(autor, "Desconhecido");
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        if (anoPublicacao != null && anoPublicacao < 0) {
            throw new IllegalArgumentException("Ano de publicação inválido");
        }
        this.anoPublicacao = anoPublicacao;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        if (quantidadeEstoque != null && quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    /* --------- Métodos utilitários --------- */

    @Override
    public int hashCode() {
        return Objects.hash(idLivro);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Livro other)) return false;
        return Objects.equals(idLivro, other.idLivro);
    }

    @Override
    public String toString() {
        return "Livro[id=%d, título='%s', autor='%s']"
                .formatted(idLivro, titulo, autor);
    }
}
