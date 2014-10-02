package tabs;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import net.server.Servidor;
import tabs.listeners.ConnectButtonListener;
import tools.LimiteCaracteres;
import tools.OnlyNumberKeyListener;
import users.Usuarios;

public class TabConfiguracao extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelPorta;
	private JTextField txtPorta;
	private JLabel labelUsuarioL;
	private JTextField txtUsuarioL;
	private JLabel labelSenhaL;
	private JPasswordField txtSenhaL;
	private JButton btIniciarServidor;
	private JTabbedPane tabs;
	private Servidor servidor;
	private Usuarios users;

	public TabConfiguracao(JTabbedPane tabs, Servidor servidor, Usuarios users) {
		// /layout tab configuração///
		this.tabs = tabs;
		this.servidor = servidor;
		this.users = users;
		
		labelPorta = new JLabel("Porta");
		txtPorta = new JTextField(5);//tamanho do JTextField
		txtPorta.setDocument(new LimiteCaracteres(5));//Limite de caracteres = 5
		txtPorta.addKeyListener(new OnlyNumberKeyListener());//Listener para digitar apenas numeros
		labelUsuarioL = new JLabel("Usuario");
		txtUsuarioL = new JTextField(6);
		txtUsuarioL.setDocument(new LimiteCaracteres(10));
		labelSenhaL = new JLabel("Senha");
		txtSenhaL = new JPasswordField(6);
		txtUsuarioL.setDocument(new LimiteCaracteres(64));
		btIniciarServidor = new JButton("Iniciar Servidor");
		btIniciarServidor.addActionListener(new ConnectButtonListener(this));
		add(labelPorta);
		add(txtPorta);
		add(labelUsuarioL);
		add(txtUsuarioL);
		add(labelSenhaL);
		add(txtSenhaL);
		add(btIniciarServidor);
	}

	public String getUsuario() {
		return txtUsuarioL.getText();
	}

	@SuppressWarnings("deprecation")
	public String getSenha() {
		return txtSenhaL.getText();
	}

	public String getPorta() {
		return txtPorta.getText();
	}

	public Servidor getServidor() {
		return servidor;
	}

	public JTabbedPane getTabs() {
		return tabs;
	}

	public Usuarios getUsers() {
		return users;
	}

}
