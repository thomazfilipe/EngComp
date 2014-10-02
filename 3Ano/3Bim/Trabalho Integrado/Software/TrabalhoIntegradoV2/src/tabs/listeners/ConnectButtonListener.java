package tabs.listeners;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import tabs.TabConfiguracao;
import tabs.TabMonitor;

public class ConnectButtonListener implements ActionListener{

	private TabConfiguracao tab;
	
	public ConnectButtonListener(TabConfiguracao tab) {
		this.tab = tab;
	}

	public void actionPerformed(ActionEvent e) {
		//pega o usuario e senha escrito na tab
		String usuario = tab.getUsuario();
		String senha = tab.getSenha();
		//se n�o digitou a porta
		if(tab.getPorta().isEmpty()){
			JOptionPane.showMessageDialog(null,
				    "A porta do servidor deve ser entre 1024 a 65535.",
				    "Porta Inv�lida",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		int porta = Integer.parseInt(tab.getPorta());
		//valida se est� usando uma porta v�lida
		if(porta < 1024 || porta > 65535){
			JOptionPane.showMessageDialog(null,
				    "A porta do servidor deve ser entre 1024 a 65535.",
				    "Porta Inv�lida",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		//valida se o campo de usuario e senha n�o est�o em branco
		else if(usuario.isEmpty() && senha.isEmpty())
		{
			JOptionPane.showMessageDialog(null,
				    "Usuario e/ou senha inv�lido(s), verifique as informa��es de usu�rio e senha.",
				    "Falha na Autentica��o",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		//se todos os dados est�o preenchidos corretamente
		else
		{
			//se conseguir logar
			if(tab.getUsers().login(usuario, senha))
			{
				try {
					//cria o servidor com criptografia de 1024 bits
					tab.getServidor().start(Integer.parseInt(tab.getPorta()), 1024);
					//Imprimir.linha("Servidor Iniciou...");
					
					//habilita as tabs e desabilita a tab de configura��o
					tab.getTabs().setEnabled(true);
					tab.getTabs().setEnabledAt(3, false);
					tab.getTabs().setSelectedIndex(0);
					//define o Label na Tab Monitor 
					((TabMonitor)tab.getTabs().getSelectedComponent()).setLaberUser("Voc� est� logado como [ " + tab.getUsers().getUser() + " ]");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"Ocorreu um erro ao iniciar o servidor: " + e1.getMessage(),
							"Erro", 
							JOptionPane.ERROR_MESSAGE);
				}		
			}
			//se os dados de login estiver errado
			else
			{
				JOptionPane.showMessageDialog(null,
					    "Ocorreu um erro na autentica��o do operador, verifique as informa��es de usu�rio e senha.",
					    "Falha na Autentica��o",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
