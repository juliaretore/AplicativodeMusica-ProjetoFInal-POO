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
import exceptions.UpdateException;
import negocio.Sistema;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JPasswordField;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Choice;
import java.awt.Scrollbar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class UsuarioView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textPNome;
	private JTextField textPCodigo;

	private static List<Object> usuarios = new ArrayList<Object>();
	Usuario usuario = new Usuario();		
	private static Sistema sistema;
	private JTextField tfNome;
	private JTextField tfLogin;
	private JTextField tfCodigo;
	JComboBox comboBox = new JComboBox();
	private JTextField tfSenha;
	private static JTable table = new JTable();
	private static UsuarioView usuarioView;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UsuarioView frame = new UsuarioView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	public static UsuarioView getInstance() {
        if(usuarioView==null) usuarioView=new UsuarioView();
        atualizarTabela();
        return usuarioView;
    } 
	public UsuarioView() {
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
		contentPane.setEnabled(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBusca = new JLabel("BUSCA");
		lblBusca.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblBusca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBusca.setBounds(300, 11, 70, 20);
		contentPane.add(lblBusca);
		
		JLabel lblArtistasCadastrados = new JLabel("USU\u00C1RIOS CADASTRADOS");
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
		sair.setBounds(247, 621, 173, 20);
		contentPane.add(sair);
		
		
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
		
		

		
		JButton excluir_1 = new JButton("Excluir");
		excluir_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		excluir_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					try {
						sistema.excluirUsuario((int)table.getValueAt(table.getSelectedRow(), 0));
					} catch (NumberFormatException | DeleteException | SelectException | NaoCadastradoException e1) {
						JOptionPane.showMessageDialog(null,  e1.getMessage());
					}
					atualizarTabela();
					
				
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				
			}
		});
		excluir_1.setBackground(SystemColor.window);
		excluir_1.setBounds(76, 554, 118, 21);
		contentPane.add(excluir_1);
		
		JButton cadastrar_1 = new JButton("Cadastrar");
		cadastrar_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		cadastrar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if(((String)comboBox.getSelectedItem()).equals("selecione")) JOptionPane.showMessageDialog(null, "Escolha um cargo!");
					else if(tfNome.getText().equals("")) JOptionPane.showMessageDialog(null, "Digite o nome!");
					else if(tfLogin.getText().equals("")) JOptionPane.showMessageDialog(null, "Digite o login!");
					else if(tfSenha.getText().equals("")) JOptionPane.showMessageDialog(null, "Digite a senha!");
					else {
						usuario.setNome(tfNome.getText());
						usuario.setLogin(tfLogin.getText());
						usuario.setSenha(tfSenha.getText());
						usuario.setCargo((String)comboBox.getSelectedItem());
						
						try {
							sistema.adicionarUsuario(usuario);
						} catch (InsertException | SelectException | JaCadastradoException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						
						atualizarTabela();
						limpar();

					}	
			}
		});
		cadastrar_1.setBackground(SystemColor.window);
		cadastrar_1.setBounds(204, 554, 118, 21);
		contentPane.add(cadastrar_1);
		
		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(Color.LIGHT_GRAY);
		separator_2_1_1.setBounds(10, 83, 659, 2);
		contentPane.add(separator_2_1_1);
		
		tfNome = new JTextField();
		tfNome.setBounds(203, 387, 282, 19);
		contentPane.add(tfNome);
		tfNome.setColumns(10);
		
		JLabel lblNome_1 = new JLabel("Nome");
		lblNome_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblNome_1.setBounds(123, 386, 70, 20);
		contentPane.add(lblNome_1);
		
		tfLogin = new JTextField();
		tfLogin.setColumns(10);
		tfLogin.setBounds(203, 445, 282, 19);
		contentPane.add(tfLogin);
		
		tfCodigo = new JTextField();
		tfCodigo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		tfCodigo.setEditable(false);
		tfCodigo.setColumns(10);
		tfCodigo.setBounds(203, 416, 33, 19);
		contentPane.add(tfCodigo);
		
		JLabel lblCdigo_1 = new JLabel("C\u00F3digo");
		lblCdigo_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCdigo_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblCdigo_1.setBounds(127, 416, 70, 20);
		contentPane.add(lblCdigo_1);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLogin.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblLogin.setBounds(127, 441, 70, 20);
		contentPane.add(lblLogin);
		
		JLabel lblCargo = new JLabel("Cargo");
		lblCargo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCargo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblCargo.setBounds(127, 506, 70, 20);
		contentPane.add(lblCargo);
		
		JButton cadastrar_1_1 = new JButton("Alterar");
		cadastrar_1_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		cadastrar_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow()!=-1){
					try {
						usuario.setNome(tfNome.getText());
						usuario.setId(Integer.parseInt(tfCodigo.getText()));	
						usuario.setLogin(tfLogin.getText());
						usuario.setSenha(tfSenha.getText());
						usuario.setCargo((String)comboBox.getSelectedItem());
						sistema.alterarUsuario(usuario);						
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					atualizarTabela();
				}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
			}
		});
		cadastrar_1_1.setBackground(SystemColor.window);
		cadastrar_1_1.setBounds(332, 554, 118, 21);
		contentPane.add(cadastrar_1_1);
		
		JButton cadastrar_1_1_1 = new JButton("Limpar");
		cadastrar_1_1_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		cadastrar_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		cadastrar_1_1_1.setBackground(SystemColor.window);
		cadastrar_1_1_1.setBounds(465, 554, 118, 21);
		contentPane.add(cadastrar_1_1_1);
		comboBox.setBackground(SystemColor.window);
		comboBox.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		

		comboBox.setModel(new DefaultComboBoxModel(new String[] {"selecione", "administrador", "normal"}));
		comboBox.setBounds(203, 509, 283, 21);
		contentPane.add(comboBox);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSenha.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblSenha.setBounds(127, 476, 70, 20);
		contentPane.add(lblSenha);
		
		tfSenha = new JTextField();
		tfSenha.setColumns(10);
		tfSenha.setBounds(203, 480, 282, 19);
		contentPane.add(tfSenha);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(113, 126, 459, 242);
		contentPane.add(scrollPane);
		table.setSelectionBackground(SystemColor.activeCaption);
		table.setBackground(SystemColor.window);
		

		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome", "Login", "Senha", "Cargo"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
			}
		});
		
		
		lblNewLabel.setBounds(0, -29, 684, 690);
		contentPane.add(lblNewLabel);

	}

	public void sair() {
		dispose();
	}

	public void limpar() {
		tfNome.setText("");
		tfCodigo.setText("");
		tfLogin.setText("");
		comboBox.setSelectedIndex(0);
		tfSenha.setText("");
	}
	
	public void setCamposFromTabela() {
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		tfLogin.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		tfSenha.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
		if(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)).equals("administrador"))
			comboBox.setSelectedIndex(1);
		else comboBox.setSelectedIndex(2);
	}
	
	public static void atualizarTabela() {
		try {
			usuarios = sistema.listarUsuarios();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=usuarios.size();i++)model.addRow((Object[]) usuarios.get(i));
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}