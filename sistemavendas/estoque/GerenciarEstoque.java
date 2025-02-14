package sistemavendas.estoque;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import sistemavendas.gerir.*;

public class GerenciarEstoque {
    private static final String CAMINHO_ESTOQUE = "data/estoque.txt";

    public void gerenciarEstoque(Scanner scanner) {
        int escolha;
        while (true) {
            String[] options = {"Adicionar Produto", "Remover Produto", "Modificar Preço","Voltar ao Menu Principal"};
            escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Gerenciamento De Estoque",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (escolha) {
                case 0 -> adicionarProduto(scanner);
                case 1 -> removerProduto(scanner);
                case 2 -> modificarPreco(scanner);
                case 3 -> {
                    return;
                }
                default -> {
                  return;
                }
            }
        }
    }

    private void adicionarProduto(Scanner scanner) {
        System.out.print("Nome do produto: ");
        String nome = scanner.next().trim();
        System.out.print("Quantidade: ");
        String quantidadeStr = scanner.next().trim();
        System.out.print("Preço: ");
        String precoStr = scanner.next().trim();
        System.out.print("Categoria: ");
        String categoria = scanner.next().trim();
        System.out.print("Marca: ");
        String marca = scanner.next().trim();

        try {
            Tratador.validarNomeProduto(nome);
            Tratador.validarQuantidade(quantidadeStr);
            Tratador.validarPreco(precoStr);
            Tratador.validarCategoria(categoria);
            Tratador.validarMarca(marca);
         } catch (IllegalArgumentException ex){
               System.out.println("Erro: " + ex.getMessage());
               return;
        }
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr);

        if (quantidade <= 0 || preco <= 0) {
            System.out.println("Erro: Quantidade e preço devem ser maiores que zero.");
            return;
        }

        // Abrir o arquivo com permissão para escrita
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ESTOQUE, true))) {
            writer.write(nome + "," + quantidade + "," + preco + "," + categoria + "," + marca );
            writer.newLine();
            System.out.println("Produto adicionado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o produto: " + e.getMessage());
        }
    }

    private void removerProduto(Scanner scanner) {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        System.out.print("Nome do produto a remover: ");
        String nomeProduto = scanner.next();

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ESTOQUE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (!partes[0].equalsIgnoreCase(nomeProduto)) {
                    linhas.add(linha);
                } else {
                    encontrado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o estoque: " + e.getMessage());
            return;
        }

        if (!encontrado) {
            System.out.println("Produto não encontrado.");
            return;
        }

        // Criar um arquivo temporário para armazenar as modificações
        File arquivoTemp = new File("data/estoque_temp.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Produto removido com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo temporário: " + e.getMessage());
            return;
        }

        // Apagar o arquivo original
        File arquivoOriginal = new File(CAMINHO_ESTOQUE);
        if (arquivoOriginal.exists()) {
            if (arquivoOriginal.delete()) {
                System.out.println("Arquivo original removido com sucesso.");
            } else {
                System.out.println("Erro ao remover o arquivo original.");
                return;
            }
        }

        // Renomear o arquivo temporário para o nome do arquivo original
        if (arquivoTemp.renameTo(arquivoOriginal)) {
            System.out.println("Estoque atualizado com sucesso!");
        } else {
            System.out.println("Erro ao renomear o arquivo temporário.");
        }
    }

    private void modificarPreco(Scanner scanner) {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        System.out.print("Nome do produto a modificar: ");
        String nomeProduto = scanner.next();
        System.out.print("Novo preço: ");
        double novoPreco = validarEntradaDouble(scanner);

        if (novoPreco <= 0) {
            System.out.println("Erro: O preço deve ser maior que zero.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ESTOQUE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes[0].equalsIgnoreCase(nomeProduto)) {
                    linhas.add(partes[0] + "," + partes[1] + "," + novoPreco);
                    encontrado = true;
                } else {
                    linhas.add(linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o estoque: " + e.getMessage());
            return;
        }

        if (!encontrado) {
            System.out.println("Produto não encontrado.");
            return;
        }

        // Criar um arquivo temporário para armazenar as modificações
        File arquivoTemp = new File("data/estoque_temp.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Preço atualizado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo temporário: " + e.getMessage());
            return;
        }

        // Apagar o arquivo original
        File arquivoOriginal = new File(CAMINHO_ESTOQUE);
        if (arquivoOriginal.exists()) {
            if (arquivoOriginal.delete()) {
                System.out.println("Arquivo original removido com sucesso.");
            } else {
                System.out.println("Erro ao remover o arquivo original.");
                return;
            }
        }

        // Renomear o arquivo temporário para o nome do arquivo original
        if (arquivoTemp.renameTo(arquivoOriginal)) {
            System.out.println("Estoque atualizado com sucesso!");
        } else {
            System.out.println("Erro ao renomear o arquivo temporário.");
        }
    }

    private int validarEntradaNumero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Digite um número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double validarEntradaDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada inválida. Digite um valor numérico válido.");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
