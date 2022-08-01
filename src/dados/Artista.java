package dados;

public class Artista {
	private int id;
	private String nome;
		
	public Artista() {
		
	}
	
	public Artista(int id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public  String toString(){
		String str="Artista: " +this.nome+" ID: "+this.id;
		return str;	
	}
		
}
