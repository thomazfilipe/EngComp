import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import bolha.IconBolha;
import bolha.IconList;
import bolha.IconPonteiro;

public class Main2Pic {

	static JButton bStart;
	static JFrame fApp;
	static JTextField txtNBolhas;
	static JLabel jlNBolhas;
	static JCheckBox jcP1;
	static JCheckBox jcP2;
	static JRadioButton jrbSC;
	static JRadioButton jrbM;
	static JRadioButton jrbK;
	static ButtonGroup bgController1;
	static ButtonGroup bgController2;

	// //JPanel Bolhas e Ponteiros
	static JLabel jlBolha;
	static IconBolha iconB = new IconBolha();
	static JLabel jlPonteiro1;
	static IconPonteiro iconP1 = new IconPonteiro();
	static JLabel jlPonteiro2;
	static IconPonteiro iconP2 = new IconPonteiro();
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
		JPanel jp2 = new JPanel(new GridLayout(5, 1));
		jp2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		JPanel jp3 = new JPanel(new GridLayout(5, 1));
		jp3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		fApp.add(jp1);
		fApp.add(jp2);
		fApp.add(jp3);
		// componentes de configuração da bolha
		jlNBolhas = new JLabel("numero de bolhas");
		txtNBolhas = new JTextField();
		txtNBolhas.setFont(new Font("Dialog", Font.BOLD, 24));
		// Layout bolhas
		JPanel jpBolhaLay = new JPanel(new FlowLayout());
		bStart = new JButton("<");
		jlBolha = new JLabel(iconB.next());
		bStart.addActionListener(new previousIconAction(iconB, jlBolha));
		jpBolhaLay.add(bStart, BorderLayout.EAST);
		jpBolhaLay.add(jlBolha);
		bStart = new JButton(">");
		bStart.addActionListener(new nextIconAction(iconB, jlBolha));
		jpBolhaLay.add(bStart, BorderLayout.WEST);
		// ////////////////
		bStart = new JButton("Iniciar");
		bStart.addActionListener(new buttonAction());
		jp1.add(jlNBolhas);
		jp1.add(txtNBolhas);
		jp1.add(jpBolhaLay);
		JPanel jpSerial = new JPanel(new FlowLayout());
		jpSerial.add(new JLabel("Porta Serial"));
		jpSerial.add(lstPortas);
		jp1.add(jpSerial);
		jp1.add(bStart);

		// ////
		// //configuração do ponteiro P1
		jlNBolhas = new JLabel("Ponteiro 1");
		jcP1 = new JCheckBox("Ativar", true);

		jrbM = new JRadioButton("Ativar Mouse Controller", true);
		jrbK = new JRadioButton("Ativar Teclado Controller", false);
		jrbSC = new JRadioButton("Ativar Serial Controller", false);
		bgController1 = new ButtonGroup();
		bgController1.add(jrbM);
		bgController1.add(jrbK);
		bgController1.add(jrbSC);

		JPanel controller = new JPanel(new GridLayout(3, 1));
		controller.add(jrbK);
		controller.add(jrbM);
		controller.add(jrbSC);
		// /
		JPanel jpPonteiroLay = new JPanel(new FlowLayout());
		jlPonteiro1 = new JLabel(iconP1.next());
		//
		bStart = new JButton("<");
		bStart.addActionListener(new previousIconAction(iconP1, jlPonteiro1));
		jpPonteiroLay.add(bStart, BorderLayout.EAST);
		jpPonteiroLay.add(jlPonteiro1);
		//
		bStart = new JButton(">");
		bStart.addActionListener(new nextIconAction(iconP1, jlPonteiro1));
		jpPonteiroLay.add(bStart, BorderLayout.WEST);
		// /
		jcP1.setEnabled(false);

		jp2.add(jlNBolhas);
		jp2.add(jcP1);
		jp2.add(jpPonteiroLay);
		jp2.add(controller);

		// //configuração do ponteiro P2
		jlNBolhas = new JLabel("Ponteiro 2");
		jcP2 = new JCheckBox("Ativar");

		jrbM = new JRadioButton("Ativar Mouse Controller", false);
		jrbK = new JRadioButton("Ativar Teclado Controller", true);
		jrbSC = new JRadioButton("Ativar Serial Controller", false);
		bgController2 = new ButtonGroup();
		bgController2.add(jrbM);
		bgController2.add(jrbK);
		bgController2.add(jrbSC);

		controller = new JPanel(new GridLayout(3, 1));
		controller.add(jrbK);
		controller.add(jrbM);
		controller.add(jrbSC);
		// /
		jpPonteiroLay = new JPanel(new FlowLayout());
		jlPonteiro2 = new JLabel(iconP2.next());
		//
		bStart = new JButton("<");
		bStart.addActionListener(new previousIconAction(iconP2, jlPonteiro2));
		jpPonteiroLay.add(bStart, BorderLayout.EAST);
		jpPonteiroLay.add(jlPonteiro2);
		//
		bStart = new JButton(">");
		bStart.addActionListener(new nextIconAction(iconP2, jlPonteiro2));
		jpPonteiroLay.add(bStart, BorderLayout.WEST);

		jp3.add(jlNBolhas);
		jp3.add(jcP2);
		jp3.add(jpPonteiroLay);
		jp3.add(controller);

		fApp.pack();
		CentrarFrame();
		fApp.setVisible(true);
		// fApp.setSize(550, 400);

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
				Image bolhaIcon = iconB.getSelectedImage();
				Image p1Icon = iconP1.getSelectedImage();
				boolean serial1 = radioSelected(bgController1).equals(
						"Ativar Serial Controller");
				boolean pcControl1 = radioSelected(bgController1).equals(
						"Ativar Teclado Controller");
				Image p2Icon = (jcP2.isSelected() ? iconP2.getSelectedImage()
						: null);
				boolean serial2 = radioSelected(bgController2).equals(
						"Ativar Serial Controller");
				boolean pcControl2 = radioSelected(bgController2).equals(
						"Ativar Teclado Controller");
				String com = lstPortas.getSelectedItem().toString();
				int baud = 19200;
				
				////carregar um loading aqui
				
				LoadModelo loadmodelo = new LoadModelo(nBolhas, bolhaIcon, p1Icon, serial1, pcControl1, p2Icon, serial2, pcControl2, com, baud);
				
				@SuppressWarnings("unused")
				Principal p = new Principal(loadmodelo.getModelo());
				fApp.setVisible(false);
			} else
				JOptionPane.showMessageDialog(null,
						"A quantidade de bolhas deve ser entre 10 e 50.",
						"Quantidade de bolhas inválida",
						JOptionPane.PLAIN_MESSAGE);
		}
	}

	public static class nextIconAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private IconList il;
		private JLabel jl;

		public nextIconAction(IconList il, JLabel jl) {
			this.il = il;
			this.jl = jl;
		}

		public void actionPerformed(ActionEvent e) {
			jl.setIcon(il.next());
		}
	}

	public static class previousIconAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private IconList il;
		private JLabel jl;

		public previousIconAction(IconList il, JLabel jl) {
			this.il = il;
			this.jl = jl;
		}

		public void actionPerformed(ActionEvent e) {
			jl.setIcon(il.previous());
		}
	}

	public static String radioSelected(ButtonGroup grupo) {
		Enumeration<AbstractButton> elementos = grupo.getElements();
		while (elementos.hasMoreElements()) {
			AbstractButton button = elementos.nextElement();
			if (button.isSelected()) {
				return button.getText();
			}
		}
		return null;
	}

	public static void CentrarFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension tamanhoTela = kit.getScreenSize();

		int width = tamanhoTela.width;
		int height = tamanhoTela.height;

		fApp.setLocation((width / 2) - (fApp.getSize().width / 2), (height / 2)
				- (fApp.getSize().height / 2));
	}

}
