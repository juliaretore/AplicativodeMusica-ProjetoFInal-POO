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
import dados.Musica;
import dados.Playlist;
import dados.Usuario;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.JaCadastradoException;
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
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Rectangle;

public class FavoritasView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textPNome;
	private JTextField textPCodigo;
	static JTable table;
	private static FavoritasView favoritasView;

	private static List<Object> musicas = new ArrayList<Object>();
	Musica musica = new Musica();		
	private static Sistema sistema;
	static JTextField tfCodigoMusica;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FavoritasView frame = new FavoritasView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	public static FavoritasView getInstance() {
        if(favoritasView==null) favoritasView=new FavoritasView();
        atualizarTabela();
        return favoritasView;
    } 
	public FavoritasView() {
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null,  e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
			}
		});
		JLabel lblNewLabel = new JLabel("New label");
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/janelas.jpg")));

		setTitle("Gerenciar Favoritas");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		JButton cadastrar_1 = new JButton("Adicionar");
		cadastrar_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		cadastrar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdicionarMusicasFavoritasView frame = new AdicionarMusicasFavoritasView();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);				
			}
		});
		cadastrar_1.setBackground(SystemColor.window);
		cadastrar_1.setBounds(283, 482, 118, 21);
		contentPane.add(cadastrar_1);
		
		JButton excluir_1 = new JButton("Excluir");
		excluir_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		excluir_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					int musica = (int) table.getValueAt(table.getSelectedRow(), 0);
					int usuario = Integer.parseInt(TelaPrincipal.tfIdUsuario.getText());
					try {
						sistema.removerMusicaFavorita(musica,usuario);
					} catch (NumberFormatException | DeleteException | SelectException | NaoCadastradoException e1) {
						JOptionPane.showMessageDialog(null,  e1.getMessage());
					}
					atualizarTabela();
					
				
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				
			}
		});
		excluir_1.setBackground(SystemColor.window);
		excluir_1.setBounds(458, 482, 118, 21);
		contentPane.add(excluir_1);
		
		JLabel lblBusca = new JLabel("BUSCA");
		lblBusca.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblBusca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBusca.setBounds(300, 11, 70, 20);
		contentPane.add(lblBusca);
		
		JLabel lblArtistasCadastrados = new JLabel("MUSICAS FAVORITAS");
		lblArtistasCadastrados.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblArtistasCadastrados.setBounds(253, 96, 232, 20);
		contentPane.add(lblArtistasCadastrados);
		
		JButton sair = new JButton("Sair");
		sair.setBorder(new LineBorder(new Color(0, 0, 0)));
		sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});
		sair.setBackground(SystemColor.window);
		sair.setBounds(254, 633, 173, 20);
		contentPane.add(sair);
		
		JButton btnNewButton = new JButton("Tocar Pr\u00E9via");
		btnNewButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnNewButton.setBackground(SystemColor.window);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					String nomearq = null;
					try {
						nomearq = "arq/"+ sistema.buscarArquivo((int)table.getValueAt(table.getSelectedRow(), 0));
					} catch (SelectException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					tocarPreviaMusica(nomearq);
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");

			}
		});
		btnNewButton.setBounds(91, 481, 138, 23);
		contentPane.add(btnNewButton);
		
		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(Color.LIGHT_GRAY);
		separator_2_1_1.setBounds(10, 83, 659, 2);
		contentPane.add(separator_2_1_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(SystemColor.window);
		scrollPane.setBounds(91, 126, 485, 341);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setSelectionBackground(SystemColor.activeCaption);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setBackground(SystemColor.window);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
//				setCamposFromTabela();
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
		
		
		lblNewLabel.setBounds(0, -29, 684, 690);
		contentPane.add(lblNewLabel);
		
		
		
		

		
		tfCodigoMusica = new JTextField();
		tfCodigoMusica.setVisible(false);
		tfCodigoMusica.setAlignmentY(Component.TOP_ALIGNMENT);
		tfCodigoMusica.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfCodigoMusica.setEditable(false);
		tfCodigoMusica.setColumns(10);
		tfCodigoMusica.setBounds(324, 527, 46, 20);
		contentPane.add(tfCodigoMusica);
		

	}

	public void sair() {
		dispose();
	}


	
	public static void atualizarTabela() {
		try {
			musicas = sistema.listarMusicasFavoritas(Integer.parseInt(TelaPrincipal.tfIdUsuario.getText()));
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++)model.addRow((Object[]) musicas.get(i));
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void AdicionarMusica() throws InsertException, SelectException, NaoCadastradoException, NumberFormatException, JaCadastradoException{
		sistema.adicionarMusicaFavorita(Integer.parseInt(tfCodigoMusica.getText()), Integer.parseInt(TelaPrincipal.tfIdUsuario.getText()));
		atualizarTabela();	
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