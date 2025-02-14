package sistemavendas.usuarios;

public class Administrador extends Usuario {
    public Administrador(String nome, String senha) {
        super(nome, senha, "admin");
    }
}
