package sistemavendas.usuarios;

import java.util.Scanner;
import javax.swing.*;
import sistemavendas.gerir.*;

public class AdminMenu {
    public static void adminMenu(UsuarioService usuarioService, sistemavendas.estoque.EstoqueService estoqueService, Scanner scanner, String usuarioAtual){
        int escolha;
        while (true) {

            String[] options = {"Gerenciar Estoque", "Visualizar Estoque", "Listar Usuários", "Promover Usuário a Gerente", "Alterar Meu Usuário/Senha", "Logout"};
            escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Menu Administrador",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (escolha) {
                case 0 -> estoqueService.gerenciarEstoque(scanner);
                case 1 -> estoqueService.exibirEstoque();
                case 2 -> usuarioService.listarUsuarios();
                case 3 -> usuarioService.promoverUsuario();  // Apenas admin pode promover
                case 4 -> usuarioService.alterarUsuarioESenha(usuarioAtual);
                case 5 -> {

                    JOptionPane.showMessageDialog(null, "Login Realizado.");
                    // Transferir o conteúdo de estoque_temp.txt para estoque.txt ao sair
                    SistemaVendas.atualizarEstoque();
                    return;  // Retorna ao menu de login
                }
                default -> JOptionPane.showMessageDialog(null, "Opção Inválida!");
            }

            String[] options2 = {"Voltar"};
            escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Menu Administrador",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[0]);

            switch (escolha) {
                case 0 -> {
                return;
                }
            };
        }
    }
}
