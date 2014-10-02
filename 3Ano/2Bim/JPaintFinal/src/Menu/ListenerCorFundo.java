package Menu;

import Ferramentas.Mensagem;

public class ListenerCorFundo implements ListenerMenu{
	private Controller controller;
	
	public ListenerCorFundo(Controller controller){
		this.controller = controller;
	}
	
	public void eventoMenu(int numOp) {
		switch(numOp){
		case 1:
			Mensagem.println("Selecione uma cor de Fundo na Paleta de Cores...");
			controller.setCorFundo();
			break;
		case 2:				
			controller.setCorFundo(true);
			break;
		}
	}
}