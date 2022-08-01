package dados;

import java.io.File;

public class Musica {
	private int id;
	private String nome;
	private String album;
	private String estilo;
	private String nomeArquivo;
	private File arq;
	
	public Musica(int id, String nome, String album, String estilo){
		setId(id);
		setNome(nome);
		setAlbum(album);
		setEstilo(estilo);
	}

	public Musica(){

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getEstilo() {
		return estilo;
	}
	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	 public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public File getArq() {
		return arq;
	}

	public void setArq(File arq) {
		this.arq = arq;
	}

	@Override
	 public boolean equals(Object o) {
		if(o instanceof Musica) {
			Musica m = (Musica) o;
			if (this.nome.equals(m.getNome()) && this.album.equals(m.getAlbum()) && this.estilo.equals(m.getEstilo())) return true;
		}
         return false;
	}

}