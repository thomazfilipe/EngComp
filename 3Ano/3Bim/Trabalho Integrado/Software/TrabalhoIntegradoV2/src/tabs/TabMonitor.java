package tabs;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.server.Servidor;
import tabs.listeners.AjudaListener;
import tabs.listeners.CommandKeyListener;
import users.Usuarios;
import database.Db;

public class TabMonitor extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ModeloGrafico myChartPanel;
	private Db conexao;
	private JPanel commandLine;
	private JLabel labelComando;
	private JTextField txtCommandLine;
	private JLabel lCommand;
	private Usuarios users;
	private Servidor servidor;
	private JLabel lUsuario;

	public TabMonitor(Db conexao, Usuarios users, Servidor servidor) {
		this.conexao = conexao;
		this.users = users;
		this.servidor = servidor;
		setLayout(new FlowLayout());
		
		lUsuario = new JLabel();
		add(lUsuario);
		// cria um panel com o grafico
		myChartPanel = new ModeloGrafico(conexao, servidor);
		myChartPanel.setSize(800, 600);
		myChartPanel.setVisible(true);
		add(myChartPanel);
		// /fim gráfco///

		// //adicionando command line
		commandLine = new JPanel(new GridLayout(0, 1));
		labelComando = new JLabel("Entrada de comandos:");
		txtCommandLine = new JTextField(50);
		lCommand = new JLabel("");
		txtCommandLine.addKeyListener(new CommandKeyListener(this));
		txtCommandLine.setSize(200, 50);
		JButton btAjuda = new JButton("Ajuda");
		btAjuda.addActionListener(new AjudaListener());
		JPanel jpLay = new JPanel(new GridLayout(0,2));
		jpLay.add(labelComando);
		jpLay.add(btAjuda);
		commandLine.add(jpLay);
		commandLine.add(txtCommandLine);
		commandLine.add(lCommand);
		
		add(commandLine);
	}

	public Db getConexao() {
		return conexao;
	}

	public Usuarios getUsers() {
		return users;
	}

	public Servidor getServidor() {
		return servidor;
	}


	public String getComando() {
		return txtCommandLine.getText();
	}

	
	public void setComando(String comando) {
		txtCommandLine.setText(comando);
	}

	public void setStatus(String setRegra) {
		lCommand.setText(setRegra);		
	}
	
	public void setLaberUser(String usuario){
		lUsuario.setText(usuario);
	}

}
