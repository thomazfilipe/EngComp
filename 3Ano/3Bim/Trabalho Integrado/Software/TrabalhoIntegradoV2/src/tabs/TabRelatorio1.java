package tabs;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tabs.listeners.PesquisarListener;

import org.jdesktop.swingx.JXDatePicker;

import tools.LimiteCaracteres;
import tools.OnlyNumberKeyListener;
import database.Db;

public class TabRelatorio1 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelRelFluxo;
	private JPanel panelDataInicial;
	private JXDatePicker jxDataI;
	private JTextField txtHoraI;
	private JTextField txtMinI;
	private JTextField txtSecI;

	private JPanel panelDataFinal;
	private JXDatePicker jxDataF;
	private JTextField txtHoraF;
	private JTextField txtMinF;
	private JTextField txtSecF;

	// ///tabela de resultados
	private JPanel panelTabela;
	private JButton btPesquisar;
	
	private Db conexao;
	
	public TabRelatorio1(Db conexao) {
		this.conexao = conexao;
		// /layout tab relatorios///
		panelRelFluxo = new JPanel();
		panelRelFluxo.setBorder(BorderFactory
				.createTitledBorder("Relatorio de fluxo"));
		panelDataInicial = new JPanel(new GridLayout(0, 2));
		panelDataInicial.setBorder(BorderFactory
				.createTitledBorder("Data Inicial"));
		jxDataI = new JXDatePicker();
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
		btPesquisar.addActionListener(new PesquisarListener(this));

		panelRelFluxo.add(panelDataInicial);
		panelDataInicial.add(new JLabel("Dia"));
		panelDataInicial.add(jxDataI);
		panelDataInicial.add(new JLabel("Horas"));
		panelDataInicial.add(txtHoraI);
		panelDataInicial.add(new JLabel("Minutos"));
		panelDataInicial.add(txtMinI);
		panelDataInicial.add(new JLabel("Segundos"));
		panelDataInicial.add(txtSecI);

		panelRelFluxo.add(panelDataFinal);
		panelDataFinal.add(new JLabel("Dia"));
		panelDataFinal.add(jxDataF);
		panelDataFinal.add(new JLabel("Horas"));
		panelDataFinal.add(txtHoraF);
		panelDataFinal.add(new JLabel("Minutos"));
		panelDataFinal.add(txtMinF);
		panelDataFinal.add(new JLabel("Segundos"));
		panelDataFinal.add(txtSecF);
		panelRelFluxo.add(btPesquisar);

		add(panelRelFluxo);
		add(panelTabela);
	}

	public Container gePanelTabela() {
		// TODO Auto-generated method stub
		return panelTabela;
	}

	public Date getDataI() {
		// TODO Auto-generated method stub
		return jxDataI.getDate();
	}

	public Date getDataF() {
		// TODO Auto-generated method stub
		return jxDataF.getDate();
	}

	public String getHoraI() {
		// TODO Auto-generated method stub
		return txtHoraI.getText();
	}

	public String getHoraF() {
		// TODO Auto-generated method stub
		return txtHoraF.getText();
	}

	public String getMinI() {
		// TODO Auto-generated method stub
		return txtMinI.getText();
	}

	public String getMinF() {
		// TODO Auto-generated method stub
		return txtMinF.getText();
	}

	public String getSecI() {
		// TODO Auto-generated method stub
		return txtSecI.getText();
	}

	public String getSecF() {
		// TODO Auto-generated method stub
		return txtSecF.getText();
	}

	public Db getConexao(){
		return conexao;
	}
}
