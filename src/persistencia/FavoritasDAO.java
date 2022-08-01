package persistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import dados.Musica;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;

public class FavoritasDAO {
	private static FavoritasDAO instance = null;
	
	private PreparedStatement selectNewId;
	private PreparedStatement selectAll;
	private PreparedStatement selectIds;
	private PreparedStatement insert;
	private PreparedStatement delete;
	private PreparedStatement selectExcetoFavoritas;
	
	public static FavoritasDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new FavoritasDAO();
		return instance;
	}
	
	private FavoritasDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		selectNewId = conexao.prepareStatement("select nextval('id_musicas_favoritas')");
		insert =  conexao.prepareStatement("insert into musicas_favoritas values (?,?,?)");
		selectIds = conexao.prepareStatement("select id_musica from musicas_favoritas where id_usuario=?");
		selectAll = conexao.prepareStatement("select distinct musica.id, musica.nome from musica, musicas_favoritas, usuario where musicas_favoritas.id_usuario=? and musicas_favoritas.id_musica=musica.id");
		selectExcetoFavoritas = conexao.prepareStatement("select distinct musica.id, musica.nome from musica left join (select * from musicas_favoritas where id_usuario=?) AS foo on musica.id=foo.id_musica where foo.id_musica is null");
		delete = conexao.prepareStatement("delete from musicas_favoritas where id_musica=? and id_usuario=?");
	}
	
	private int selectNewId() throws SelectException{
		try {
			ResultSet rs= selectNewId.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da tabela musicas favoritas");
		}
		return 0;
	}
	
	public void insert(int musica, int usuario) throws InsertException, SelectException, JaCadastradoException{
//		List<Integer> musicas = selectIds(usuario);
//		if(!musicas.contains(musica)) {
			try {
				insert.setInt(1, selectNewId());
				insert.setInt(2, musica);
				insert.setInt(3, usuario);
				insert.executeUpdate();
			}catch (SQLException e) {
				throw new InsertException("Erro ao inserir musica favorita.");
			}
//		}else throw new JaCadastradoException("Música favorita já cadastrada.");
	}
		
	public List<Object> selectAll(int usuario) throws SelectException {
		List<Object> musicas = new LinkedList<Object>();
		try {
			selectAll.setInt(1, usuario);
			ResultSet rs = selectAll.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				musicas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar musicas favoritas");
		}
		return musicas;
	}
	
	public List<Object> selectExcetoFavoritas(int usuario) throws SelectException {
		List<Object> musicas = new LinkedList<Object>();
		try {
			selectExcetoFavoritas.setInt(1, usuario);
			ResultSet rs = selectExcetoFavoritas.executeQuery();
			while(rs.next()) {
				Object[] linha = {rs.getInt(1), rs.getString(2)};
				musicas.add(linha);
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar artista");
		}
		return musicas;
	}
			
	public void delete(int musica, int usuario) throws DeleteException, SelectException, NaoCadastradoException{
//		List<Integer> musicas = selectIds(usuario);
//		if(musicas.contains(musica)) {
			try {
				delete.setInt(1, musica);
				delete.setInt(2, usuario);
				delete.executeUpdate();
			}catch(SQLException e) {
				throw new DeleteException("Erro ao deletar música favorita");
			}
//		}else throw new NaoCadastradoException("Música favorita não cadastrada.");

	}
	
//	public List<Integer> selectIds(int usuario) throws SelectException{
//		List<Integer> IDsMusicas = new LinkedList<Integer>();
//		try {
//			selectIds.setInt(1, usuario);
//			ResultSet rs = selectIds.executeQuery();
//			while(rs.next()) {
//				IDsMusicas.add(rs.getInt(1));
//			}
//		}catch(SQLException e) {
//			throw new SelectException("Erro ao buscar música favorita");
//		}
//		return IDsMusicas;
//	}
}
