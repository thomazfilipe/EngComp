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
		//se não digitou a porta
		if(tab.getPorta().isEmpty()){
			JOptionPane.showMessageDialog(null,
				    "A porta do servidor deve ser entre 1024 a 65535.",
				    "Porta Inválida",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		int porta = Integer.parseInt(tab.getPorta());
		//valida se está usando uma porta válida
		if(porta < 1024 || porta > 65535){
			JOptionPane.showMessageDialog(null,
				    "A porta do servidor deve ser entre 1024 a 65535.",
				    "Porta Inválida",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		//valida se o campo de usuario e senha não estão em branco
		else if(usuario.isEmpty() && senha.isEmpty())
		{
			JOptionPane.showMessageDialog(null,
				    "Usuario e/ou senha inválido(s), verifique as informações de usuário e senha.",
				    "Falha na Autenticação",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		//se todos os dados estão preenchidos corretamente
		else
		{
			//se conseguir logar
			if(tab.getUsers().login(usuario, senha))
			{
				try {
					//cria o servidor com criptografia de 1024 bits
					tab.getServidor().start(Integer.parseInt(tab.getPorta()), 1024);
					//Imprimir.linha("Servidor Iniciou...");
					
					//habilita as tabs e desabilita a tab de configuração
					tab.getTabs().setEnabled(true);
					tab.getTabs().setEnabledAt(3, false);
					tab.getTabs().setSelectedIndex(0);
					//define o Label na Tab Monitor 
					((TabMonitor)tab.getTabs().getSelectedComponent()).setLaberUser("Você está logado como [ " + tab.getUsers().getUser() + " ]");
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
					    "Ocorreu um erro na autenticação do operador, verifique as informações de usuário e senha.",
					    "Falha na Autenticação",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
