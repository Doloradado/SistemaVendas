package sistemavendas.gerir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import javax.swing.*;
import sistemavendas.estoque.*;
import sistemavendas.usuarios.*;

public class SistemaVendas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioService usuarioService = new UsuarioService();
        EstoqueService estoqueService = new EstoqueService();
        ComprarProduto comprarProduto = new ComprarProduto();

        verificarAdministrador(usuarioService);

        boolean usuarioLogado = false;
        String usuarioAtual = null;
        String tipoUsuario = null;
        int escolha;

        while (true) {
            if (!usuarioLogado) {
                JOptionPane.showMessageDialog(null, "Bem-vindo ao Sistema de Estoque Digital!");
                String[] options = {"Cadastro", "Login", "Sair"};
                escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Tela inicial",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                switch (escolha) {
                    case 0 -> usuarioService.registrarUsuario(scanner);
                    case 1 -> {
                        String[] loginInfo = usuarioService.login();
                        if (loginInfo != null) {
                            usuarioLogado = true;
                            usuarioAtual = loginInfo[0];
                            tipoUsuario = loginInfo[1];
                        }
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(null, "Saindo...");
                        return;
                    }
                    default -> {
                        return;
                    }    
                }
            } else {
                switch (tipoUsuario) {
                    case "admin":
                        AdminMenu.adminMenu(usuarioService, estoqueService, scanner, usuarioAtual);
                        break;
                    case "gerente":
                        GerenteMenu.gerenteMenu(usuarioService, estoqueService, scanner, usuarioAtual);
                        break;
                    case "usuario":
                        UsuarioMenu.usuarioMenu(usuarioService, comprarProduto, scanner, usuarioAtual, usuarioLogado, tipoUsuario);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    // Método para transferir o conteúdo de estoque_temp.txt para estoque.txt
    public static void atualizarEstoque() {
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
                JOptionPane.showMessageDialog(null, "Estoque atualizado com sucesso!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Opção Inválida! " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Erro: O arquivo estoque_temp.txt não existe.");
        }
    }
    private static void verificarAdministrador(UsuarioService usuarioService) {
        File arquivoUsuarios = new File("data/usuarios.txt");
        if (!arquivoUsuarios.exists() || arquivoUsuarios.length() == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum administrador encontrado. Criando primeiro usuário como ADMIN.");
            usuarioService.registrarAdministrador();
        }
    }

    
}
