package sistemavendas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import sistemavendas.estoque.ComprarProduto;
import sistemavendas.estoque.EstoqueService;
import sistemavendas.usuarios.UsuarioService;

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

        while (true) {
            if (!usuarioLogado) {
                System.out.println("\n===== Bem vindo a loja =====\n1. Login\n2. Registrar\n3. Sair");
                int escolha = usuarioService.validarEntradaNumero(scanner);

                switch (escolha) {
                    case 1 -> {
                        String[] loginInfo = usuarioService.login(scanner);
                        if (loginInfo != null) {
                            usuarioLogado = true;
                            usuarioAtual = loginInfo[0];
                            tipoUsuario = loginInfo[1];
                        }
                    }
                    case 2 -> usuarioService.registrarUsuario(scanner);
                    case 3 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } else {
                switch (tipoUsuario) {
                    case "admin":
                        // Somente administrador pode promover usuários
                        while (true) {
                            System.out.println("\n===== Menu Administrador =====\n1. Gerenciar Estoque\n2. Visualizar Estoque\n3. Listar Usuários\n4. Promover Usuário a Gerente\n5. Alterar Meu Usuário/Senha\n6. Logout");
                            int escolha = usuarioService.validarEntradaNumero(scanner);
                            
                            switch (escolha) {
                                case 1 -> estoqueService.gerenciarEstoque(scanner);
                                case 2 -> estoqueService.exibirEstoque();
                                case 3 -> usuarioService.listarUsuarios();
                                case 4 -> usuarioService.promoverUsuario(scanner);  // Apenas admin pode promover
                                case 5 -> usuarioService.alterarUsuarioESenha(scanner, usuarioAtual);
                                case 6 -> {
                                    
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
                                break; // Retorna ao menu principal de opções do administrador
                            }
                        }   break;
                    case "gerente":
                        // Gerente não tem a opção de promover usuários
                        while (true) {
                            System.out.println("\n===== Menu gerente =====\n1. Gerenciar Estoque\n2. Visualizar Estoque\n3. Alterar Meu Usuário/Senha\n4. Logout");
                            int escolha = usuarioService.validarEntradaNumero(scanner);
                            
                            switch (escolha) {
                                case 1 -> estoqueService.gerenciarEstoque(scanner);
                                case 2 -> estoqueService.exibirEstoque();
                                case 3 -> usuarioService.alterarUsuarioESenha(scanner, usuarioAtual);
                                case 4 -> {
                                    
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
                        }   break;
                    case "usuario":
                        System.out.println("/n===== Menu usuario =====\n1. Ir ao menu compra\n2. Visualizar Estoque\n3. sair");
                        int escolha = usuarioService.validarEntradaNumero(scanner);
                        switch (escolha) {
                            case 1 -> comprarProduto.menuCompra(scanner, usuarioAtual, tipoUsuario);
                            case 2 -> comprarProduto.visualizarEstoque();
                            case 3 -> {
                                usuarioLogado = false;
                                usuarioAtual = null;
                                tipoUsuario = null;
                                System.out.println("Logout realizado.");
                                // Transferir o conteúdo de estoque_temp.txt para estoque.txt ao sair
                                atualizarEstoque();
                            }
                            default -> System.out.println("Opção inválida.");
                        }   break;
                    default:
                        break;
                }
            }
        }
    }
    // Método para transferir o conteúdo de estoque_temp.txt para estoque.txt
    private static void atualizarEstoque() {
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
    private static void verificarAdministrador(UsuarioService usuarioService) {
        File arquivoUsuarios = new File("data/usuarios.txt");
        if (!arquivoUsuarios.exists() || arquivoUsuarios.length() == 0) {
            System.out.println("Nenhum administrador encontrado. Criando primeiro usuário como ADMIN.");
            usuarioService.registrarAdministrador();
        }
    }

    
}
