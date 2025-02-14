package sistemavendas.estoque;

import java.io.*;
import java.util.*;
import sistemavendas.gerir.*;

public class EstoqueService {
    private static final String STOCK_FILE = "data/estoque.txt";

    public void exibirEstoque() {
        System.out.println("\n===== ESTOQUE DISPONÍVEL =====");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE))) {
            String linha;
            boolean estoqueVazio = true;
    
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                
                if (partes.length >= 5) {
                    int quantidade = Integer.parseInt(partes[1]);
                    double preco = Double.parseDouble(partes[2]);
    
                    // Exibe o alerta se a quantidade for menor ou igual a 5
                    if (quantidade <= 5) {
                        System.out.println("ALERTA: O estoque do produto " + partes[0] + " está quase no fim!");
                    }
    
                    // Exibe os dados do produto de forma formatada
                    System.out.println("Produto: " + partes[0] + " | Quantidade: " + partes[1] + " | Preço: R$" + String.format("%.2f", preco) + " |Categoria:" + partes[3] + " |Marca: " + partes[4]);
                    estoqueVazio = false;
                }
            }
    
            if (estoqueVazio) {
                System.out.println("O estoque está vazio.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao exibir estoque.");
        }
    }
    

    // Método para gerenciar o estoque (adicionar, remover, editar produtos)
    public void gerenciarEstoque(Scanner scanner) {
        System.out.println("Gerenciar Estoque:");
        System.out.println("1. Adicionar Produto");
        System.out.println("2. Remover Produto");
        System.out.println("3. Editar Produto");
        System.out.println("Escolha uma opção:");

        int escolha = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer de entrada

        switch (escolha) {
            case 1 -> adicionarProduto(scanner);
            case 2 -> removerProduto(scanner);
            case 3 -> editarProduto(scanner);
            default -> System.out.println("Opção inválida.");
        }
    }

    private void adicionarProduto(Scanner scanner) {
       
        String nome;
        String quantidadeStr;
        String precoStr;
        String categoria;
        String marca;

        try {
            System.out.print("Nome do produto: ");
             nome = scanner.next().trim();
            Tratador.validarNomeProduto(nome);
            System.out.print("Quantidade: ");
              quantidadeStr = scanner.next().trim();
            Tratador.validarQuantidade(quantidadeStr);
            System.out.print("Preço: ");
             precoStr = scanner.next().trim();
            Tratador.validarPreco(precoStr);
            System.out.print("Categoria: ");
             categoria = scanner.next().trim();
            Tratador.validarCategoria(categoria);
            System.out.print("Marca: ");
             marca = scanner.next().trim();
            Tratador.validarMarca(marca);
         } catch (IllegalArgumentException ex){
               System.out.println("Erro: " + ex.getMessage());
               return;
        }
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr);
        // Adiciona o produto ao arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE, true))) {
            writer.write(nome + "," + quantidade + "," + preco + "," + categoria + "," + marca + "\n");
            System.out.println("Produto adicionado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao adicionar produto.");
        }
    }

    private void removerProduto(Scanner scanner) {
        System.out.println("Informe o nome do produto a ser removido:");
        String nome = scanner.nextLine();

        // Lógica para remover o produto
        File arquivo = new File(STOCK_FILE);
        File tempFile = new File("data/estoque_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String linha;
            boolean produtoEncontrado = false;

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes[0].equalsIgnoreCase(nome)) {
                    produtoEncontrado = true;
                    continue; // Pula a linha do produto encontrado
                }
                writer.write(linha + "\n");
            }

            if (!produtoEncontrado) {
                System.out.println("Produto não encontrado.");
            } else {
                arquivo.delete();
                tempFile.renameTo(arquivo);
                System.out.println("Produto removido com sucesso!");
            }

        } catch (IOException e) {
            System.out.println("Erro ao remover produto.");
        }
    }

    private void editarProduto(Scanner scanner) {
        System.out.println("Informe o nome do produto a ser editado:");
        String nome = scanner.nextLine();

        // Lógica para editar o produto
        File arquivo = new File(STOCK_FILE);
        File tempFile = new File("data/estoque_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String linha;
            boolean produtoEncontrado = false;

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes[0].equalsIgnoreCase(nome)) {
                    produtoEncontrado = true;
                    System.out.println("Produto encontrado. Informe os novos dados:");
                    System.out.println("Quantidade:");
                    int quantidade = scanner.nextInt();
                    System.out.println("Preço:");
                    double preco = scanner.nextDouble();
                    scanner.nextLine(); // Limpa o buffer de entrada
					System.out.println("Categoria:");
					String categoria = scanner.nextLine();
					System.out.println("Marca:");
					String marca = scanner.nextLine();
                    
                    writer.write(nome + "," + quantidade + "," + preco + "," + categoria + "," + marca + "\n");
                } else {
                    writer.write(linha + "\n");
                }
            }

            if (!produtoEncontrado) {
                System.out.println("Produto não encontrado.");
            } else {
                arquivo.delete();
                tempFile.renameTo(arquivo);
                System.out.println("Produto editado com sucesso!");
            }

        } catch (IOException e) {
            System.out.println("Erro ao editar produto.");
        }
    }
}
