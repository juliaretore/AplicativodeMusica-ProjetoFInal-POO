 package apresentacao;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import persistencia.LoginDAO;
import dados.Usuario;
import exceptions.LoginIncorretoException;
import exceptions.SelectException;
import negocio.Sistema;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.border.LineBorder;

public class LoginView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tflogin;
	private static Sistema sistema;
	private JLabel lblNewLabel;
	private JPasswordField tfsenha;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);

					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
//					e.printStackTrace();
				}
			}
		});
	}

	public LoginView() {
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		
		lblNewLabel = new JLabel("New label");
		ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/img/login.jpg"));  
		lblNewLabel.setIcon(img);
        img.setImage(img.getImage().getScaledInstance(445, 250, 100));
		
		setTitle("Login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 256);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		tflogin = new JTextField();
		tflogin.setBorder(UIManager.getBorder("Tree.editorBorder"));
		tflogin.setBounds(78, 44, 133, 23);
		contentPane.add(tflogin);
		tflogin.setColumns(10);
		
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnSair.setForeground(Color.BLACK);
		btnSair.setBackground(Color.WHITE);
		btnSair.setBounds(314, 183, 110, 23);
		contentPane.add(btnSair);
		
		tfsenha = new JPasswordField();
		tfsenha.setBorder(new LineBorder(new Color(0, 0, 0)));
		tfsenha.setBounds(78, 79, 133, 22);
		contentPane.add(tfsenha);

		
		JLabel lblSenha_1 = new JLabel("Senha");
		lblSenha_1.setForeground(new Color(255, 102, 51));
		lblSenha_1.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblSenha_1.setBackground(Color.WHITE);
		lblSenha_1.setBounds(29, 75, 89, 23);
		contentPane.add(lblSenha_1);
		
		JButton btlogin = new JButton("logar");
		btlogin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btlogin.setBackground(Color.WHITE);
		btlogin.setForeground(Color.BLACK);
		btlogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Usuario usuario = null;
				try {
					usuario = sistema.validacaoLogin(tflogin.getText(), tfsenha.getText());
				} catch (SQLException | SelectException | LoginIncorretoException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					
				}

				if(usuario!=null) {
					JOptionPane meuJOPane = new JOptionPane("Bem vindo " +usuario.getLogin()+ "!");
					final JDialog dialog = meuJOPane.createDialog("Carregando...");
					dialog.setModal(true);
					Timer timer = new Timer(600, new ActionListener() {  
						public void actionPerformed(ActionEvent ev) {  
							dialog.dispose();  
						}  
					});  
					
					timer.start();
					dialog.setVisible(true);
					timer.stop();
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
					frame.textusuario.setText(usuario.getLogin());
					frame.tfIdUsuario.setText(String.valueOf(usuario.getId()));
					
					if(usuario.getCargo().equals("administrador")) {
						TelaPrincipal.btnUsuario.setVisible(true);
						TelaPrincipal.btnPlaylists.setVisible(false);
						TelaPrincipal.btnFavoritas.setVisible(false);
						MusicaView.panel.setVisible(true);
						MusicaView.panel_1.setVisible(true);
						ArtistaView.panel.setVisible(true);
						ArtistaView.panel_1.setVisible(true);
						
					}else {
						TelaPrincipal.btnUsuario.setVisible(false);
						TelaPrincipal.btnPlaylists.setVisible(true);
						TelaPrincipal.btnFavoritas.setVisible(true);
						MusicaView.panel.setVisible(false);
						MusicaView.panel_1.setVisible(false);
						ArtistaView.panel.setVisible(false);
						ArtistaView.panel_1.setVisible(false);
						
					}
					
					dispose();
				}
				tflogin.setText("");
				tfsenha.setText("");	
				
			}			
		
		});
		btlogin.setBounds(88, 122, 110, 23);
		contentPane.add(btlogin);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBackground(Color.WHITE);
		lblLogin.setForeground(new Color(255, 102, 51));
		lblLogin.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		lblLogin.setBounds(29, 41, 89, 23);
		contentPane.add(lblLogin);
		

		lblNewLabel.setBounds(0, 0, 434, 237);
		contentPane.add(lblNewLabel);
		


		

		

	}
}
