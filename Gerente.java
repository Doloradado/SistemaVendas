package sistemavendas.usuarios;

public class Gerente extends Usuario {
    public Gerente(String nome, String senha) {
        super(nome, senha, "gerente");
    }
}
