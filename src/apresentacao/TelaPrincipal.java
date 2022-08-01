package apresentacao;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dados.Usuario;
import exceptions.SelectException;
import negocio.Sistema;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.border.MatteBorder;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextField textusuario;
	static JTextField tfIdUsuario;
	static JButton btnUsuario = new JButton("Gerenciamento de Usuários");
	static JButton btnMusicas = new JButton("Músicas");
	static JButton btnPlaylists= new JButton("Playlists");
	static JButton btnArtistas = new JButton("Artistas");
	static JButton btnFavoritas = new JButton("Favoritas");

	public static void main(String[] args) { 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					TelaPrincipal frame = new TelaPrincipal();
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


	public TelaPrincipal() {
		JLabel lblNewLabel = new JLabel("New label");
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu.jpg")));
		
		
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  692, 700);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnUsuario.setBackground(Color.WHITE);
		btnUsuario.setForeground(Color.BLACK);
		btnUsuario.setVisible(false);
		btnUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUsuario.setBounds(211, 253, 271, 77);
		contentPane.add(btnUsuario);
		
		btnMusicas.setBackground(Color.WHITE);
		btnMusicas.setForeground(Color.BLACK);
		btnMusicas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnMusicas.addActionListener(new ActionListener() {
		
			
			public void actionPerformed(ActionEvent e) {
				MusicaView objeto = MusicaView.getInstance();
				objeto.setVisible(true);
				objeto.setLocationRelativeTo(null);
			}
		});
		btnMusicas.setBounds(72, 145, 256, 67);
		contentPane.add(btnMusicas);
		textusuario = new JTextField();
		textusuario.setBackground(SystemColor.window);
		textusuario.setDisabledTextColor(new Color(0, 0, 0));
		textusuario.setEditable(false);
		textusuario.setBounds(429, 42, 86, 29);
		contentPane.add(textusuario);
		textusuario.setColumns(10);
		
		
		tfIdUsuario = new JTextField();
		tfIdUsuario.setBackground(SystemColor.window);
		tfIdUsuario.setEditable(false);
		tfIdUsuario.setColumns(10);
		tfIdUsuario.setBounds(402, 42, 29, 29);
		contentPane.add(tfIdUsuario);

		
		btnPlaylists.setBackground(Color.WHITE);
		btnPlaylists.setForeground(Color.BLACK);
		btnPlaylists.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPlaylists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			PlaylistView objeto = PlaylistView.getInstance();
			objeto.setVisible(true);
			objeto.setLocationRelativeTo(null);

			}
			
		});
		btnPlaylists.setBounds(72, 381, 256, 67);
		contentPane.add(btnPlaylists);
		
		JButton btnNewButton = new JButton("Deslogar");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginView frame = new LoginView();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewButton.setBounds(522, 41, 133, 31);
		contentPane.add(btnNewButton);
		
		btnFavoritas.setBackground(Color.WHITE);
		btnFavoritas.setForeground(Color.BLACK);
		btnFavoritas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFavoritas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FavoritasView objeto = FavoritasView.getInstance();
				objeto.setVisible(true);
				objeto.setLocationRelativeTo(null);
				
				
			}
		});
		btnFavoritas.setBounds(370, 381, 256, 67);
		contentPane.add(btnFavoritas);
		
		btnArtistas.setBackground(Color.WHITE);
		btnArtistas.setForeground(Color.BLACK);
		btnArtistas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnArtistas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArtistaView objeto = ArtistaView.getInstance();
				objeto.setVisible(true);
				objeto.setLocationRelativeTo(null);

				
			}
		});
		btnArtistas.setBounds(370, 145, 256, 67);
		contentPane.add(btnArtistas);
		

	
		
		btnUsuario.setActionCommand("Gerenciamento de Usuários");
		btnUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UsuarioView objeto = UsuarioView.getInstance();
				objeto.setVisible(true);
				objeto.setLocationRelativeTo(null);
				
				
			}
		});
		
		
		
		lblNewLabel.setBounds(0, 0, 676, 661);
		contentPane.add(lblNewLabel);
		

	}
}
