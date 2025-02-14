package sistemavendas.estoque;

public class Produto {
    private final String nomeProduto;
    private int quantidade;
    private double preco;
	private String categoria;
	private String marca;
	
    
	//Construtor
    public Produto(String nomeProduto, int quantidade, double preco, String categoria, String marca) {
        this.nomeProduto = nomeProduto;
        this.quantidade = Math.max(0, quantidade);
        this.preco = Math.max(0.0, preco);
		this.categoria = categoria;
		this.marca = marca;
		
    }
    //Metodos Setters
    public String getNomeProduto() {
        return nomeProduto;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public double getPreco() {
        return preco;
    }
	public String getCategoria(){
		return categoria;
	}
	public String getMarca(){
		return marca;
	}

	
	
    //Metodos getters 
    public void setQuantidade(int quantidade) {
        this.quantidade = Math.max(0, quantidade);
    }

    public void setPreco(double preco) {
        this.preco = Math.max(0.0, preco);
    }
	
	public void setCategoria(String categoria){
		 if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia!");
        }
        this.categoria = categoria;
	}	
	public void setMarca(String marca){
		if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("Marca não pode ser vazia!");
        }
        this.marca = marca;
	}
	
}
