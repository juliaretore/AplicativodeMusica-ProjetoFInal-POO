package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dados.Usuario;
import exceptions.SelectException;

public class LoginDAO {
		private static LoginDAO instance = null;
		private PreparedStatement listaUsuario;
		private PreparedStatement validarSenha;
		private PreparedStatement retornarUsuario;
		
		public static LoginDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
			if(instance==null) instance=new LoginDAO();
			return instance;
			
		}
		
		public LoginDAO() throws ClassNotFoundException, SQLException, SelectException{
			Connection conexao = Conexao.getConexao();
			listaUsuario = conexao.prepareStatement("SELECT login FROM usuario");
			validarSenha =  conexao.prepareStatement("SELECT senha FROM usuario WHERE login=?");
			retornarUsuario = conexao.prepareStatement("SELECT * FROM usuario WHERE login=?");
		}	
	

	
	public List<String> listaUsuario() throws SQLException, SelectException{
		List<String> login = new ArrayList<String>();
		try {
			ResultSet rs = listaUsuario.executeQuery();
			while (rs.next()) {
				login.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new SelectException("Erro ao buscar usuários cadastrados");
		}
		return login;
	}
	

	public List<String> validarSenha(String login) throws SQLException, SelectException{
		List<String> senha = new ArrayList<String>();
		try {
			validarSenha.setString(1, login);
			ResultSet rs= validarSenha.executeQuery();		
			while (rs.next()) {
				senha.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new SelectException("Erro ao buscar senha");
		}
		return senha;
	}
		
	public Usuario retornarUsuario(String login) throws SQLException, SelectException {	
		Usuario usuario = null;
		try {
			retornarUsuario.setString(1, login);
			ResultSet rs= retornarUsuario.executeQuery();		
			while (rs.next()) {
				int id=rs.getInt(1);
				String nome = rs.getString(2);
				String login2 = rs.getString(3);
				String senha = rs.getString(4);
				String cargo = rs.getString(5);
				return new Usuario(id, nome, login2,senha,cargo);
			}
		} catch (SQLException e) {
			throw new SelectException("Erro ao buscar usuário no login");
		}			
		return usuario;
	}
			
}
