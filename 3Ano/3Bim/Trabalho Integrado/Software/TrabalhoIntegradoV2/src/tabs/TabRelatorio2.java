package tabs;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tabs.listeners.Pesquisar2Listener;

import org.jdesktop.swingx.JXDatePicker;

import tools.LimiteCaracteres;
import tools.OnlyNumberKeyListener;
import database.Db;

public class TabRelatorio2 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelRelCom;
	private JPanel panelDataInicial;
	private JPanel panelUser;
	private JXDatePicker jxDataI;
	private JTextField txtHoraI;
	private JTextField txtMinI;
	private JTextField txtSecI;

	private JPanel panelDataFinal;
	private JXDatePicker jxDataF;
	private JTextField txtHoraF;
	private JTextField txtMinF;
	private JTextField txtSecF;
	
	private JLabel lUsuarios;
	private JComboBox<String> lstUsuarios;

	// ///tabela de resultados
	private JPanel panelTabela;
	private JButton btPesquisar;

	private Db conexao;

	public TabRelatorio2(Db conexao) {
		this.conexao = conexao;
		// /layout tab relatorios///
		panelRelCom = new JPanel();
		panelRelCom.setBorder(BorderFactory
				.createTitledBorder("Relatorio de comandos"));
		panelDataInicial = new JPanel(new GridLayout(0, 2));
		panelDataInicial.setBorder(BorderFactory
				.createTitledBorder("Data Inicial"));
		jxDataI = new JXDatePicker();
		//jxDataI.setFormats("dd-MM-yyyy HH:mm:ss");

		txtHoraI = new JTextField(2);
		txtHoraI.setDocument(new LimiteCaracteres(2));
		txtHoraI.addKeyListener(new OnlyNumberKeyListener());
		txtMinI = new JTextField(2);
		txtMinI.setDocument(new LimiteCaracteres(2));
		txtMinI.addKeyListener(new OnlyNumberKeyListener());
		txtSecI = new JTextField(2);
		txtSecI.setDocument(new LimiteCaracteres(2));
		txtSecI.addKeyListener(new OnlyNumberKeyListener());

		panelDataFinal = new JPanel(new GridLayout(0, 2));
		panelDataFinal
				.setBorder(BorderFactory.createTitledBorder("Data Final"));
		jxDataF = new JXDatePicker();
		txtHoraF = new JTextField(2);
		txtHoraF.setDocument(new LimiteCaracteres(2));
		txtHoraF.addKeyListener(new OnlyNumberKeyListener());
		txtMinF = new JTextField(2);
		txtMinF.setDocument(new LimiteCaracteres(2));
		txtMinF.addKeyListener(new OnlyNumberKeyListener());
		txtSecF = new JTextField(2);
		txtSecF.setDocument(new LimiteCaracteres(2));
		txtSecF.addKeyListener(new OnlyNumberKeyListener());

		// ///tabela de resultados
		panelTabela = new JPanel(new FlowLayout());
		// ///

		btPesquisar = new JButton("Pesquisar");
		btPesquisar.addActionListener(new Pesquisar2Listener(this));

		panelRelCom.add(panelDataInicial);
		panelDataInicial.add(new JLabel("Dia"));
		panelDataInicial.add(jxDataI);
		panelDataInicial.add(new JLabel("Horas"));
		panelDataInicial.add(txtHoraI);
		panelDataInicial.add(new JLabel("Minutos"));
		panelDataInicial.add(txtMinI);
		panelDataInicial.add(new JLabel("Segundos"));
		panelDataInicial.add(txtSecI);

		panelRelCom.add(panelDataFinal);
		panelDataFinal.add(new JLabel("Dia"));
		panelDataFinal.add(jxDataF);
		panelDataFinal.add(new JLabel("Horas"));
		panelDataFinal.add(txtHoraF);
		panelDataFinal.add(new JLabel("Minutos"));
		panelDataFinal.add(txtMinF);
		panelDataFinal.add(new JLabel("Segundos"));
		panelDataFinal.add(txtSecF);

		panelUser = new JPanel();
		panelUser.setBorder(BorderFactory
				.createTitledBorder("Usuario"));
		lUsuarios = new JLabel("Usuarios");
		lstUsuarios = new JComboBox<>();
		// adicionando usuarios no ComboBox
		String consulta = "SELECT login "+
				"FROM operador";
		ResultSet rs = conexao.consultar(consulta);
		lstUsuarios.addItem("Todos");
		try {
			while (rs.next()) {
				lstUsuarios.addItem(rs.getString("login"));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao listar os usuarios existentes: "+ e.getMessage(), 
					"Erro", 
					JOptionPane.ERROR_MESSAGE);
		}
		panelUser.add(lUsuarios);
		panelUser.add(lstUsuarios);
		panelRelCom.add(panelUser);
		panelRelCom.add(btPesquisar);

		add(panelRelCom);
		add(panelTabela);
	}

	public Container gePanelTabela() {
		return panelTabela;
	}

	public Date getDataI() {
		return jxDataI.getDate();
	}

	public Date getDataF() {
		return jxDataF.getDate();
	}

	public String getHoraI() {
		return txtHoraI.getText();
	}

	public String getHoraF() {
		return txtHoraF.getText();
	}

	public String getMinI() {
		return txtMinI.getText();
	}

	public String getMinF() {
		return txtMinF.getText();
	}

	public String getSecI() {
		return txtSecI.getText();
	}

	public String getSecF() {
		return txtSecF.getText();
	}

	public Db getConexao() {
		return conexao;
	}

	public String getSelectedUser(){
		return lstUsuarios.getSelectedItem().toString();
	}
}
