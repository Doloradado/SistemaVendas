package sistemavendas.usuarios;

import javax.swing.*;
import java.util.Scanner;

import static sistemavendas.SistemaVendas.atualizarEstoque;

public class GerenteMenu {
    public static void gerenteMenu (UsuarioService usuarioService, sistemavendas.estoque.EstoqueService estoqueService, Scanner scanner, String usuarioAtual)
    {
        // Gerente não tem a opção de promover usuários
        int escolha;
        while (true) {
            String[] options = {"Gerenciar Estoque", "Visualizar Estoque", "Alterar Meu Usuário/Senha", "Logout"};
            escolha = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Menu Gerente",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            escolha = usuarioService.validarEntradaNumero(scanner);

            switch (escolha) {
                case 0 -> estoqueService.gerenciarEstoque(scanner);
                case 1 -> estoqueService.exibirEstoque();
                case 2 -> usuarioService.alterarUsuarioESenha(scanner, usuarioAtual);
                case 3 -> {

                    System.out.println("Logout realizado.");
                    // Transferir o conteúdo de estoque_temp.txt para estoque.txt ao sair
                    atualizarEstoque();
                    return;  // Retorna ao menu de login
                }
                default -> System.out.println("Opção inválida.");
            }

            // Opção para retornar ao menu anterior
            System.out.println("\nPressione 0 para voltar ao menu anterior.");
            int voltar = usuarioService.validarEntradaNumero(scanner);
            if (voltar == 0) {
                break; // Retorna ao menu principal de opções do gerente
            }
        }
    }
}
