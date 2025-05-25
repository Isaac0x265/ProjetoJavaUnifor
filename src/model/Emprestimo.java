package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Vincula um Aluno a um Livro, registrando datas de empréstimo e devolução.
 */
public class Emprestimo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer   idEmprestimo;      // PK
    private Integer   idAluno;           // FK (simplificado: guardar só o id)
    private Integer   idLivro;           // FK
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;     // nulo enquanto não devolvido

    /* --------- Construtores --------- */

    public Emprestimo() { }

    public Emprestimo(Integer idEmprestimo,
                      Integer idAluno,
                      Integer idLivro,
                      LocalDate dataEmprestimo,
                      LocalDate dataDevolucao) {

        this.idEmprestimo = idEmprestimo;
        this.idAluno = Objects.requireNonNull(idAluno);
        this.idLivro = Objects.requireNonNull(idLivro);
        this.dataEmprestimo = Objects.requireNonNull(dataEmprestimo);
        this.dataDevolucao = dataDevolucao;
    }

    /* --------- Getters & Setters --------- */

    public Integer getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(Integer idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = Objects.requireNonNull(idAluno);
    }

    public Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = Objects.requireNonNull(idLivro);
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = Objects.requireNonNull(dataEmprestimo);
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    /* --------- Regras de domínio auxiliares --------- */

    /** Retorna true se o livro já foi devolvido. */
    public boolean isDevolvido() {
        return dataDevolucao != null;
    }

    /** Quantos dias de atraso (negativo ou zero se não houver atraso / não devolvido). */
    public long diasDeAtraso(LocalDate dataReferencia) {
        if (dataDevolucao == null) return 0;
        return ChronoUnit.DAYS.between(dataReferencia, dataDevolucao);
    }

    /* --------- Métodos utilitários --------- */

    @Override
    public int hashCode() {
        return Objects.hash(idEmprestimo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Emprestimo other)) return false;
        return Objects.equals(idEmprestimo, other.idEmprestimo);
    }

    @Override
    public String toString() {
        return "Emprestimo[id=%d, aluno=%d, livro=%d, emprestado=%s, devolucao=%s]"
                .formatted(idEmprestimo, idAluno, idLivro,
                        dataEmprestimo, dataDevolucao);
    }
}
