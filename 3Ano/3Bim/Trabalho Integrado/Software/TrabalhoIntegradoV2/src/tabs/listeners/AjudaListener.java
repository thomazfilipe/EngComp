package tabs.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tools.PopUpComandos;

public class AjudaListener implements ActionListener {

	public void actionPerformed(ActionEvent arg0) {
		PopUpComandos pop = new PopUpComandos();
		pop.addLabels("setar X1=nn%", "seta o valor de prioridade do fluxo A em relação a B, onde 'nn' é o valor especificado.");
		pop.addLabels("setar Y1=nn%", "seta o valor de prioridade do fluxo B em relação a A, onde 'nn' é o valor especificado.");
		pop.addLabels("VIA A=ON", "Habilita sinal Verde em A e vermelho em B, por tempo indeterminado.");
		pop.addLabels("VIA A=OFF", "Ativa o modo automatico novamente.");
		pop.addLabels("VIA B=ON", "Habilita sinal Verde em B e vermelho em A, por tempo indeterminado.");
		pop.addLabels("VIA B=OFF", "Habilita o modo automatico novamente.");
		pop.addLabels("sistema on", "Ativa o supervisor enviando o standby e ativando o modo automatico.");
		pop.addLabels("sistema off", "Coloca o supervisor em standby e desativa o modo automatico.");
		pop.addLabels("user login pass senha", "Adiciona um operador normal, onde o login de no maximo 10 caracteres e a senha de no maximo 64 caracteres.");
		pop.addLabels("user login pass senha z", "Adiciona um operador onde z é o tipo de usuario. 1 para Admin e 0 para Normal, necessita operador Admin para criar outro Admin.");
		pop.addLabels("changeuser login senha", "troca o operador atual pelo especificado.");
		pop.addLabels("changepass oldpass newpass", "Troca a senha do operador atual.");
		pop.addLabels("deluser login", "Deleta um usuario, necessita operador Admin");
		pop.addLabels("getusers", "Listas os Operadores cadastrados, necessita operador Admin");
	
		pop.ativar();
	}

}
