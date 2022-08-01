package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dados.Musica;
import dados.Usuario;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import exceptions.UpdateException;

public class UsuarioDAO {
private static UsuarioDAO instance = null;
	
	private PreparedStatement selectNewId;
	private PreparedStatement insert;
	private PreparedStatement delete;
	private PreparedStatement selectLogins;
	private PreparedStatement selectAll;
	private PreparedStatement select;
	private PreparedStatement update;

	public static UsuarioDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
		if(instance==null) instance=new UsuarioDAO();
		return instance;
	}
	
	private UsuarioDAO() throws ClassNotFoundException, SQLException, SelectException{
		Connection conexao = Conexao.getConexao();
		selectNewId = conexao.prepareStatement("select nextval('id_usuario')");
		selectLogins = conexao.prepareStatement("select login from usuario");
		insert =  conexao.prepareStatement("insert into usuario values (?,?,?,?,?)");
		delete = conexao.prepareStatement("delete from usuario where id=?");
		selectAll = conexao.prepareStatement("select * from usuario");
		select = conexao.prepareStatement("select * from usuario where id=?");
		update = conexao.prepareStatement("update usuario set nome=?, login=?, senha=?, cargo=? where id=?");
//		update musica set nome=?, album=?, estilo=? where id=?
	}
	
	private int selectNewId() throws SelectException{
		try {
			ResultSet rs= selectNewId.executeQuery();
			if(rs.next()) return rs.getInt(1);
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar novo ID da tabela usuário");
		}
		return 0;
	}
	
	public void insert(Usuario usuario) throws InsertException, SelectException, JaCadastradoException{
		List<String> lista = selectLogins();
		if(!lista.contains(usuario.getLogin())) {
			try {
				insert.setInt(1, selectNewId());
				insert.setString(2, usuario.getNome());
				insert.setString(3, usuario.getLogin());
				insert.setString(4, usuario.getSenha());
				insert.setString(5, usuario.getCargo());
				insert.executeUpdate();
			}catch (SQLException e) {
				throw new InsertException("Erro ao inserir usuário.");
			}
		}else throw new JaCadastradoException("Usuário já cadastrado");
		
	}
		
	public void delete(int usuario) throws DeleteException, SelectException, NaoCadastradoException{
		select(usuario);
		try {
			delete.setInt(1, usuario);
			delete.executeUpdate();
		}catch(SQLException e) {
			throw new DeleteException("Erro ao deletar usuário");
		}
	}
	
	public List<String> selectLogins() throws SelectException {
		List<String> lista = new ArrayList<String>();
		try {
			ResultSet rs = selectLogins.executeQuery();
			while(rs.next()) {
				lista.add(rs.getString(1));
			}
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar logins dos usuários");
		}
		return lista;
	}
	
	public List<Object> selectAll() throws SelectException {
		List<Object> usuarios = new LinkedList<Object>();
	try {
		ResultSet rs = selectAll.executeQuery();
		while(rs.next()) {
			Object[] linha = {rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5)};
			usuarios.add(linha);
		}
	}catch(SQLException e) {
		throw new SelectException("Erro ao buscar usuários");
	}
	return usuarios;
}

	public Usuario select(int usuario) throws SelectException, NaoCadastradoException {
		try {
			select.setInt(1, usuario);
			ResultSet rs = select.executeQuery();
			if(rs.next()) {
				int id = rs.getInt(1);
				String nome = rs.getString(2);		
				String login = rs.getString(3);	
				String senha = rs.getString(4);	
				String cargo = rs.getString(5);	
				return new Usuario(id, nome, login, senha, cargo);
			}else throw new NaoCadastradoException("Usuário não cadastrado.");
			
		}catch(SQLException e) {
			throw new SelectException("Erro ao buscar usuário");
		}
	}
	
	public void update(Usuario usuario) throws UpdateException, SelectException, NaoCadastradoException{
		try {
			update.setString(1, usuario.getNome());
			update.setString(2, usuario.getLogin());
			update.setString(4, usuario.getCargo());
			update.setString(3, usuario.getSenha());
			update.setInt(5, usuario.getId());
			update.executeUpdate();
		}catch (SQLException e) {
			throw new UpdateException("Erro ao atualizar usuário.");
		}
	}

}
	
