package persistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dados.Musica;
import dados.Playlist;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class PlaylistDAO {
	private static PlaylistDAO instance = null;
	private PreparedStatement selectNewIdPlaylist;
	private PreparedStatement selectNewIdMusicasPlaylist;
	private PreparedStatement insertPlaylist;
	private PreparedStatement insertMusicaPlaylist;
	private PreparedStatement deletePlaylist;
	private PreparedStatement deleteMusicas;
	private PreparedStatement deleteMusicaPlaylist;
	private PreparedStatement update;
	private PreparedStatement selectPlaylistsUsuario;
	private PreparedStatement selectExcetoNaPlaylist;
	private PreparedStatement selectMusicasPlaylist;
	private PreparedStatement selectNomesPlaylists;
	
	
	public static PlaylistDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new PlaylistDAO();
		return instance;
	}
	
	private PlaylistDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		selectNewIdPlaylist = conexao.prepareStatement("select nextval('id_playlist')");
		selectNewIdMusicasPlaylist = conexao.prepareStatement("select nextval('id_musicas_playlist')");
		insertPlaylist =  conexao.prepareStatement("insert into playlist values (?,?,?)");
		insertMusicaPlaylist =  conexao.prepareStatement("insert into musicas_playlist values (?,?,?)");
		deletePlaylist = conexao.prepareStatement("delete from playlist where id=?");
		deleteMusicas = conexao.prepareStatement("delete from musicas_playlist where id_playlist = ?");	
		deleteMusicaPlaylist = conexao.prepareStatement("delete from musicas_playlist where id_playlist=? and id_musica=?");	
		selectExcetoNaPlaylist = conexao.prepareStatement("select distinct musica.id, musica.nome from musica left join (select * from musicas_playlist where id_playlist=?) AS foo on musica.id=foo.id_musica where foo.id_musica is null");
		selectPlaylistsUsuario = conexao.prepareStatement("select id, nome from playlist where id_usuario=?");
		selectNomesPlaylists = conexao.prepareStatement("select nome from playlist where id_usuario=?");
		selectMusicasPlaylist = conexao.prepareStatement("select musica.id, musica.nome from musica, musicas_playlist where musica.id=musicas_playlist.id_musica and musicas_playlist.id_playlist=?");
		update = conexao.prepareStatement( "update playlist set nome=? where id=?" ) ;

	}
	
	private int selectNewIdMusicasPlaylist() throws SelectException{
		try {
			ResultSet rs= selectNewIdMusicasPlaylist.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da tabela musicas_playlist");
		}
		return 0;
	}
	
	private int selectNewIdPlaylist() throws SelectException{
		try {
			ResultSet rs= selectNewIdPlaylist.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da tabela playlist");
		}
		return 0;
	}
	
	public void insertPlaylist(Playlist playlist, int usuario) throws InsertException, SelectException, JaCadastradoException{
		List<String> lista = selectNomesPlaylists(usuario);
		if(!lista.contains(playlist.getNome())) {
			try {
				insertPlaylist.setInt(1, selectNewIdPlaylist());
				insertPlaylist.setString(2, playlist.getNome());
				insertPlaylist.setInt(3, usuario);
				insertPlaylist.executeUpdate();
			}catch (SQLException e) {
				throw new InsertException("Erro ao inserir playlist.");
			}
		}else throw new JaCadastradoException("Playlist já cadastrada.");
	}
	
	public void insertMusicaPlaylist(int musica, int playlist) throws InsertException, SelectException, NaoCadastradoException{
		try {
			insertMusicaPlaylist.setInt(1, selectNewIdMusicasPlaylist());
			insertMusicaPlaylist.setInt(2, musica);
			insertMusicaPlaylist.setInt(3, playlist);
			insertMusicaPlaylist.executeUpdate();
		}catch (SQLException e) {
			throw new InsertException("Erro ao inserir música na playlist.");
		}
	}
		
	public void deletePlaylist(int playlist) throws DeleteException, SelectException, NaoCadastradoException{
		deleteMusicas(playlist);
		try {
			deletePlaylist.setInt(1, playlist);
			deletePlaylist.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar playlist");
		}
		
	}
	
	public void deleteMusicas(int playlist) throws DeleteException{		
		try {
			deleteMusicas.setInt(1, playlist);
			deleteMusicas.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar músicas da playlist");
		}
	}

	public void deleteMusicaPlaylist(int playlist, int musica) throws DeleteException, SelectException, NaoCadastradoException{
		try {
			deleteMusicaPlaylist.setInt(1, playlist);
			deleteMusicaPlaylist.setInt(2, musica);
			deleteMusicaPlaylist.executeUpdate();	
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar música da playlist");
		}
	}
	
	public List<Object> selectMusicasPlaylist(int playlist) throws SelectException {
		List<Object> musicas = new ArrayList<Object>();
		try {
			selectMusicasPlaylist.setInt(1, playlist);
			ResultSet rs = selectMusicasPlaylist.executeQuery();
			
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				musicas.add(linha);
			}
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar playlist");
		}
		return musicas;
	}
	
	public List<Object> selectPlaylistsUsuario(int usuario) throws SelectException {
		 List<Object> playlists = new ArrayList<Object>();
		try {
			selectPlaylistsUsuario.setInt(1, usuario);
			ResultSet rs = selectPlaylistsUsuario.executeQuery();
			
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				playlists.add(linha);
			}
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar playlist");
		}
		return playlists;
	}
	
	public List<Object>selectExcetoNaPlaylist(int playlist) throws SelectException {
		List<Object> musicas = new ArrayList<Object>();
		try {
			selectExcetoNaPlaylist.setInt(1, playlist);
			ResultSet rs = selectExcetoNaPlaylist.executeQuery();
			
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				musicas.add(linha);
			}
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar músicas para adicionar a playlist");
		}
		return musicas;
	}

	public List<String> selectNomesPlaylists(int usuario) throws SelectException {
		 List<String> nomeMusicas = new ArrayList<String>();
		try {
			selectNomesPlaylists.setInt(1, usuario);
			ResultSet rs = selectNomesPlaylists.executeQuery();
			while(rs.next()) {
				nomeMusicas.add(rs.getString(1));
			}
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar nomes das playlist");
		}
		return nomeMusicas;
	}
	
	public void update(Playlist playlist) throws UpdateException, SelectException, NaoCadastradoException{
		try {
			update.setString(1, playlist.getNome());
			update.setInt(2, playlist.getId());
			update.executeUpdate();
		}catch (SQLException e) {
			throw new UpdateException("Erro ao atualizar música.");
		}
	}
}
	

