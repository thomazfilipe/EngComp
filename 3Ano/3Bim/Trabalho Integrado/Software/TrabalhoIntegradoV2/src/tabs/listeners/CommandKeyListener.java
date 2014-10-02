package tabs.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import regra.Regras;
import tabs.TabMonitor;

public class CommandKeyListener implements KeyListener {

	private TabMonitor tab;
	private Regras regras;
	
	public CommandKeyListener(TabMonitor tab) {
		this.tab = tab;
		regras = new Regras(tab.getConexao(), tab.getUsers(), tab.getServidor());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyReleased(KeyEvent e) {
		//se foi soltado o ENTER
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			//se estiver conectado ao Ponto
			if(tab.getServidor().isRunning())
				tab.setStatus(regras.setRegra(tab));
			else
				JOptionPane.showMessageDialog(null, "Não ha nenhuma conexão ativa!", "Conexão", JOptionPane.INFORMATION_MESSAGE);
			
			tab.setComando("");//apaga o que tem escrito no Jtextfield
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
