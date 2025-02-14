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
                case 0 -> adicionarProduto();
                case 1 -> removerProduto();
                case 2 -> modificarPreco();
                case 3 -> {
                    return;
                }
                default -> {
                  return;
                }
            }
        }
    }

    private void adicionarProduto() {

        String nome = JOptionPane.showInputDialog("Nome do produto: ");
        String quantidadeStr = JOptionPane.showInputDialog("Quantidade: ");
        String precoStr = JOptionPane.showInputDialog("Preço: ");
        String categoria = JOptionPane.showInputDialog("Categoria: ");
        String marca = JOptionPane.showInputDialog("Marca: ");

        try {
            Tratador.validarNomeProduto(nome);
            Tratador.validarQuantidade(quantidadeStr);
            Tratador.validarPreco(precoStr);
            Tratador.validarCategoria(categoria);
            Tratador.validarMarca(marca);
         } catch (IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
               return;
        }
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr);

        if (quantidade <= 0 || preco <= 0) {
            JOptionPane.showMessageDialog(null, "Erro: Quantidade e preço devem ser maiores que zero.");
            return;
        }

        // Abrir o arquivo com permissão para escrita
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ESTOQUE, true))) {
            writer.write(nome + "," + quantidade + "," + preco + "," + categoria + "," + marca );
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar o produto: " + e.getMessage());
        }
    }

    private void removerProduto() {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        String nomeProduto = JOptionPane.showInputDialog("Nome do produto a remover: ");

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
            JOptionPane.showMessageDialog(null, "Erro ao ler o estoque: " + e.getMessage());
            return;
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "Produto não encontrado.");
            return;
        }

        // Criar um arquivo temporário para armazenar as modificações
        File arquivoTemp = new File("data/estoque_temp.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Produto removido com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao escrever no arquivo temporário: " + e.getMessage());
            return;
        }

        // Apagar o arquivo original
        File arquivoOriginal = new File(CAMINHO_ESTOQUE);
        if (arquivoOriginal.exists()) {
            if (arquivoOriginal.delete()) {
                JOptionPane.showMessageDialog(null, "Arquivo original removido com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao remover o arquivo original.");
                return;
            }
        }

        // Renomear o arquivo temporário para o nome do arquivo original
        if (arquivoTemp.renameTo(arquivoOriginal)) {
            JOptionPane.showMessageDialog(null, "Estoque atualizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao renomear o arquivo temporário.");
        }
    }

    private void modificarPreco() {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        String nomeProduto = JOptionPane.showInputDialog("Nome do produto a modificar: ");
        String Preco = JOptionPane.showInputDialog("Novo preço: ");
        double novoPreco = Double.parseDouble(Preco);

        if (novoPreco <= 0) {
            JOptionPane.showMessageDialog(null, "Erro: O preço deve ser maior que zero.");
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
            JOptionPane.showMessageDialog(null, "Erro ao ler o estoque: " + e.getMessage());
            return;
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "Produto não encontrado.");
            return;
        }

        // Criar um arquivo temporário para armazenar as modificações
        File arquivoTemp = new File("data/estoque_temp.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Preço atualizado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao escrever no arquivo temporário: " + e.getMessage());
            return;
        }

        // Apagar o arquivo original
        File arquivoOriginal = new File(CAMINHO_ESTOQUE);
        if (arquivoOriginal.exists()) {
            if (arquivoOriginal.delete()) {
                JOptionPane.showMessageDialog(null, "Arquivo original removido com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao remover o arquivo original.");
                return;
            }
        }

        // Renomear o arquivo temporário para o nome do arquivo original
        if (arquivoTemp.renameTo(arquivoOriginal)) {
            JOptionPane.showMessageDialog(null, "Estoque atualizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao renomear o arquivo temporário.");
        }
    }

    private int validarEntradaNumero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Digite um número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double validarEntradaDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Digite um valor numérico válido.");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
