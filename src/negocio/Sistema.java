package negocio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import persistencia.*;
import java.util.List;
import dados.*;
import exceptions.ArquivoNaoEncontradoException;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.LoginIncorretoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class Sistema {
	private static ArtistaDAO artistaDAO;
	private static MusicaDAO musicaDAO;
	private static LoginDAO loginDAO;
	private static FavoritasDAO favoritasDAO;
	private static PlaylistDAO playlistDAO;
	private static UsuarioDAO usuarioDAO;
	
	public Sistema() throws ClassNotFoundException, SQLException, SelectException{
		artistaDAO = ArtistaDAO.getInstance();
		musicaDAO = MusicaDAO.getInstance();
		loginDAO = LoginDAO.getInstance();
		favoritasDAO = FavoritasDAO.getInstance();
		playlistDAO = PlaylistDAO.getInstance();
		usuarioDAO= UsuarioDAO.getInstance();
	}
	
	
	//LOGIN
	public Usuario validacaoLogin(String login, String senha) throws SQLException, SelectException, LoginIncorretoException {
		List<String> logins = loginDAO.listaUsuario();
		if(logins.contains(login)) {
			List<String> senhaLogin = loginDAO.validarSenha(login);
			if(senhaLogin.contains(senha)) {
				return loginDAO.retornarUsuario(login);
			}else throw new LoginIncorretoException("Senha incorreta! Tente novamente");
			
		}else throw new LoginIncorretoException("Usuario n�o encontrado! Tente novamente");
	}
	

	//ARTISTA
	public List<Object> listarMusicasArtistas(int artista) throws SelectException{
		return artistaDAO.selectMusicasArtista(artista);
	}
		
	public List<Object> listarArtistas() throws SelectException{
		return artistaDAO.selectAll();
	}
			
	public void cadastrarArtista(Artista artista) throws InsertException, SelectException, JaCadastradoException{
		artistaDAO.insert(artista);
	}
	
	public void excluirArtista(int artista) throws DeleteException, SelectException, NaoCadastradoException {
		artistaDAO.delete(artista);
	}
	
	public void alterarArtista(Artista artista) throws UpdateException, NaoCadastradoException, SelectException {
		artistaDAO.update(artista);
	}
	
	public List<Object> listarMusicasArtistaAdicionar(int artista) throws SelectException {
		return artistaDAO.selectExcetoMusicasArtista(artista);
	}
	
	public void adicionarMusicasArtistas(int artista, int musica) throws InsertException, SelectException, NaoCadastradoException {
			musicaDAO.insertArtista(musica, artista);
	}	
	
	
	//MUSICA

	public List<Object> listarMusicas() throws SelectException{
		return musicaDAO.selectAll();
	}
		
	public void uploadMusicas(Musica musica) throws InsertException, SelectException, JaCadastradoException, IOException, ArquivoNaoEncontradoException {
		musicaDAO.insert(musica);
	}
	
	public void adicionarArtistasMusica(int artista, int musica) throws InsertException, SelectException, NaoCadastradoException {
		musicaDAO.insertArtista(musica, artista);
	}

	public void removerArtistaMusica(int musica, int artista) throws DeleteException {
		musicaDAO.deleteArtistaMusica(musica, artista);
	}
	
	public void excluirMusica(int musica) throws DeleteException, SelectException, NaoCadastradoException {
		musicaDAO.delete(musica);
	}

	public List<Object> listarArtistasMusicaAdiconar(int musica) throws SelectException {
		return musicaDAO.selectExcetoArtistasMusica(musica);
	}

	public List<Object> listarArtistasMusica(int musica) throws SelectException {
		return  musicaDAO.selectArtistasMusica(musica);
	}
		
	public void alterarMusica(Musica musica) throws UpdateException, SelectException, NaoCadastradoException {
		musicaDAO.update(musica);
	}
	
	public String buscarArquivo(int musica) throws SelectException {
		return musicaDAO.selectArquivo(musica);
	}
	
	
	
	//PLAYLIST
 	public void criarNovaPlaylist(Playlist playlist, int usuario) throws InsertException, SelectException, JaCadastradoException {
		playlistDAO.insertPlaylist(playlist, usuario);
	}
	
	public void excluirPlaylist(int playlist) throws DeleteException, SelectException, NaoCadastradoException {
		playlistDAO.deletePlaylist(playlist);
	}
	
	public List<Object> listarPlaylists(int usuario) throws SelectException {
		return playlistDAO.selectPlaylistsUsuario(usuario);	
	}
	
	public List<Object> listarMusicasPlaylist(int playlist) throws SelectException {
		return playlistDAO.selectMusicasPlaylist(playlist);
	}
	
	public void adicionarMusicasPlaylist(int musica, int playlist) throws InsertException, SelectException, NaoCadastradoException {
		playlistDAO.insertMusicaPlaylist(musica, playlist);
	}

	public List<Object> listarMusicasAdiconarPlaylist(int playlist) throws SelectException {
		return playlistDAO.selectExcetoNaPlaylist(playlist);
	}
	
	public void removerMusicasPlaylist(int playlist, int musica) throws DeleteException, SelectException, NaoCadastradoException {
		playlistDAO.deleteMusicaPlaylist(playlist,musica);
	}
	
	public void alterarPlaylist(Playlist playlist) throws UpdateException, SelectException, NaoCadastradoException {
		playlistDAO.update(playlist);
	}

	
	
	//MUSICAS FAVORITAS
	public List<Object> listarMusicasFavoritas(int usuario) throws SelectException {
		return favoritasDAO.selectAll(usuario);
	}	

	public List<Object> listarExcetoMusicasFavoritas(int usuario) throws SelectException {
		return favoritasDAO.selectExcetoFavoritas(usuario);
	}
	
	public void removerMusicaFavorita(int musica, int usuario) throws DeleteException, SelectException, NaoCadastradoException {
		favoritasDAO.delete(musica, usuario);
	}
	
	public void adicionarMusicaFavorita(int musica, int usuario) throws InsertException, SelectException, JaCadastradoException {
		favoritasDAO.insert(musica, usuario);
	}	
	
	
	//USUARIO
	public void adicionarUsuario(Usuario usuario) throws InsertException, SelectException, JaCadastradoException {
		usuarioDAO.insert(usuario);
	}
	
	public List<Object> listarUsuarios() throws SelectException{
		return usuarioDAO.selectAll();
	}
	
	public void excluirUsuario(int usuario) throws DeleteException, SelectException, NaoCadastradoException {
		usuarioDAO.delete(usuario);
	}
	
	public void alterarUsuario(Usuario usuario) throws UpdateException, SelectException, NaoCadastradoException {
		usuarioDAO.update(usuario);
	}
	
	
	
	//Pr�via m�sica
	public void tocarPreviaMusica(Musica m) {
		try {
			FileInputStream fileInputStream = new FileInputStream(m.getNome()+".mp3");
			Player player = new Player(fileInputStream);
			System.out.println("Song is playing...");
				player.play(300);
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (JavaLayerException e) {
				e.printStackTrace();
			}
	}


}
