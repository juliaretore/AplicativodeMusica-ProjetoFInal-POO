package dados;

public class Usuario {
	private int id;
	private String nome;
	private String login;
	private String senha;
	private String cargo;
	
	public Usuario(int id, String nome, String login, String senha, String cargo) {
		this.setId(id);
		this.setLogin(login);
		this.setNome(nome);
		this.setSenha(senha);
		this.setCargo(cargo);
	}

	public Usuario() {
		
	}
	
	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String toString() {
		//sei que talvez mostrar a senha nos dados não seria muito bom mesmo sendo só para os administradores, mas não tive tempo de fazer algo diferente :(
		return "Usuário "+this.getId()+": Nome "+this.getNome()+" Login "+this.getLogin()+" Senha "+this.getSenha()+" Cargo "+this.getCargo()+"\n";
	}
}
