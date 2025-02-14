package sistemavendas.gerir;


    public class Tratador {

    // Valida nome de usuário: não pode ser vazio e deve conter apenas letras e números.
    public static void validarNome(String nome) throws IllegalArgumentException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário não pode ser vazio.");
        }
        if (!nome.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Nome de usuário deve conter apenas letras e números.");
        }
    }

    // Valida senha: não pode ser vazia.
    public static void validarSenha(String senha) throws  IllegalArgumentException {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }
    }

    // Valida nome do produto: não pode ser vazio e deve conter apenas letras e espaços.
    public static void validarNomeProduto(String nomeProduto) throws IllegalArgumentException{
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (!nomeProduto.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Nome do produto deve conter apenas letras e espaços.");
        }
    }

    // Valida quantidade: não pode ser vazia e deve conter apenas dígitos.
    public static void validarQuantidade(String quantidade) throws IllegalArgumentException{
        if (quantidade == null || quantidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Quantidade não pode ser vazia.");
        }
        if (!quantidade.matches("\\d+")) {
            throw new IllegalArgumentException("Quantidade deve conter apenas números.");
        }
    }

    // Valida preço: não pode ser vazio e deve ser um número válido (ex.: 10 ou 10.99).
    public static void validarPreco(String preco) throws IllegalArgumentException{
        if (preco == null || preco.trim().isEmpty()) {
            throw new IllegalArgumentException("Preço não pode ser vazio.");
        }
        if (!preco.matches("\\d+(\\.\\d{1,2})?")) {
            throw new IllegalArgumentException("Preço deve ser um número válido (ex: 10 ou 10.99).");
        }
    }

    // Valida categoria: não pode ser vazia e deve conter apenas letras e espaços.
    public static void validarCategoria(String categoria) throws IllegalArgumentException {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia.");
        }
        if (!categoria.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Categoria deve conter apenas letras e espaços.");
        }
    }

    // Valida marca: não pode ser vazia e deve conter apenas letras e espaços.
    public static void validarMarca(String marca)  throws IllegalArgumentException{
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("Marca não pode ser vazia.");
        }
        if (!marca.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Marca deve conter apenas letras e espaços.");
        }
    }
}