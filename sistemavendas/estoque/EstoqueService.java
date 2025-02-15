package sistemavendas.estoque;

import java.io.*;
import java.util.*;
import sistemavendas.gerir.*;

import javax.swing.*;

public class EstoqueService {
    private static final String STOCK_FILE = "data/estoque.txt";

    public void exibirEstoque() {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE))) {
            String linha;
            boolean estoqueVazio = true;

            StringBuilder sb = new StringBuilder();
            sb.append("Estoque: ").append("\n\n");
    
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                
                if (partes.length >= 5) {
                    int quantidade = Integer.parseInt(partes[1]);
                    double preco = Double.parseDouble(partes[2]);
    
                    // Exibe o alerta se a quantidade for menor ou igual a 5
                    if (quantidade <= 5) {
                        System.out.println("ALERTA: O estoque do produto " + partes[0] + " está quase no fim!");
                    }

                        sb.append("Produto:: ").append(partes[0]).append("\n")
                                .append("Quantidade: ").append(partes[1]).append("\n")
                                .append("Preço: R$ ").append(String.format("%.2f", preco)).append("\n")
                                .append("Categoria: ").append(partes[3]).append("\n")
                                .append("Marca: : ").append(partes[4]).append("\n\n");

                    // Exibe os dados do produto de forma formatada
                    estoqueVazio = false;
                }
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Estoque", JOptionPane.INFORMATION_MESSAGE);
    
            if (estoqueVazio) {
                JOptionPane.showMessageDialog(null, "O estoque está vazio.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exibir estoque.");
        }
    }
    

    // Método para gerenciar o estoque (adicionar, remover, editar produtos)
    public void gerenciarEstoque(Scanner scanner) {
        int escolha;
        JOptionPane.showMessageDialog(null, "Gerenciador de Estoque");
        String[] options = {"Adicionar Produto", "Remover Produto","Editar Produto","Sair"};
        escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Gerenciador de Estoque",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (escolha) {
            case 0 -> adicionarProduto(scanner);
            case 1 -> removerProduto(scanner);
            case 2 -> editarProduto(scanner);
            default -> {
                return;
            }
        }
    }

    private void adicionarProduto(Scanner scanner) {
       
        String nome;
        String quantidadeStr;
        String precoStr;
        String categoria;
        String marca;

        try {
            nome = JOptionPane.showInputDialog("Nome do produto: ");
            Tratador.validarNomeProduto(nome);
            quantidadeStr = JOptionPane.showInputDialog("Quantidade: ");
            Tratador.validarQuantidade(quantidadeStr);
            precoStr = JOptionPane.showInputDialog("Preço: ");
            Tratador.validarPreco(precoStr);
            categoria = JOptionPane.showInputDialog("Categoria: ");
            Tratador.validarCategoria(categoria);
            marca = JOptionPane.showInputDialog("Marca: ");
            Tratador.validarMarca(marca);
         } catch (IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
               return;
        }
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr);
        // Adiciona o produto ao arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE, true))) {
            writer.write(nome + "," + quantidade + "," + preco + "," + categoria + "," + marca + "\n");
            JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar produto.");
        }
    }

    private void removerProduto(Scanner scanner) {
        String nome = JOptionPane.showInputDialog("Informe o nome do produto a ser removido:");

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
                JOptionPane.showMessageDialog(null, "Produto não encontrado.");
            } else {
                arquivo.delete();
                tempFile.renameTo(arquivo);
                JOptionPane.showMessageDialog(null, "Produto removido com sucesso!");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover produto.");
        }
    }

    private void editarProduto(Scanner scanner) {
        String nome = JOptionPane.showInputDialog("Informe o nome do produto a ser editado:");

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
                    JOptionPane.showMessageDialog(null, "Produto encontrado. Informe os novos dados.");

                    String quantidade  = JOptionPane.showInputDialog("Quantidade: ");
                    String preco = JOptionPane.showInputDialog("Preço: ");
                    String categoria = JOptionPane.showInputDialog("Categoria: ");
                    String marca = JOptionPane.showInputDialog("Marca: ");
                    
                    writer.write(nome + "," + quantidade + "," + preco + "," + categoria + "," + marca + "\n");
                } else {
                    writer.write(linha + "\n");
                }
            }

            if (!produtoEncontrado) {
                JOptionPane.showMessageDialog(null, "Produto não encontrado.");
            } else {
                arquivo.delete();
                tempFile.renameTo(arquivo);
                JOptionPane.showMessageDialog(null, "Produto editado com sucesso!");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar produto.");
        }
    }
}
