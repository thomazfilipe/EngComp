import gnu.io.CommPortIdentifier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import bolha.IconPonteiro;

public class MainSerial4 {

	static JButton bStart;
	static JFrame fApp;
	static JTextField txtNBolhas;
	static JLabel jlNBolhas;
	static JCheckBox jcP1;
	static JCheckBox jcP2;
	static JRadioButton jrbSC;
	static JRadioButton jrbM;
	static JRadioButton jrbK;

	// / config serial
	static Enumeration<?> listaDePortas = CommPortIdentifier
			.getPortIdentifiers();
	static JComboBox<String> lstPortas = new JComboBox<>();

	public static void main(String[] args) {
		// adicionando portas no ComboBox
		while (listaDePortas.hasMoreElements()) {
			CommPortIdentifier ips = (CommPortIdentifier) listaDePortas
					.nextElement();
			lstPortas.addItem(ips.getName());
		}
		fApp = new JFrame("Bolhas Malditas - Configurações");
		fApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fApp.setLocationRelativeTo(null);
		fApp.setLayout(new GridLayout(1, 3));

		// cria layout para inserir os componentes no frame
		JPanel jp1 = new JPanel(new GridLayout(5, 1));
		jp1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		fApp.add(jp1);
		// componentes de configuração da bolha
		jlNBolhas = new JLabel("numero de bolhas");
		txtNBolhas = new JTextField();
		txtNBolhas.setFont(new Font("Dialog", Font.BOLD, 24));
		// ////////////////
		bStart = new JButton("Iniciar");
		bStart.addActionListener(new buttonAction());
		jp1.add(jlNBolhas);
		jp1.add(txtNBolhas);
		JPanel jpSerial = new JPanel(new FlowLayout());
		jpSerial.add(new JLabel("Porta Serial"));
		jpSerial.add(lstPortas);
		jp1.add(jpSerial);
		jp1.add(bStart);
		fApp.pack();
		CentrarFrame();
		fApp.setVisible(true);

	}

	public static void CentrarFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension tamanhoTela = kit.getScreenSize();

		int width = tamanhoTela.width;
		int height = tamanhoTela.height;

		fApp.setLocation((width / 2) - (fApp.getSize().width / 2), (height / 2)
				- (fApp.getSize().height / 2));
	}

	public static class buttonAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// pegando os dados
			if (Integer.parseInt(txtNBolhas.getText()) > 9
					&& Integer.parseInt(txtNBolhas.getText()) < 51) {
				bStart.setEnabled(false);
				String nBolhas = txtNBolhas.getText();
				Image bolhaIcon = null;
				Image p1Icon = null;
				boolean serial1 = true;
				boolean pcControl1 = false;
				Image p2Icon = new IconPonteiro().getSelectedImage();
				boolean serial2 = true;
				boolean pcControl2 = false;
				String com = lstPortas.getSelectedItem().toString();
				int baud = 19200;

				// //carregar um loading aqui

				LoadModelo4 loadmodelo = new LoadModelo4(nBolhas, bolhaIcon,
						p1Icon, serial1, pcControl1, p2Icon, serial2,
						pcControl2, com, baud);

				@SuppressWarnings("unused")
				Principal p = new Principal(loadmodelo.getModelo());
				fApp.setVisible(false);
				fApp.dispose();
			} else
				JOptionPane.showMessageDialog(null,
						"A quantidade de bolhas deve ser entre 10 e 50.",
						"Quantidade de bolhas inválida",
						JOptionPane.PLAIN_MESSAGE);
		}
	}

}
