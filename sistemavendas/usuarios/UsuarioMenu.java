package sistemavendas.usuarios;
import java.util.Scanner;
import javax.swing.*;
import sistemavendas.gerir.*;

public class UsuarioMenu {
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
                JOptionPane.showMessageDialog(null, "Logout realizado.");
                // Transferir o conteúdo de estoque_temp.txt para estoque.txt ao sair
                SistemaVendas.atualizarEstoque();
                return;
            }
            default -> {
                return;
            }
        }

    }
}
