package appc;

import gnu.io.CommPortIdentifier;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tools.LimiteCaracteres;
import tools.NumberDotKeyListener;
import tools.OnlyNumberKeyListener;
import Serial.SerialCom;
import net.MessageListener;
import net.TCPClient;
import net.impl.SecureTCPClient;

public class Cliente implements MessageListener {
	
	private static TCPClient tcpClient;
	//pega a lista de portas serial
	static Enumeration<?> listaDePortas = CommPortIdentifier
			.getPortIdentifiers();
	static JComboBox<String> lstPortas = new JComboBox<>();
	static JComboBox<String> lstBaud = new JComboBox<>();
	static SerialCom sc;
	static JFrame JApp;
	static Cliente cliente;
	static JTextField txtIp;
	static JTextField txtPorta;

	public Cliente() throws Exception {
		//cria um cliente com o ip e portas fornecidos
		tcpClient = new SecureTCPClient(txtIp.getText(), Integer.parseInt(txtPorta.getText()));
		//passa o listener deste objeto implementado
		tcpClient.setMessageListener(this);
	}

	public void start() {
		//inicia o cliente//
		tcpClient.start();
		//pega a porta e o baud e cria uma conexão serial e inicia;
		String com = lstPortas.getSelectedItem().toString();
		int baud = Integer.parseInt(lstBaud.getSelectedItem().toString());
		sc = new SerialCom(com, baud, tcpClient);
		tcpClient.setSerial(sc);
	}

	public void stop() {
		tcpClient.stop();
	}

	@Override
	public void onMessage(TCPClient client, byte[] message) {
		//System.out.println(new String(message));
		sc.send(new String(message));
	}

	public static void main(String[] args) throws Exception {
		JApp = new JFrame("Trabalho Integrado - Ponto");
		JApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JApp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (cliente != null){
					if(cliente.isConnected())
						cliente.stop();
				}
				System.exit(0);
			}
		});
		//JApp.setSize(200, 200);
		JApp.setLayout(new FlowLayout());

		// adicionando portas no ComboBox
		while (listaDePortas.hasMoreElements()) {
			CommPortIdentifier ips = (CommPortIdentifier) listaDePortas
					.nextElement();
			lstPortas.addItem(ips.getName());
		}
		///adicionando Bauds
		lstBaud.addItem("19200");
		lstBaud.addItem("1200");
		lstBaud.addItem("2400");
		lstBaud.addItem("4800");
		lstBaud.addItem("9600");
		lstBaud.addItem("38400");
		lstBaud.addItem("57600");
		lstBaud.addItem("115200");

		//criando configurações dos componentes
		JButton bStart = new JButton("Iniciar");
		bStart.addActionListener(new buttonAction());
		JPanel jpConfg = new JPanel(new FlowLayout());
		JLabel lbIP = new JLabel("IP");
		txtIp = new JTextField(15);
		txtIp.setDocument(new LimiteCaracteres(15));
		txtIp.addKeyListener(new NumberDotKeyListener(txtIp));
		JLabel lbPorta = new JLabel("Porta");
		txtPorta = new JTextField(5);
		txtPorta.setDocument(new LimiteCaracteres(5));
		txtPorta.addKeyListener(new OnlyNumberKeyListener());
		JLabel lbBaud = new JLabel("Baud");

		//adicionando componentes no panel jpConfig
		jpConfg.add(lbIP);
		jpConfg.add(txtIp);
		jpConfg.add(lbPorta);
		jpConfg.add(txtPorta);
		//adicionando componentes no panel jpSerial
		JPanel jpSerial = new JPanel(new FlowLayout());
		jpSerial.add(new JLabel("Porta Serial"));
		jpSerial.add(lstPortas);
		jpSerial.add(lbBaud);
		jpSerial.add(lstBaud);
		
		//adicionando panels no Jframe
		JApp.add(jpSerial);
		JApp.add(jpConfg);
		JApp.add(bStart);

		JApp.pack();
		JApp.setVisible(true);
	}
	
	//garante que retorna um estado 
	protected boolean isConnected() {
		return tcpClient == null? false : tcpClient.isConnected();
	}

	//ação do botão Iniciar
	public static class buttonAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			
			JApp.setVisible(false);
			
			try {
				cliente = new Cliente();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,
						e1.getMessage(),
						"Erro ao tentar Conectar no Supervisor",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}

			cliente.start();
		}
	}
}
