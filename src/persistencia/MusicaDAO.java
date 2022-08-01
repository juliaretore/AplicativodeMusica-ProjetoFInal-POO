package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import dados.Musica;
import exceptions.ArquivoNaoEncontradoException;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class MusicaDAO {
	private static MusicaDAO instance = null;
	private PreparedStatement selectNewId;
	private PreparedStatement selectAll;
	private PreparedStatement insert;
	private PreparedStatement insertArtista;
	private PreparedStatement delete;
	private PreparedStatement update;
	private PreparedStatement deleteArtistaMusica;
	private PreparedStatement deleteMusicaPlaylist;
	private PreparedStatement deleteMusicaFavorita;
	private PreparedStatement deleteTodosArtistasMusica;
	private PreparedStatement select;
	private PreparedStatement selectArtistasMusica;
	private PreparedStatement selectExcetoArtistasMusica;
	private PreparedStatement selectAllDados;
	private PreparedStatement selectArquivo;

	
	public static MusicaDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new MusicaDAO();
		return instance;
	}
	
	private MusicaDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		selectNewId = conexao.prepareStatement("select nextval('id_musica')");
		select = conexao.prepareStatement("select * from musica where id=?");
		selectAll = conexao.prepareStatement("select * from musica");
		selectAllDados = conexao.prepareStatement("select nome, album, estilo from musica");
		selectExcetoArtistasMusica = conexao.prepareStatement("select distinct artista.id, artista.nome from artista left join (select * from artistas_musica where id_musica=?) AS foo on artista.id=foo.id_artista where foo.id_artista is null");
		selectArtistasMusica = conexao.prepareStatement("select distinct artista.id, artista.nome from artista, artistas_musica where artista.id=artistas_musica.id_artista and artistas_musica.id_musica=?");
		insertArtista = conexao.prepareStatement("insert into artistas_musica values (nextval('id_artistas_musica'),?,?)");
		update = conexao.prepareStatement( "update musica set nome=?, album=?, estilo=? where id=?") ;
		delete = conexao.prepareStatement("delete from musica where id=?");
		deleteTodosArtistasMusica = conexao.prepareStatement("delete from artistas_musica where id_musica=?");
		deleteMusicaPlaylist = conexao.prepareStatement("delete from musicas_playlist where id_musica=?");	
		deleteMusicaFavorita = conexao.prepareStatement("delete from musicas_favoritas where id_musica=?");	
		deleteArtistaMusica = conexao.prepareStatement("delete from artistas_musica where id_musica=? and id_artista=?");
		selectArquivo = conexao.prepareStatement("SELECT nomearq FROM musica WHERE id=?");
		insert =  conexao.prepareStatement("insert into musica values (?,?,?,?,?,?)");
		
	}
	
	private int selectNewId() throws SelectException{
		try {
			ResultSet rs= selectNewId.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da música");
		}
		return 0;
	}
	
	public void insert(Musica musica) throws InsertException, SelectException, JaCadastradoException, IOException, ArquivoNaoEncontradoException{
		List<Musica> musicas = selectAllDados();
		if(!musicas.contains(musica)) {
			try {
				File file = new File("arq/"+musica.getNome()+"-"+musica.getAlbum()+"-"+musica.getEstilo()+".mp3");
				FileInputStream fis = new FileInputStream(file);

				if(file.exists()) {
					insert.setInt(1, selectNewId());
					insert.setString(2, musica.getNome());
					insert.setString(3, musica.getAlbum());
					insert.setString(4, musica.getEstilo());
					insert.setString(5, file.getName());
					insert.setBinaryStream(6, fis, file.length());	
					insert.executeUpdate();
					insert.close();
					fis.close();
				}else throw new ArquivoNaoEncontradoException();		
			}catch (SQLException e) {
				throw new InsertException("Erro ao inserir música.");
			}
		}else throw new JaCadastradoException("Música já cadastrada!");
		
	}
	
	public void insertArtista(int musica, int artista) throws InsertException, SelectException{
		try {
			insertArtista.setInt(1, musica);
			insertArtista.setInt(2, artista);
			insertArtista.executeUpdate();
		}catch (SQLException e) {
			throw new InsertException("Erro ao inserir artista da música.");
		}
	}
	
	public List<Object> selectAll() throws SelectException {
		List<Object> musicas = new LinkedList<Object>();
		try {
			ResultSet rs = selectAll.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4)};
				musicas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar músicas");
		}
		return musicas;
	}

	public List<Object> selectExcetoArtistasMusica(int musica) throws SelectException {
		List<Object> artistas = new LinkedList<Object>();
		try {
			selectExcetoArtistasMusica.setInt(1, musica);
			ResultSet rs = selectExcetoArtistasMusica.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				artistas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar artistas para adiconar a música");
		}
		return artistas; 
	}
	
	public String selectArquivo(int musica) throws SelectException {
		try {
			selectArquivo.setInt(1, musica);
			ResultSet rs = selectArquivo.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar arquivo de música");
		}
		return null;
	}
	
	public List<Object> selectArtistasMusica(int musica) throws SelectException {
		List<Object> artistas = new LinkedList<Object>();
		try {
			selectArtistasMusica.setInt(1, musica);
			ResultSet rs = selectArtistasMusica.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				artistas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar artistas da música");
		}
		return artistas;
	}
	
	public List<Musica> selectAllDados() throws SelectException {
		List<Musica> musicas = new ArrayList<Musica>();
		try {
			ResultSet rs = selectAllDados.executeQuery();
			while(rs.next()) {
				musicas.add(new Musica(0, rs.getString(1), rs.getString(2), rs.getString(3)));
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar dados da música");
		}
		return musicas;
	}
	
	public void update(Musica musica) throws UpdateException, SelectException, NaoCadastradoException{
		select(musica.getId());
		try {
			update.setString(1, musica.getNome());
			update.setString(2, musica.getAlbum());
			update.setString(3, musica.getEstilo());
			update.setInt(4, musica.getId());
			update.executeUpdate();
		}catch (SQLException e) {
			throw new UpdateException("Erro ao atualizar música.");
		}
	}

	public void delete(int IDmusica) throws DeleteException, SelectException, NaoCadastradoException{
		select(IDmusica);
		deleteTodosArtistasMusica(IDmusica);
		deleteMusicaPlaylist(IDmusica);
		deleteMusicaFavorita(IDmusica);
		try {
			delete.setInt(1, IDmusica);
			delete.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar música");
		}
	}
	
	public void deleteMusicaPlaylist(int musica) throws DeleteException{
		try {
			deleteMusicaPlaylist.setInt(1, musica);
			deleteMusicaPlaylist.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar música das playlists");
		}
	}
	
	public void deleteMusicaFavorita(int musica) throws DeleteException{
		try {
			deleteMusicaFavorita.setInt(1, musica);
			deleteMusicaFavorita.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar essa música das músicas favoritas");
		}
	}
	
	public void deleteTodosArtistasMusica(int musica) throws DeleteException{
		try {
			deleteTodosArtistasMusica.setInt(1, musica);
			deleteTodosArtistasMusica.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar músicas do artista");
		}
	}
	
	public void deleteArtistaMusica(int musica, int artista) throws DeleteException{
		try {
			deleteArtistaMusica.setInt(1, musica);
			deleteArtistaMusica.setInt(2, artista);
			deleteArtistaMusica.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar artista da música.");
		}
	}
	
	public Musica select(int musica) throws SelectException, NaoCadastradoException {
		try {
			select.setInt(1, musica);
			ResultSet rs = select.executeQuery();
			if(rs.next()) {
				int id = rs.getInt(1);
				String nome = rs.getString(2);
				String album = rs.getString(3);
				String estilo = rs.getString(4);
				return new Musica(id, nome, album, estilo);
			}else throw new NaoCadastradoException("Música não cadastrada!");	
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar música");
		}
	}
}
