package Menu;

import Ferramentas.Mensagem;

public class ListenerCorLinha implements ListenerMenu{
	private Controller controller;
	
	public ListenerCorLinha(Controller controller){
		this.controller = controller;
	}
	
	public void eventoMenu(int numOp) {
		switch(numOp){
		case 1:
			Mensagem.println("Selecione uma cor de Fundo na Paleta de Cores...");
			controller.setCorLinha();
			break;
		case 2:				
			controller.setCorLinha(true);
			break;
		}
	}
}
