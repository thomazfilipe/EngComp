package Menu;

import paint.model.Primitiva;

public class ListenerPrimitivas implements ListenerMenu {
	private Controller controller;
	private Class<Primitiva> clazz;

	public ListenerPrimitivas(Controller controller, Class<Primitiva> clazz) {
		this.controller = controller;
		this.clazz = clazz;
	}
	
	public void eventoMenu(int numOp) {
		controller.criar(clazz);		
	}
}