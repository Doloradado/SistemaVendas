package sistemavendas.estoque;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ComprarProduto {
    private static final String CAMINHO_ESTOQUE_TEMP = "data/estoque_temp.txt"; // Alterado para estoque_temp.txt
    private static final String CAMINHO_HISTORICO_COMPRAS = "data/compras.txt";

    public void menuCompra(Scanner scanner, String usuario, String tipoUsuario) {
        if (!tipoUsuario.equalsIgnoreCase("usuario")) {
            System.out.println("Erro: Apenas usuários podem realizar compras.");
            return;
        }

        while (true) {
            System.out.println("===== MENU DE COMPRAS =====");
            System.out.println("1. Visualizar Estoque");
            System.out.println("2. Comprar Produto");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int escolha = validarEntradaNumero(scanner);

            switch (escolha) {
                case 1 -> visualizarEstoque();
                case 2 -> comprarProduto(scanner, usuario);
                case 3 -> {
                    return;
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    public void visualizarEstoque() {
        System.out.println("\n===== ESTOQUE DISPONÍVEL =====");
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ESTOQUE_TEMP))) { // Alterado para estoque_temp.txt
            String linha;
            boolean estoqueVazio = true;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 5) {
                    System.out.println("Produto: " + partes[0] + " | Quantidade: " + partes[1] + " | Preço: R$" + partes[2] + " | Categoria: " + partes[3] + " | Marca: " + partes[4]);
                    estoqueVazio = false;
                }
            }
            if (estoqueVazio) {
                System.out.println("O estoque está vazio.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao acessar o estoque: " + e.getMessage());
        }
    }

    private void comprarProduto(Scanner scanner, String usuario) {
        List<String> linhasEstoque = new ArrayList<>();
        double totalCompra = 0; // Variável para acumular o total da compra
        boolean produtoEncontrado = false;

        System.out.println("\n===== COMPRA DE PRODUTO =====");
        System.out.print("Nome do produto que deseja comprar: ");
        String nomeProduto = scanner.next();
        System.out.print("Quantidade desejada: ");
        int quantidadeCompra = validarEntradaNumero(scanner);

        if (quantidadeCompra <= 0) {
            System.out.println("Erro: A quantidade deve ser maior que zero.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ESTOQUE_TEMP))) { // Alterado para estoque_temp.txt
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                 nomeProduto = partes[0];
                int quantidadeDisponivel = Integer.parseInt(partes[1]);
                double preco = Double.parseDouble(partes[2]);
				
			    
                if (nomeProduto.equalsIgnoreCase(nomeProduto)) {
                    produtoEncontrado = true;
                    if (quantidadeCompra > quantidadeDisponivel) {
                        System.out.println("Erro: Estoque insuficiente. Apenas " + quantidadeDisponivel + " disponíveis.");
                        return;
                    }
                    int novaQuantidade = quantidadeDisponivel - quantidadeCompra;
                    linhasEstoque.add(nomeProduto + "," + novaQuantidade + "," + preco);
                    totalCompra += preco * quantidadeCompra; // Soma o total da compra
                    registrarCompra(usuario, nomeProduto, quantidadeCompra, preco * quantidadeCompra);

                    if (novaQuantidade <= 5) {
                        System.out.println("ALERTA: Estoque de " + nomeProduto + " está quase no fim! Restam apenas " + novaQuantidade + " unidades.");
                    }
                } else {
                    linhasEstoque.add(linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao acessar o estoque: " + e.getMessage());
            return;
        }

        if (!produtoEncontrado) {
            System.out.println("Erro: Produto não encontrado no estoque.");
            return;
        }

        // Atualiza o estoque temporário
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ESTOQUE_TEMP))) { // Alterado para estoque_temp.txt
            for (String linha : linhasEstoque) {
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Compra realizada com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o estoque: " + e.getMessage());
        }

        // Registra o total da compra no histórico de compras
        registrarTotalCompra(usuario, totalCompra);
        File estoqueTemp = new File("data/estoque_temp.txt");
        File estoqueOriginal = new File("data/estoque.txt");

        if (estoqueTemp.exists()) {
            try {
                // Apaga o arquivo original
                if (estoqueOriginal.exists()) {
                    estoqueOriginal.delete();
                }
                // Copia o conteúdo do estoque_temp.txt para estoque.txt
                Files.copy(estoqueTemp.toPath(), estoqueOriginal.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Estoque atualizado com sucesso!");
            } catch (IOException e) {
                System.out.println("Erro ao atualizar o estoque: " + e.getMessage());
            }
        } else {
            System.out.println("Erro: O arquivo estoque_temp.txt não existe.");
        }
    }

    private void registrarCompra(String usuario, String produto, int quantidade, double total) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_HISTORICO_COMPRAS, true))) {
            writer.write(usuario + "," + produto + "," + quantidade + "," + total);
            writer.newLine();
            System.out.println("Compra registrada no histórico.");
        } catch (IOException e) {
            System.out.println("Erro ao registrar compra: " + e.getMessage());
        }
    }

    private void registrarTotalCompra(String usuario, double totalCompra) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_HISTORICO_COMPRAS, true))) {
            writer.write("Total da compra de " + usuario + ": R$" + totalCompra);
            writer.newLine();
            System.out.println("Total da compra registrado no histórico.");
        } catch (IOException e) {
            System.out.println("Erro ao registrar total da compra: " + e.getMessage());
        }
    }

    private int validarEntradaNumero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Digite um número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
