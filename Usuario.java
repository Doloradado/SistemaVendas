package sistemavendas.usuarios;

public class Usuario {
    protected String nome;
    protected String senha;
    protected String tipo; // "usuario", "gerente" ou "admin"

    public Usuario(String nome, String senha, String tipo) {
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
    }
}
