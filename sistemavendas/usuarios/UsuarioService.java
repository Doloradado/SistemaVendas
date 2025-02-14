package sistemavendas.usuarios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import sistemavendas.gerir.*;

public class UsuarioService {
    private static final String USERS_FILE = "data/usuarios.txt";

    public String[] login(Scanner scanner) {
        System.out.print("Usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        String tipoUsuario = autenticarUsuario(usuario, senha);
        if (tipoUsuario != null) {
            System.out.println("Login bem-sucedido como " + tipoUsuario);
            return new String[]{usuario, tipoUsuario};
        } else {
            System.out.println("Usuário ou senha incorretos.");
            return null;
        }
    }
    

    private String autenticarUsuario(String usuario, String senha) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 3 && partes[0].equals(usuario) && partes[1].equals(senha)) {
                    return partes[2];
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de usuários.");
        }
        return null;
    }

    public void registrarUsuario(Scanner scanner) {
        System.out.print("Novo usuário: ");
        String usuario = scanner.nextLine().trim();
        try {
            Tratador.validarNome(usuario);
        } catch (IllegalArgumentException ex) {
            System.out.println("Erro: " + ex.getMessage());
            return;
        }
        if (verificarUsuarioExistente(usuario)) {
            System.out.println("Erro: Usuário já existe!");
            return;
        }
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();
        try {
            Tratador.validarSenha(senha);
        } catch (IllegalArgumentException ex){
            System.out.println("Erro: " + ex.getMessage());
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(usuario + "," + senha + ",usuario\n");
            System.out.println("Usuário registrado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao registrar usuário.");
        }
    }

    private boolean verificarUsuarioExistente(String usuario) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length >= 1 && partes[0].equals(usuario)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao verificar usuário existente.");
        }
        return false;
    }

    public int validarEntradaNumero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida, digite um número.");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer
        return numero < 0 ? 0 : numero;
    }


   
    // Método para registrar o administrador
    public void registrarAdministrador() {
        File arquivoUsuarios = new File(USER_FILE);
        
        // Verifica se o arquivo existe; caso contrário, cria o arquivo
        if (!arquivoUsuarios.exists()) {
            try {
                // Cria o diretório e o arquivo se não existirem
                arquivoUsuarios.getParentFile().mkdirs();
                arquivoUsuarios.createNewFile();
                System.out.println("Arquivo de usuários criado.");
            } catch (IOException e) {
                System.out.println("Erro ao criar o arquivo de usuários.");
                return;
            }
        }

        // Adiciona o administrador
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoUsuarios, true))) {
            // Dados do administrador (você pode modificar esses valores conforme necessário)
            String nome = "admin";
            String senha = "admin123"; // Use um valor mais seguro para senhas reais
            String tipoUsuario = "admin";

            boolean adminExiste;
            try ( // Verifica se o administrador já está no arquivo
                    BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuarios))) {
                String linha;
                adminExiste = false;
                while ((linha = reader.readLine()) != null) {
                    if (linha.split(",")[0].equals("admin")) {
                        adminExiste = true;
                        break;
                    }
                }
            }

            // Se o administrador não existir, cria um novo
            if (!adminExiste) {
                writer.write(nome + "," + senha + "," + tipoUsuario + "\n");
                System.out.println("Administrador criado com sucesso!");
            } else {
                System.out.println("Administrador já existe no sistema.");
            }

        } catch (IOException e) {
            System.out.println("Erro ao criar ou adicionar administrador.");
        }
    }
    private static final String USER_FILE = "data/usuarios.txt";

    // Método para listar todos os usuários
    public void listarUsuarios() {
        File arquivoUsuarios = new File(USER_FILE);
        if (arquivoUsuarios.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuarios))) {
                String linha;
                System.out.println("Lista de Usuários:");
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(",");
                    System.out.println("Nome: " + dados[0] + ", Tipo: " + dados[2]);
                }
            } catch (IOException e) {
                System.out.println("Erro ao listar usuários.");
            }
        } else {
            System.out.println("Nenhum usuário encontrado.");
        }
    }

    // Método para promover um usuário a gerente
    public void promoverUsuario(Scanner scanner) {
        System.out.println("Digite o nome do usuário a ser promovido a gerente:");
        String nomeUsuario = scanner.nextLine();

        File arquivoUsuarios = new File(USER_FILE);
        if (arquivoUsuarios.exists()) {
            List<String> usuarios = new ArrayList<>();
            boolean usuarioEncontrado = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuarios))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(",");
                    if (dados[0].equals(nomeUsuario) && !dados[2].equals("admin")) {
                        dados[2] = "gerente"; // Promovendo o usuário a gerente
                        usuarioEncontrado = true;
                    }
                    usuarios.add(String.join(",", dados));
                }

                if (usuarioEncontrado) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoUsuarios))) {
                        for (String usuario : usuarios) {
                            writer.write(usuario + "\n");
                        }
                        System.out.println("Usuário promovido a gerente com sucesso!");
                    }
                } else {
                    System.out.println("Usuário não encontrado ou já é gerente.");
                }
            } catch (IOException e) {
                System.out.println("Erro ao promover usuário.");
            }
        } else {
            System.out.println("Arquivo de usuários não encontrado.");
        }
    }

    // Método para alterar o nome de usuário e a senha
    public void alterarUsuarioESenha(Scanner scanner, String usuarioAtual) {
        File arquivoUsuarios = new File(USER_FILE);
        if (arquivoUsuarios.exists()) {
            List<String> usuarios = new ArrayList<>();
            boolean usuarioAlterado = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuarios))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(",");
                    if (dados[0].equals(usuarioAtual)) {
                        // Alterar nome de usuário
                        System.out.println("Digite o novo nome de usuário:");
                        String novoNome = scanner.nextLine();
                        dados[0] = novoNome;

                        // Alterar senha
                        System.out.println("Digite a nova senha:");
                        String novaSenha = scanner.nextLine();
                        System.out.println("Digite novamente a nova senha:");
                        String confirmarSenha = scanner.nextLine();

                        if (novaSenha.equals(confirmarSenha)) {
                            dados[1] = novaSenha; // Atualizando a senha
                            usuarioAlterado = true;
                            System.out.println("Usuário e senha alterados com sucesso!");
                        } else {
                            System.out.println("As senhas não coincidem. A alteração foi cancelada.");
                        }
                    }
                    usuarios.add(String.join(",", dados));
                }

                if (usuarioAlterado) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoUsuarios))) {
                        for (String usuario : usuarios) {
                            writer.write(usuario + "\n");
                        }
                    }
                } else {
                    System.out.println("Usuário não encontrado.");
                }
            } catch (IOException e) {
                System.out.println("Erro ao alterar usuário e senha.");
            }
        } else {
            System.out.println("Arquivo de usuários não encontrado.");
        }
    }
    }

