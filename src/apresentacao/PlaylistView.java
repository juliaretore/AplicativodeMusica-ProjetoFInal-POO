package apresentacao;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import persistencia.*;
import dados.Artista;
import dados.Playlist;
import dados.Usuario;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import negocio.Sistema;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Component;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;

public class PlaylistView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextField textPNome;
	private JTextField textPCodigo;
	static JTable table;

	private static List<Object> playlists = new ArrayList<Object>();
	private static List<Object> musicas = new ArrayList<Object>();
	Playlist playlist = new Playlist();		
	private static Sistema sistema;
	static JTextField tfCodigo;
	private static JTable table_1;
	static JTextField tfCodigoMusica;
	private static PlaylistView playlistView;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlaylistView frame = new PlaylistView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	public static PlaylistView getInstance() {
        if(playlistView==null) playlistView=new PlaylistView();
        atualizarTabela();
        ((DefaultTableModel) table_1.getModel()).setNumRows(0);
        return playlistView;
    }
	
	public PlaylistView() {
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null,  e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
				limpar();
			}
		});
		JLabel lblNewLabel = new JLabel("New label");
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/janelas.jpg")));


		setTitle("Gerenciar Playlist");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label_1 = new JLabel("Nome:");
		label_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(12, 409, 61, 20);
		contentPane.add(label_1);
		
		tfNome = new JTextField();
		tfNome.setColumns(10);
		tfNome.setBounds(99, 409, 204, 20);
		contentPane.add(tfNome);
		
		JButton alterar = new JButton("Alterar");
		alterar.setBorder(new LineBorder(new Color(0, 0, 0)));
		alterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					try {
						playlist.setNome(tfNome.getText());
						playlist.setId(Integer.parseInt(tfCodigo.getText()));						
						sistema.alterarPlaylist(playlist);						
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					atualizarTabela();
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
//				
			}
		});
		alterar.setBackground(SystemColor.window);
		alterar.setBounds(173, 490, 118, 21);
		contentPane.add(alterar);
		
		JButton cadastrar = new JButton("Cadastrar");
		cadastrar.setBorder(new LineBorder(new Color(0, 0, 0)));
		cadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					playlist.setNome(tfNome.getText());
					sistema.criarNovaPlaylist(playlist, Integer.parseInt(TelaPrincipal.tfIdUsuario.getText()));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				atualizarTabela();
				limpar();
			}
		});
		cadastrar.setBackground(SystemColor.window);
		cadastrar.setBounds(173, 522, 118, 21);
		contentPane.add(cadastrar);
		
		JButton limpar = new JButton("Limpar");
		limpar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		limpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		limpar.setBackground(SystemColor.window);
		limpar.setBounds(27, 522, 118, 21);
		contentPane.add(limpar);
		
		JButton excluir = new JButton("Excluir");
		excluir.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		excluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					Object[] options3 = {"Excluir", "Cancelar"};
					if(JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir o registro:\n>   " 
							+ table.getValueAt(table.getSelectedRow(), 0) + "   -   "
							+ table.getValueAt(table.getSelectedRow(), 1), null,
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[0]) == 0){
						try {
							sistema.excluirPlaylist(Integer.parseInt(tfCodigo.getText()));											
							atualizarTabela();
							limpar();
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				}
			}
		});
		excluir.setBackground(SystemColor.window);
		excluir.setBounds(27, 490, 118, 21);
		contentPane.add(excluir);
		
		JLabel lblBusca = new JLabel("BUSCA");
		lblBusca.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblBusca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBusca.setBounds(300, 11, 70, 20);
		contentPane.add(lblBusca);
		
		JLabel lblArtistasCadastrados = new JLabel("PLAYLISTS");
		lblArtistasCadastrados.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblArtistasCadastrados.setBounds(122, 96, 232, 20);
		contentPane.add(lblArtistasCadastrados);
		
		JButton sair = new JButton("Sair");
		sair.setBorder(new LineBorder(new Color(0, 0, 0)));
		sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});
		sair.setBackground(SystemColor.window);
		sair.setBounds(246, 630, 173, 20);
		contentPane.add(sair);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 126, 276, 261);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setSelectionBackground(SystemColor.activeCaption);
		table.setBackground(SystemColor.window);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(15);

		
		JLabel lblCodigo = new JLabel("Codigo:");
		lblCodigo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCodigo.setBounds(12, 443, 74, 20);
		contentPane.add(lblCodigo);
		
		tfCodigo = new JTextField();
		tfCodigo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		tfCodigo.setBackground(SystemColor.menu);
		tfCodigo.setDisabledTextColor(SystemColor.textHighlightText);
		tfCodigo.setEditable(false);
		tfCodigo.setColumns(10);
		tfCodigo.setBounds(99, 440, 46, 20);
		contentPane.add(tfCodigo);
		
		JLabel lblCdigo = new JLabel("C\u00F3digo");
		lblCdigo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblCdigo.setBounds(127, 21, 70, 20);
		contentPane.add(lblCdigo);
		lblCdigo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textPCodigo = new JTextField();
		textPCodigo.setBounds(76, 42, 198, 20);
		contentPane.add(textPCodigo);
		textPCodigo.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table.setRowSorter(filtro);
				
				if (textPCodigo.getText().length()==0) filtro.setRowFilter(null);
				else filtro.setRowFilter(RowFilter.regexFilter(textPCodigo.getText(), 0));  
			}
		});
		textPCodigo.setColumns(10);
		
		textPNome = new JTextField();
		textPNome.setBounds(407, 42, 198, 20);
		contentPane.add(textPNome);
		textPNome.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table.setRowSorter(filtro); 
				
				if (textPNome.getText().length()==0) filtro.setRowFilter(null);
				else filtro.setRowFilter(RowFilter.regexFilter("(?i)" + textPNome.getText(), 1));  
				
			}
		});
		textPNome.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblNome.setBounds(465, 21, 70, 20);
		contentPane.add(lblNome);
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		
		

		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(368, 126, 276, 261);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setBackground(SystemColor.window);
		table_1.setGridColor(SystemColor.controlDkShadow);
		table_1.setSelectionBackground(SystemColor.activeCaption);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome"
			}
		));
		scrollPane_1.setViewportView(table_1);
		
		JLabel lblMsicasDoArtista = new JLabel("M\u00DASICAS DA PLAYLIST");
		lblMsicasDoArtista.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblMsicasDoArtista.setBounds(420, 96, 232, 20);
		contentPane.add(lblMsicasDoArtista);
		
		JButton excluir_1 = new JButton("Excluir");
		excluir_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		excluir_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table_1.getSelectedRow()!=-1){
					int musica = (int) table_1.getValueAt(table_1.getSelectedRow(), 0);
					int playlist = (int) table.getValueAt(table.getSelectedRow(), 0);
					try {
						sistema.removerMusicasPlaylist(playlist,musica);
					} catch (NumberFormatException | DeleteException | SelectException | NaoCadastradoException e1) {
						JOptionPane.showMessageDialog(null,  e1.getMessage());
					}
					atualizarTabela2();
					
				
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				
			}
		});
		excluir_1.setBackground(SystemColor.window);
		excluir_1.setBounds(534, 446, 118, 21);
		contentPane.add(excluir_1);
		
		JButton cadastrar_1 = new JButton("Adicionar");
		cadastrar_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		cadastrar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					AdicionarMusicasPlaylistView frame = new AdicionarMusicasPlaylistView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);

				}else{
					JOptionPane.showMessageDialog(null, "Artista n�o selecionado!");
				}				
			}
		});
		cadastrar_1.setBackground(SystemColor.window);
		cadastrar_1.setBounds(388, 446, 118, 21);
		contentPane.add(cadastrar_1);
		
		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(Color.LIGHT_GRAY);
		separator_2_1_1.setBounds(0, 87, 659, 2);
		contentPane.add(separator_2_1_1);
		
		tfCodigoMusica = new JTextField();
		tfCodigoMusica.setVisible(false);
		tfCodigoMusica.setAlignmentY(Component.TOP_ALIGNMENT);
		tfCodigoMusica.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfCodigoMusica.setEditable(false);
		tfCodigoMusica.setColumns(10);
		tfCodigoMusica.setBounds(511, 542, 46, 20);
		contentPane.add(tfCodigoMusica);
		
		JButton btnNewButton = new JButton("Tocar pr\u00E9via");
		btnNewButton.setBackground(SystemColor.window);
		btnNewButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table_1.getSelectedRow()!=-1){
					String nomearq = null;
					try {
						nomearq = "arq/"+ sistema.buscarArquivo((int)table_1.getValueAt(table_1.getSelectedRow(), 0));
					} catch (SelectException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					tocarPreviaMusica(nomearq);
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
			}
		});
		btnNewButton.setBounds(446, 398, 118, 23);
		contentPane.add(btnNewButton);
		
		lblNewLabel.setBounds(0, -30, 684, 701);
		contentPane.add(lblNewLabel);
	}

	public void sair() {
		dispose();
	}

	public void setCamposFromTabela() {
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		atualizarTabela2();
	}

	public void limpar() {
		tfNome.setText(null);
		tfCodigo.setText(null);
	}

	public static void atualizarTabela() {
		try {
			playlists = sistema.listarPlaylists(Integer.parseInt(TelaPrincipal.tfIdUsuario.getText()));
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=playlists.size();i++){
				model.addRow((Object[]) playlists.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void atualizarTabela2() {
		try {
			musicas = sistema.listarMusicasPlaylist(Integer.parseInt(tfCodigo.getText()));
			DefaultTableModel model = (DefaultTableModel) table_1.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++)model.addRow((Object[]) musicas.get(i));
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void AdicionarMusica() throws InsertException, SelectException, NaoCadastradoException{
		sistema.adicionarMusicasPlaylist(Integer.parseInt( tfCodigoMusica.getText()), (int)table.getValueAt(table.getSelectedRow(), 0));
		atualizarTabela2();	
	}
	
	public static void tocarPreviaMusica(String nome) {
		try {
			FileInputStream fileInputStream = new FileInputStream(nome);
			Player player = new Player(fileInputStream);
			player.play(300);
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (JavaLayerException e) {
				e.printStackTrace();
			}
	}
}