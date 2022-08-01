package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import dados.Artista;
import dados.Musica;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class ArtistaDAO {
	private static ArtistaDAO instance = null;
	private PreparedStatement selectNewId;
	private PreparedStatement selectAll;
	private PreparedStatement select;
	private PreparedStatement insert;
	private PreparedStatement delete;
	private PreparedStatement deleteArtistaMusica;
	private PreparedStatement update;
	private PreparedStatement selectExcetoMusicasArtista;
	private PreparedStatement selectMusicasArtista;
	private PreparedStatement selectAllDados;
	
	public static ArtistaDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new ArtistaDAO();
		return instance;
	}
	
	private ArtistaDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		selectNewId = conexao.prepareStatement("select nextval('id_artista')");
		insert =  conexao.prepareStatement("insert into artista values (?,?)");
		select = conexao.prepareStatement("select * from artista where id=?");
		selectAll = conexao.prepareStatement("select * from artista");
		selectAllDados = conexao.prepareStatement("select nome from artista");
		selectExcetoMusicasArtista=conexao.prepareStatement("select distinct musica.id, musica.nome from musica left join (select * from artistas_musica where id_artista=?) AS foo on musica.id=foo.id_musica where foo.id_musica is null");
		selectMusicasArtista=conexao.prepareStatement("select distinct musica.id, musica.nome from musica, artistas_musica where musica.id=artistas_musica.id_musica and artistas_musica.id_artista=?");
		update = conexao.prepareStatement( "update artista set nome=? where id=?" ) ;
		delete = conexao.prepareStatement("delete from artista where id = ?");
		deleteArtistaMusica = conexao.prepareStatement("delete from artistas_musica where id_artista= ?");			
	}
	
	public int selectNewId() throws SelectException{
		try {
			ResultSet rs=selectNewId.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID do artista");
		}
		return 0;
	}
	
	public void insert(Artista artista) throws InsertException, SelectException, JaCadastradoException{
		List<String> artistas = selectAllDados();
		if(!artistas.contains(artista.getNome())) {
			try {
				insert.setInt(1, selectNewId());
				insert.setString(2, artista.getNome());
				insert.executeUpdate();
			}catch (SQLException e) {
				throw new InsertException("Erro ao inserir artista.");
			}
		}else throw new JaCadastradoException("Artista já castrado!");
		
	}
	
	public List<Object> selectAll() throws SelectException {
		List<Object> artistas = new LinkedList<Object>();
		try {
			ResultSet rs = selectAll.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				artistas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar artista");
		}
		return artistas;
	}
	
	public List<String> selectAllDados() throws SelectException {
		List<String> artistas = new LinkedList<String>();
		try {
			ResultSet rs = selectAllDados.executeQuery();
			while(rs.next()) {
				artistas.add(rs.getString(1));
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar dados do artista");
		}
		return artistas;
	}
	
	public List<Object> selectExcetoMusicasArtista(int artista) throws SelectException {
		List<Object> musicas = new LinkedList<Object>();
		try {
			selectExcetoMusicasArtista.setInt(1, artista);
			ResultSet rs = selectExcetoMusicasArtista.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				musicas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar músicas para adiconar ao artista");
		}
		return musicas;
	}	
		
	public List<Object> selectMusicasArtista(int artista) throws SelectException {
		List<Object> musicas = new LinkedList<Object>();
		try {
			selectMusicasArtista.setInt(1, artista);
			ResultSet rs = selectMusicasArtista.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				musicas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar músicas do artista");
		}
		return musicas;
	}
	
	public void update(Artista artista) throws UpdateException, NaoCadastradoException, SelectException{
		select(artista.getId());
			try {
				update.setString(1, artista.getNome());
				update.setInt(2, artista.getId());
				update.executeUpdate();
			}catch (SQLException e) {
				throw new UpdateException("Erro ao atualizar artista.");
			}		
	}
		
	public void delete(int artista) throws DeleteException, SelectException, NaoCadastradoException{
		Artista a = select(artista);
		deleteArtistaMusica(artista);
		try {
			delete.setInt(1, artista);
			delete.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar artista");
		}
	}
	
	public void deleteArtistaMusica(int artista) throws DeleteException{
		try {
			deleteArtistaMusica.setInt(1, artista);
			deleteArtistaMusica.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar músicas do artista");
		}
	}
	
	public Artista select(int artista) throws SelectException, NaoCadastradoException {
		try {
			select.setInt(1, artista);
			ResultSet rs = select.executeQuery();
			if(rs.next()) {
				int id = rs.getInt(1);
				String nome = rs.getString(2);
				return new Artista(id, nome);
			}else throw new NaoCadastradoException("Artista não cadastrado.");
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar artista");
		}
	}
}



