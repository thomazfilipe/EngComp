package Menu;

public class ListenerSelecionar implements ListenerMenu {
	
	private Controller controller;

	public ListenerSelecionar(Controller controller){
		this.controller = controller;
	}
	
	public void eventoMenu(int numOp) {
		
		controller.setPrimitivaSelecionada(numOp -1);
	}

}
