import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Autor;
import model.Livro;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca-jpa");
    private static Scanner leitor = new Scanner(System.in);
    public static void main(String[] args) {
        int escolhaUsuario = 0;

        while (escolhaUsuario != 5) {
            System.out.println("Seja bem vindo ao submundo!");
            System.out.println("\n1 - Cadastrar um livro");
            System.out.println("\n2 - Listar todos os livros  ");
            System.out.println("\n3 - Atualizar um livro");
            System.out.println("\n4 - Remover um livro");
            System.out.println("\n5 - Sair");
            System.out.println("\nDigite uma opção:\t");
            escolhaUsuario = leitor.nextInt();
            leitor.nextLine();
            switch (escolhaUsuario) {
                case 1:
                    addBook();
                    break;
                case 2:
                    listBook();
                    break;
                case 3:
                    attBook();
                    break;
                case 4:
                    removeBook();
                    break;
                case 5:
                    System.out.println("Obrigado por ter usado o programa!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        }
        System.out.println("Encerrando o programa");
        leitor.close();
        emf.close();
    }
    private static void addBook() {
        EntityManager em = emf.createEntityManager();
        System.out.println("\nDigite o titulo do livro: ");
        String titulo = leitor.nextLine();
        System.out.println("\nDigite o nome autor do livro");
        String autor = leitor.nextLine();
        Autor novoAutor = new Autor(autor);
        Livro novoLivro = new Livro(titulo, novoAutor);
        try {
            em.getTransaction().begin(); //Inicio de tudo, começa a operação
            em.persist(novoLivro); //Gerente, pegue esse novo livro
            em.getTransaction().commit(); // salva os dados, da commit
            System.out.println("Livro salvo com sucesso!");
        } catch (Exception e){
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            System.out.println("Erro ao salvar o livro");
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    private static void listBook() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Livro> livros = em.createQuery("SELECT l FROM Livro l", Livro.class).getResultList();
            if (livros.isEmpty()){
                System.out.println("\nLista vazia!");
            }else {
                System.out.println("\nOs livros cadastrados são: \n");
                for (Livro livro : livros) {
                    System.out.println(livro + "\n");
                }
            }
        } catch (Exception e){
            System.out.println("Erro ao listar livros");
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }


    private static void attBook() {
        EntityManager em = emf.createEntityManager();
        try {
            listBook();
            long id;
            System.out.println("\nDigite o ID do livro que você deseja atualizar: ");
            id = leitor.nextInt();
            leitor.nextLine();
            System.out.println("\nDigite o novo titulo do livro: ");
            String novoTitulo = leitor.nextLine();
            System.out.println("\nDigite o novo nome do autor/autora: ");
            String novoNome = leitor.nextLine();
            em.getTransaction().begin();
            Livro livroParaAtualizar = em.find(Livro.class, id); //como é para buscar algo do banco, já existente, então nao precisa fazer o New..

            if (livroParaAtualizar == null){
                System.out.println("\nLivro não encontrado!");
            }else {
                livroParaAtualizar.setTitulo(novoTitulo);
                livroParaAtualizar.getAutor().setNome(novoNome); // como ele é da classe autor, deve-se fazer assim
                em.getTransaction().commit();
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            System.out.println("Erro ao atualizar o livro");
            e.printStackTrace();
        }finally {
            em.close();
        }

    }

    private static void removeBook() {
        EntityManager em = emf.createEntityManager();
        try {
            listBook();
            long id;
            System.out.println("\nDigite o ID do livro que você deseja atualizar: ");
            id = leitor.nextInt();
            leitor.nextLine();
            em.getTransaction().begin();
            Livro livroParaRemover = em.find(Livro.class, id); //como é para buscar algo do banco, já existente, então nao precisa fazer o New..
            if (livroParaRemover == null){
                System.out.println("\nLivro não encontrado!");
                em.getTransaction().rollback();
            }else {
                em.remove(livroParaRemover);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            System.out.println("Erro ao atualizar o livro");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
