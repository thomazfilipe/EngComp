package Menu;

import Ferramentas.Mensagem;

public class ListenerCorPreenchimento implements ListenerMenu{
	private Controller controller;
	
	public ListenerCorPreenchimento(Controller controller){
		this.controller = controller;
	}
	
	public void eventoMenu(int numOp) {
		switch(numOp){
		case 1:
			Mensagem.println("Selecione uma cor de Fundo na Paleta de Cores...");
			controller.setCorPreenchimento();
			break;
		case 2:				
			controller.setCorPreenchimento(true);
			break;
		}
	}
}