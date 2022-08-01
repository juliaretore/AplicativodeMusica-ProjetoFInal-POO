package apresentacao;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;

public class MusicaView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextField textPNome;
	private JTextField textPCodigo;
	static JTable table;

	private static List<Object> artistas = new ArrayList<Object>();
	private static List<Object> musicas = new ArrayList<Object>();
	Musica musica = new Musica();		
	private static Sistema sistema;
	static JTextField tfCodigo;
	private static JTable table_1;
	static JTextField tfCodigoMusica;
	private JTextField tfAlbum;
	private JTextField tfEstilo;
	static JButton alterar = new JButton("Alterar");
	static JButton excluir = new JButton("Excluir");
	static JButton cadastrar = new JButton("Cadastrar");
	static JButton limpar = new JButton("Limpar");
	static JPanel panel = new JPanel();
	static JPanel panel_1 = new JPanel();
	private static MusicaView musicaView;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicaView frame = new MusicaView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}
	
	


	 public static MusicaView getInstance() {
         if(musicaView==null) musicaView=new MusicaView();
         atualizarTabela();
         ((DefaultTableModel) table_1.getModel()).setNumRows(0);
         return musicaView;
     } 
	public MusicaView() {
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
		lblNewLabel.setBounds(-26, -460, 684, 692);
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/janelas.jpg")));

		setTitle("Gerenciar M�sicas");
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
		
		JLabel lblArtistasCadastrados = new JLabel("MUSICAS CADASTRADAS");
		lblArtistasCadastrados.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblArtistasCadastrados.setBounds(76, 96, 232, 20);
		contentPane.add(lblArtistasCadastrados);
		
		JButton sair = new JButton("Sair");
		sair.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
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
				"ID", "Nome", "Álbum", "Estilo"
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
		scrollPane_1.setBounds(374, 126, 276, 261);
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
		
		JLabel lblMsicasDoArtista = new JLabel("ARTISTAS DA M\u00DASICA");
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
		tfCodigoMusica.setBounds(313, 259, 46, 20);
		contentPane.add(tfCodigoMusica);
		
		JButton btnNewButton = new JButton("Tocar Pr\u00E9via");
		btnNewButton.setBorder(new LineBorder(new Color(0, 0, 0)));
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
		btnNewButton.setBounds(185, 397, 118, 21);
		contentPane.add(btnNewButton);
		

		panel.setBounds(22, 428, 286, 185);
		contentPane.add(panel);
		panel.setLayout(null);
		
		tfNome = new JTextField();
		tfNome.setBounds(67, 12, 204, 19);
		panel.add(tfNome);
		tfNome.setColumns(10);
		
		JLabel label_1 = new JLabel("Nome:");
		label_1.setBounds(1, 8, 56, 21);
		panel.add(label_1);
		label_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tfCodigo = new JTextField();
		tfCodigo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		tfCodigo.setBounds(67, 39, 63, 19);
		panel.add(tfCodigo);
		tfCodigo.setEditable(false);
		tfCodigo.setColumns(10);
		
				
				JLabel lblCodigo = new JLabel("Codigo:");
				lblCodigo.setBounds(-26, 39, 86, 20);
				panel.add(lblCodigo);
				lblCodigo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
				lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
				
				tfAlbum = new JTextField();
				tfAlbum.setBounds(67, 68, 204, 20);
				panel.add(tfAlbum);
				tfAlbum.setColumns(10);
				
				tfEstilo = new JTextField();
				tfEstilo.setBounds(67, 98, 204, 20);
				panel.add(tfEstilo);
				tfEstilo.setColumns(10);
				
				JLabel label_1_1 = new JLabel("\u00C1lbum:");
				label_1_1.setBounds(-26, 64, 83, 20);
				panel.add(label_1_1);
				label_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
				label_1_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
				
				JLabel label_1_1_1 = new JLabel("Estilo:");
				label_1_1_1.setBounds(-26, 94, 83, 20);
				panel.add(label_1_1_1);
				label_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
				label_1_1_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
				
				JButton btnNewButton_1 = new JButton("Inserir Arquivo");
				btnNewButton_1.setBackground(SystemColor.window);
				btnNewButton_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				btnNewButton_1.setBounds(140, 38, 131, 21);
				panel.add(btnNewButton_1);
				limpar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				limpar.setBounds(11, 128, 118, 21);
				panel.add(limpar);
				
				
				limpar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						limpar();
					}
				});
				limpar.setBackground(SystemColor.window);
				cadastrar.setBorder(new LineBorder(new Color(0, 0, 0)));
				cadastrar.setBounds(153, 128, 118, 21);
				panel.add(cadastrar);
				

				cadastrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							musica.setNome(tfNome.getText());
							musica.setEstilo(tfEstilo.getText());
							musica.setAlbum(tfAlbum.getText());
							sistema.uploadMusicas(musica);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						atualizarTabela();
						limpar();
					}
				});
				cadastrar.setBackground(SystemColor.window);
				excluir.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				excluir.setBounds(153, 154, 118, 21);
				panel.add(excluir);
				

				excluir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow()!=-1){
							Object[] options3 = {"Excluir", "Cancelar"};
							if(JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir o registro:\n>   " 
									+ table.getValueAt(table.getSelectedRow(), 0) + "   -   "
									+ table.getValueAt(table.getSelectedRow(), 1), null,
									JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[0]) == 0){
								try {
									sistema.excluirMusica(Integer.parseInt(tfCodigo.getText()));											
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
				alterar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				alterar.setBounds(12, 154, 118, 21);
				panel.add(alterar);
				

				alterar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow() != -1){
							try {
								musica.setNome(tfNome.getText());
								musica.setId(Integer.parseInt(tfCodigo.getText()));	
								musica.setEstilo(tfEstilo.getText());
								musica.setAlbum(tfAlbum.getText());
								sistema.alterarMusica(musica);						
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							atualizarTabela();
						}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
						
					}
				});
				alterar.setBackground(SystemColor.window);
				panel_1.setBackground(SystemColor.window);

				

				panel_1.setBounds(374, 397, 276, 43);
				contentPane.add(panel_1);
				panel_1.setLayout(null);
				
				JButton cadastrar_1 = new JButton("Adicionar");
				cadastrar_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				cadastrar_1.setBounds(0, 10, 118, 21);
				panel_1.add(cadastrar_1);
				cadastrar_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow()!=-1){
							AdicionarArtistasMusicaView frame = new AdicionarArtistasMusicaView();
							frame.setVisible(true);
							frame.setLocationRelativeTo(null);

						}else{
							JOptionPane.showMessageDialog(null, "M�sica n�o selecionada!");
						}				
					}
				});
				cadastrar_1.setBackground(SystemColor.window);
				
				JButton excluir_1 = new JButton("Excluir");
				excluir_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				excluir_1.setBounds(158, 10, 118, 21);
				panel_1.add(excluir_1);
				excluir_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table_1.getSelectedRow()!=-1){
							int musica = (int) table.getValueAt(table.getSelectedRow(), 0);
							int artista = (int) table_1.getValueAt(table_1.getSelectedRow(), 0);
							try {
								sistema.removerArtistaMusica(musica,artista);
							} catch (NumberFormatException | DeleteException e1) {
								JOptionPane.showMessageDialog(null, "Erro ao deletar artista da m�sica");
							}
							atualizarTabela2();
							
						
						}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
						
					}
				});
				excluir_1.setBackground(SystemColor.window);
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(tfNome.getText().equals("") || tfAlbum.getText().equals("") || tfEstilo.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Necess�rio preencher todos os campos antes de inserir o arquivo.");
						}else {
							JFileChooser chooser = new JFileChooser();
						    FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files", "mp3", "wav");
						    chooser.setFileFilter(filter);
						    int returnVal = chooser.showOpenDialog(chooser);
						    if (returnVal == JFileChooser.APPROVE_OPTION) {
						        File file = chooser.getSelectedFile();
						        File arquivos = new File(tfNome.getText()+"-"+tfAlbum.getText()+"-"+tfEstilo.getText()+".mp3");
						        File dir = new File("arq");
						        boolean ok = chooser.getSelectedFile().renameTo(new File(dir, arquivos.getName()));
						        if(!ok)JOptionPane.showMessageDialog(null, "Nao foi possivel mover o arquivo");
						    }						
						}
					}
				});
				
				lblNewLabel.setBounds(0, -29, 684, 690);
				contentPane.add(lblNewLabel);

	}

	public void sair() {
		dispose();
	}

	public void setCamposFromTabela() {
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		tfAlbum.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		tfEstilo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
		atualizarTabela2();
	}

	public void limpar() {
		tfNome.setText("");
		tfCodigo.setText("");
		tfAlbum.setText("");
		tfEstilo.setText("");
//		tf
	}

	public static void atualizarTabela() {
		try {
			musicas = sistema.listarMusicas();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++){
				model.addRow((Object[]) musicas.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void atualizarTabela2() {
	
			try {
				artistas = sistema.listarArtistasMusica(Integer.parseInt(tfCodigo.getText()));
			} catch (NumberFormatException | SelectException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			DefaultTableModel model = (DefaultTableModel) table_1.getModel();
			model.setNumRows(0);
		for (int i=0;i!=artistas.size();i++)model.addRow((Object[]) artistas.get(i));

	}
	
	public static void AdicionarArtista() throws InsertException, SelectException, NaoCadastradoException{
		sistema.adicionarMusicasArtistas(Integer.parseInt(tfCodigoMusica.getText()), (int)table.getValueAt(table.getSelectedRow(), 0));
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