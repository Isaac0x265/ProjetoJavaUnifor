

import dao.AlunoDAO;
import dao.EmprestimoDAO;
import dao.LivroDAO;
import model.Aluno;
import model.Livro;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final AlunoDAO      alunoDAO      = new AlunoDAO();
    private static final LivroDAO      livroDAO      = new LivroDAO();
    private static final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private static final Scanner       in            = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("""
                \n===== Biblioteca =====
                1  - Cadastrar aluno
                2  - Listar alunos
                3  - Cadastrar livro
                4  - Listar livros
                5  - Registrar empréstimo
                6  - Registrar devolução
                7  - Listar empréstimos pendentes
                0  - Sair
                Escolha: """);

            switch (in.nextInt()) {
                case 1 -> cadastrarAluno();
                case 2 -> listarAlunos();
                case 3 -> cadastrarLivro();
                case 4 -> listarLivros();
                case 5 -> registrarEmprestimo();
                case 6 -> registrarDevolucao();
                case 7 -> listarPendentes();
                case 0 -> { System.out.println("Até logo!"); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    /* ---------- Operações ---------- */

    private static void cadastrarAluno() {
        try {
            System.out.print("Nome: ");
            in.nextLine(); // descarta newline
            String nome = in.nextLine();
            System.out.print("Matrícula (7 chars): ");
            String matricula = in.next();
            System.out.print("Data nascimento (AAAA-MM-DD): ");
            LocalDate nasc = LocalDate.parse(in.next());

            Aluno aluno = new Aluno(null, nome, matricula, nasc);
            int id = alunoDAO.save(aluno);
            System.out.println("Aluno salvo com id " + id);
        } catch (Exception e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    private static void listarAlunos() {
        try {
            List<Aluno> lista = alunoDAO.listAll();
            lista.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void cadastrarLivro() {
        try {
            System.out.print("Título: ");
            in.nextLine();
            String titulo = in.nextLine();
            System.out.print("Autor: ");
            String autor = in.nextLine();
            System.out.print("Ano publicação: ");
            Integer ano = in.nextInt();
            System.out.print("Quantidade estoque: ");
            int est = in.nextInt();

            Livro livro = new Livro(null, titulo, autor, ano, est);
            int id = livroDAO.save(livro);
            System.out.println("Livro salvo com id " + id);
        } catch (Exception e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
        }
    }

    private static void listarLivros() {
        try {
            List<Livro> lista = livroDAO.listAll();
            lista.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void registrarEmprestimo() {
        try {
            System.out.print("Id aluno: ");
            int idAluno = in.nextInt();
            System.out.print("Id livro: ");
            int idLivro = in.nextInt();
            System.out.print("Prazo (dias): ");
            int prazo = in.nextInt();

            boolean ok = emprestimoDAO.registrarEmprestimo(idAluno, idLivro, prazo);
            System.out.println(ok ? "Empréstimo registrado." : "Falha ao emprestar (estoque ou IDs inválidos).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void registrarDevolucao() {
        try {
            System.out.print("Id empréstimo: ");
            int idEmp = in.nextInt();
            boolean ok = emprestimoDAO.registrarDevolucao(idEmp);
            System.out.println(ok ? "Devolução registrada." : "Falha (id inválido ou já devolvido).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarPendentes() {
        try {
            emprestimoDAO.listarPendentes().forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
