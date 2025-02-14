package sistemavendas.usuarios;

import javax.swing.*;
import java.util.Scanner;

import static sistemavendas.SistemaVendas.atualizarEstoque;

public class UsarioMenu {
    public static void usuarioMenu (UsuarioService usuarioService, sistemavendas.estoque.ComprarProduto comprarProduto, Scanner scanner, String usuarioAtual, boolean usuarioLogado, String tipoUsuario){
        int escolha;
        String[] options = {"Ir ao menu compra", "Visualizar Estoque", "Sair"};
        escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Menu Usuário",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (escolha) {
            case 0 -> comprarProduto.menuCompra(scanner, usuarioAtual, tipoUsuario);
            case 1 -> comprarProduto.visualizarEstoque();
            case 2 -> {
                usuarioLogado = false;
                usuarioAtual = null;
                tipoUsuario = null;
                System.out.println("Logout realizado.");
                // Transferir o conteúdo de estoque_temp.txt para estoque.txt ao sair
                atualizarEstoque();
            }
            default -> System.out.println("Opção inválida.");
        }

    }
}
