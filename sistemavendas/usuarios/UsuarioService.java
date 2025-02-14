package sistemavendas.usuarios;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import sistemavendas.gerir.*;

import javax.swing.*;

public class UsuarioService {
    private static final String USERS_FILE = "data/usuarios.txt";

    public String[] login() {
        String usuario = JOptionPane.showInputDialog("Digite seu usuário: ");
        String senha = JOptionPane.showInputDialog("Digite sua senha: ");
        
        String tipoUsuario = autenticarUsuario(usuario, senha);
        if (tipoUsuario != null) {
            JOptionPane.showMessageDialog(null,"Login bem-sucedido como " + tipoUsuario);
            return new String[]{usuario, tipoUsuario};
        } else {
            JOptionPane.showMessageDialog(null,"Usuário ou senha incorretos.");
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
            JOptionPane.showMessageDialog(null,"Erro ao ler arquivo de usuários.");
        }
        return null;
    }

    public void registrarUsuario(Scanner scanner) {
        String usuario = JOptionPane.showInputDialog("Novo usuário: ");
        try {
            Tratador.validarNome(usuario);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,"Erro: " + ex.getMessage());
            return;
        }
        if (verificarUsuarioExistente(usuario)) {
            JOptionPane.showMessageDialog(null,"Erro: usuário já existe!");
            return;
        }
        String senha = JOptionPane.showInputDialog("Digite sua senha: ");
        try {
            Tratador.validarSenha(senha);
        } catch (IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null,"Erro: " + ex.getMessage());
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(usuario + "," + senha + ",usuario\n");
            JOptionPane.showMessageDialog(null,"Usuário registrado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Erro ao registrar usuário.");
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
            JOptionPane.showMessageDialog(null,"Erro ao verificar usuário existente.");
        }
        return false;
    }

    public int validarEntradaNumero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            JOptionPane.showMessageDialog(null,"Entrada inválida, digite um número.");
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
                JOptionPane.showMessageDialog(null,"Arquivo de usuários criado.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Erro ao criar o arquivo de usuários.");
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
                JOptionPane.showMessageDialog(null,"Administrador criado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null,"Administrador já existe no sistema.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Erro ao criar ou adicionar administrador.");
        }
    }
    private static final String USER_FILE = "data/usuarios.txt";

    // Método para listar todos os usuários
    public void listarUsuarios() {
        File arquivoUsuarios = new File(USER_FILE);
        if (arquivoUsuarios.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuarios))) {
                String linha;
                StringBuilder sb = new StringBuilder();
                sb.append("Lista de usuários: ").append("\n\n");
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(",");
                    sb.append("Nome: ").append(dados[0]).append("\n")
                            .append("Tipo: ").append(dados[2]).append("\n\n");
                }
                JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Usuários", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Erro ao listar usuários.");
            }
        } else {
            JOptionPane.showMessageDialog(null,"Nenhum usuário encontrado.");
        }
    }

    // Método para promover um usuário a gerente
    public void promoverUsuario() {
        String nomeUsuario = JOptionPane.showInputDialog("Digite o nome do usuário a ser promovido a gerente:");

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
                        JOptionPane.showMessageDialog(null,"Usuário promovido a gerente com sucesso!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Usuário não encontrado ou já é gerente.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Erro ao promover usuário.");
            }
        } else {
            JOptionPane.showMessageDialog(null,"Arquivo de usuários não encontrado.");
        }
    }

    // Método para alterar o nome de usuário e a senha
    public void alterarUsuarioESenha(String usuarioAtual) {
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
                        String novoNome = JOptionPane.showInputDialog("Digite o novo nome de usuário:");
                        dados[0] = novoNome;

                        // Alterar senha
                        String novaSenha = JOptionPane.showInputDialog("Digite a nova senha:");
                        String confirmarSenha = JOptionPane.showInputDialog("Digite novamente a nova senha:");;

                        if (novaSenha.equals(confirmarSenha)) {
                            dados[1] = novaSenha; // Atualizando a senha
                            usuarioAlterado = true;
                            JOptionPane.showMessageDialog(null,"Usuário e senha alterados com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null,"As senhas não coincidem. A alteração foi cancelada.");
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
                    JOptionPane.showMessageDialog(null,"Usuário não encontrado.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Erro ao alterar usuário e senha.");
            }
        } else {
            JOptionPane.showMessageDialog(null,"Arquivo de usuários não encontrado.");
        }
    }
    }

