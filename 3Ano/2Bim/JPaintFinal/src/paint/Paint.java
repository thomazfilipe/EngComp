package paint;

import paint.model.*;

import javax.swing.*;

// FUTURO CONTROLLER

@SuppressWarnings("serial")
public class Paint extends JFrame {
	@SuppressWarnings("unused")
	private ModeloGrafico modelo;
	private JPanel painelGrafico;
	
	public Paint(ModeloGrafico modelo) {
		this.modelo = modelo;
		painelGrafico = new PainelGrafico(modelo);
		add(painelGrafico);
	}
}