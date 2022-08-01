package apresentacao;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import exceptions.InsertException;
import exceptions.JaCadastradoException;
import exceptions.NaoCadastradoException;
import exceptions.SelectException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import negocio.Sistema;
import persistencia.*;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.border.MatteBorder;

public class AdicionarMusicasFavoritasView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textPNome;
	private JTextField textPCodigo;
	private static JTable table;
	private static Sistema sistema;

	private static List<Object> musicas = new ArrayList<Object>();
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdicionarMusicasFavoritasView frame = new AdicionarMusicasFavoritasView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	public AdicionarMusicasFavoritasView() {
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
				if(table.getRowCount()==0) {
					dispose();
					JOptionPane.showMessageDialog(null, "Sem m�sicas dispon�veis para adicionar!");
				}
			}
		});
		JLabel lblNewLabel = new JLabel("New label");
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/adicionar.jpg")));

		setTitle("Adicionar Musicas");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  415, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBusca = new JLabel("BUSCA");
		lblBusca.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblBusca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBusca.setBounds(157, 11, 69, 20);
		contentPane.add(lblBusca);
		
		JLabel lblMusicasParaAdicionae = new JLabel("MUSICAS PARA ADICIONAR");
		lblMusicasParaAdicionae.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblMusicasParaAdicionae.setBounds(99, 104, 369, 20);
		contentPane.add(lblMusicasParaAdicionae);
		
		JButton sair = new JButton("Sair");
		sair.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});
		sair.setBackground(SystemColor.window);
		sair.setBounds(277, 325, 92, 23);
		contentPane.add(sair);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 125, 379, 189);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setBackground(SystemColor.window);
		table.setSelectionBackground(SystemColor.activeCaption);
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Nome"
			}
		));
		
		JLabel label_6 = new JLabel("C\u00F3digo:");
		label_6.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		label_6.setBounds(10, 39, 69, 20);
		contentPane.add(label_6);
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textPCodigo = new JTextField();
		textPCodigo.setBounds(89, 42, 305, 20);
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
		textPNome.setBounds(89, 73, 305, 20);
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
		
		JLabel label_7 = new JLabel("Nome:");
		label_7.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		label_7.setBounds(20, 70, 59, 20);
		contentPane.add(label_7);
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setBounds(5, 104, 384, 2);
		contentPane.add(separator);
		
		JButton selecionar = new JButton("Selecionar");
		selecionar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		selecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow()!=-1){
						FavoritasView.tfCodigoMusica.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
					try {
						FavoritasView.AdicionarMusica();
					} catch (InsertException | SelectException |NumberFormatException | JaCadastradoException | NaoCadastradoException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					FavoritasView.tfCodigoMusica.setText("");
					dispose();  
				}else JOptionPane.showMessageDialog(null, "Nenhuma m�sica selecionada!");
			}
		});
		selecionar.setBackground(SystemColor.window);
		selecionar.setBounds(20, 325, 99, 23);
		contentPane.add(selecionar);

		
		JButton btnTocarPrevia = new JButton("Tocar Pr\u00E9via");
		btnTocarPrevia.addActionListener(new ActionListener() {
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
		btnTocarPrevia.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnTocarPrevia.setBackground(Color.WHITE);
		btnTocarPrevia.setBounds(148, 325, 99, 23);
		contentPane.add(btnTocarPrevia);
		
		lblNewLabel.setBounds(0, -41, 410, 490);
		contentPane.add(lblNewLabel);
	}

	public void sair() {
		dispose();
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
	public static void atualizarTabela() {
		try {
			musicas = sistema.listarExcetoMusicasFavoritas(Integer.parseInt(TelaPrincipal.tfIdUsuario.getText()));
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++) model.addRow((Object[]) musicas.get(i));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
