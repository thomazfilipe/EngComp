package tools;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class PopUpComandos extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel painel;

	public PopUpComandos(){
		setLayout(new GridLayout(0,1));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 650);
		setResizable(false);
		setLocation(centrarFrame(this));
		
		painel = new JPanel();
		JScrollPane scroll = new JScrollPane();
		  
		painel.setLayout(new GridLayout(0,1));  
		scroll.setBounds(50, 50, 650, 450);  
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
		scroll.setViewportBorder(BorderFactory.createLoweredBevelBorder());  
		scroll.setAutoscrolls(true);  
		scroll.setViewportView(painel);  
		  
		getContentPane().add(scroll);  
	}
	
	public void addLabels(String id, String descricao){
		JPanel jpcmd = new JPanel(new FlowLayout(FlowLayout.LEFT));
		TitledBorder bf = BorderFactory.createTitledBorder(id);
		bf.setTitleFont(new Font("Tahoma", Font.BOLD, 15));
		jpcmd.setBorder(bf);
		JLabel lbcmd1Descr = new JLabel(" - " + descricao);
		jpcmd.add(lbcmd1Descr);
		painel.add(jpcmd);
	}
	
	public void ativar(){
		setVisible(true);
	}
	
	private Point centrarFrame(JFrame frame) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension tamanhoTela = kit.getScreenSize();

		int width = tamanhoTela.width;
		int height = tamanhoTela.height;

		return new Point((width / 2) - (frame.getSize().width / 2), (height / 2)
				- (frame.getSize().height / 2));
	}
}
