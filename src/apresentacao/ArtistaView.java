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

public class ArtistaView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextField textPNome;
	private JTextField textPCodigo;
	static JTable table;

	private static List<Object> artistas = new ArrayList<Object>();
	private static List<Object> musicas = new ArrayList<Object>();
	Artista artista = new Artista();		
	private static Sistema sistema;
	static JTextField tfCodigo;
	private static JTable table_1;
	static JTextField tfCodigoMusica;
	static JPanel panel = new JPanel();
	static JPanel panel_1 = new JPanel();
	private static ArtistaView artistaView;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArtistaView frame = new ArtistaView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
	
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	 public static ArtistaView getInstance() {
         if(artistaView==null) artistaView=new ArtistaView();
         atualizarTabela();
         ((DefaultTableModel) table_1.getModel()).setNumRows(0);
         return artistaView;
     } 
	public ArtistaView() {
		this.setLocationRelativeTo(null);
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
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

		setTitle("Gerenciar Artistas");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBusca = new JLabel("BUSCA");
		lblBusca.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblBusca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBusca.setBounds(300, 11, 70, 20);
		contentPane.add(lblBusca);
		
		JLabel lblArtistasCadastrados = new JLabel("ARTISTAS CADASTRADOS");
		lblArtistasCadastrados.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblArtistasCadastrados.setBounds(76, 96, 232, 20);
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
		scrollPane.setBounds(27, 126, 276, 273);
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
		scrollPane_1.setBounds(376, 126, 276, 273);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setBackground(SystemColor.window);
		table_1.setSelectionBackground(SystemColor.activeCaption);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome"
			}
		));
		scrollPane_1.setViewportView(table_1);
		
		JLabel lblMsicasDoArtista = new JLabel("M\u00DASICAS DO ARTISTA");
		lblMsicasDoArtista.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblMsicasDoArtista.setBounds(420, 96, 232, 20);
		contentPane.add(lblMsicasDoArtista);
		
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
		tfCodigoMusica.setBounds(324, 233, 46, 20);
		contentPane.add(tfCodigoMusica);
		panel.setBackground(SystemColor.window);
		
		panel.setBounds(19, 440, 289, 145);
		contentPane.add(panel);
		panel.setLayout(null);
		
		tfNome = new JTextField();
		tfNome.setBounds(78, 10, 189, 20);
		panel.add(tfNome);
		tfNome.setColumns(10);
		
		JLabel label_1 = new JLabel("Nome:");
		label_1.setBounds(0, 9, 68, 20);
		panel.add(label_1);
		label_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tfCodigo = new JTextField();
		tfCodigo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		tfCodigo.setBounds(78, 42, 46, 20);
		panel.add(tfCodigo);
		tfCodigo.setEditable(false);
		tfCodigo.setColumns(10);
		
				
		JLabel lblCodigo = new JLabel("Codigo:");
		lblCodigo.setBounds(-12, 26,80,50);
		panel.add(lblCodigo);
		lblCodigo.setFont(new Font("Segoe45 Symbol", Font.BOLD, 15));
		lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
				
		JButton excluir = new JButton("Excluir");
		excluir.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		excluir.setBounds(10, 80, 118, 21);
		panel.add(excluir);
		excluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					Object[] options3 = {"Excluir", "Cancelar"};
					if(JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir o registro:\n>   " 
							+ table.getValueAt(table.getSelectedRow(), 0) + "   -   "
							+ table.getValueAt(table.getSelectedRow(), 1), null,
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[0])==0){
						try {
							sistema.excluirArtista(Integer.parseInt(tfCodigo.getText()));											
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
				
				JButton alterar = new JButton("Alterar");
				alterar.setBorder(new LineBorder(new Color(0, 0, 0)));
				alterar.setBounds(161, 80, 118, 21);
				panel.add(alterar);
				alterar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow()!=-1){
							try {
								artista.setNome(tfNome.getText());
								artista.setId(Integer.parseInt(tfCodigo.getText()));						
								sistema.alterarArtista(artista);						
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							atualizarTabela();
						}
						
						else{
							JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
						}
					}
				});
				alterar.setBackground(SystemColor.window);
				
				JButton limpar = new JButton("Limpar");
				limpar.setBorder(new LineBorder(new Color(0, 0, 0)));
				limpar.setBounds(7, 114, 118, 21);
				panel.add(limpar);
				limpar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						limpar();
					}
				});
				limpar.setBackground(SystemColor.window);
				
				JButton cadastrar = new JButton("Cadastrar");
				cadastrar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				cadastrar.setBounds(161, 111, 118, 21);
				panel.add(cadastrar);
				cadastrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							artista.setNome(tfNome.getText());
							sistema.cadastrarArtista(artista);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						atualizarTabela();
						limpar();
					}
				});
				cadastrar.setBackground(SystemColor.window);
				panel_1.setBackground(SystemColor.window);
				
				
				panel_1.setBounds(388, 440, 264, 35);
				contentPane.add(panel_1);
				panel_1.setLayout(null);
				
				JButton cadastrar_1 = new JButton("Adicionar");
				cadastrar_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				cadastrar_1.setBounds(0, 10, 118, 21);
				panel_1.add(cadastrar_1);
				cadastrar_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow()!=-1){
							AdicionarMusicasArtistaView frame = new AdicionarMusicasArtistaView();
							frame.setVisible(true);
							frame.setLocationRelativeTo(null);
						}else{
							JOptionPane.showMessageDialog(null, "Artista n�o selecionado!");
						}				
					}
				});
				cadastrar_1.setBackground(SystemColor.window);
				
				JButton excluir_1 = new JButton("Excluir");
				excluir_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				excluir_1.setBounds(146, 10, 118, 21);
				panel_1.add(excluir_1);
				excluir_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table_1.getSelectedRow()!=-1){
							int musica = (int) table_1.getValueAt(table_1.getSelectedRow(), 0);
							int artista = (int) table.getValueAt(table.getSelectedRow(), 0);
							try {
								sistema.removerArtistaMusica(musica,artista);
							} catch (NumberFormatException | DeleteException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							atualizarTabela2();
							
						
						}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
						
					}
				});
				excluir_1.setBackground(SystemColor.window);
				
				
				JButton btnNewButton = new JButton("Tocar Pr\u00E9via");
				btnNewButton.setBorder(new LineBorder(new Color(0, 0, 0)));
				btnNewButton.setBackground(SystemColor.window);
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
				btnNewButton.setBounds(534, 409, 118, 21);
				contentPane.add(btnNewButton);
				
				
				
				lblNewLabel.setBounds(0, -29, 684, 690);
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
		tfNome.setText("");
		tfCodigo.setText("");

	}

	public static void atualizarTabela() {
		try {
			artistas = sistema.listarArtistas();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=artistas.size();i++){
				model.addRow((Object[]) artistas.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void atualizarTabela2() {
		try {
				musicas = sistema.listarMusicasArtistas(Integer.parseInt(tfCodigo.getText()));
				DefaultTableModel model = (DefaultTableModel) table_1.getModel();
				model.setNumRows(0);
				for (int i=0;i!=musicas.size();i++)model.addRow((Object[]) musicas.get(i));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void AdicionarMusica() throws InsertException, SelectException, NaoCadastradoException{
		sistema.adicionarMusicasArtistas((int)table.getValueAt(table.getSelectedRow(), 0), Integer.parseInt( tfCodigoMusica.getText()));
		atualizarTabela2();
		
	}
	
	public static void tocarPreviaMusica(String nome) {
		try {
			FileInputStream fileInputStream = new FileInputStream(nome);
			Player player = new Player(fileInputStream);
			player.play(300);
			}catch (FileNotFoundException | JavaLayerException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
	}
}