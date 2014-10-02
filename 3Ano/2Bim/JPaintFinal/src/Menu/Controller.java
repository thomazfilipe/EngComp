package Menu;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JColorChooser;

import Ferramentas.Keyboard;
import paint.model.ModeloGrafico;
import paint.model.Primitiva;

public class Controller {
	private Color corLinha;
	private Color corPreenchimento;
	private int espessuraLinha;
	private Color corFundo;
	private int primitivaSelecionada;
	
	private ModeloGrafico modelo;
	// outras configuracoes padroes
	
	public Controller(ModeloGrafico modelo) {
		this.modelo = modelo;
		
		this.corLinha = Color.BLACK;
		this.corPreenchimento = null;
		this.espessuraLinha = 5;
		this.corFundo = null;
		this.primitivaSelecionada = -1;
	}
	
	public void criar(Class<Primitiva> clazz) {	
		Primitiva p = null;
		try {
			p = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		p.setCorLinha(corLinha);
		p.setCorPreenchimento(corPreenchimento);
		p.setEspessuraLinha(espessuraLinha);

		for (int i = 0; i < p.getQtddPontos(); i++) {
			System.out.println("Entre com os valores do " + (i+1) + "o. ponto:");
			int x = Keyboard.readInt();
			int y = Keyboard.readInt();
			p.add(new Point(x, y));
		}
		
		modelo.add(p);		
		primitivaSelecionada = modelo.getPrimitivas().size() - 1;
	}

	public void setCorLinha(){  
		corLinha = JColorChooser.showDialog(null, "Selecione uma cor", Color.BLACK);
	}
	
	public void setCorPreenchimento(){
		corPreenchimento = JColorChooser.showDialog(null, "Selecione uma cor",Color.BLACK);
	}
	
	public void setCorFundo(){
		corFundo = JColorChooser.showDialog(null, "Selecione uma cor", Color.WHITE);
		modelo.setColorBG(corFundo);
	}

	public String getCorLinhaString() {
		String nomeCor = "";
		nomeCor = corLinha.toString();
		return nomeCor;
	}
	
	public String getCorPreenchimentoString() {
		String nomeCor = "";
		nomeCor = corPreenchimento.toString();
		return nomeCor;
	}
	
	public String getCorFundo() {
		String nomeCor = "";
		nomeCor = corFundo.toString();
		return nomeCor;
	}

	public void setCorFundo(boolean remover) {
		if(remover)
			corFundo = null;
		else
			corFundo = JColorChooser.showDialog(null, "Selecione uma cor de Fundo", Color.WHITE);
		
		modelo.setColorBG(corFundo);
	}

	public void setCorLinha (boolean remover) {
		if(remover)
			corLinha = null;
		else
			corLinha = JColorChooser.showDialog(null, "Selecione uma cor de Linha", Color.BLACK);
		
		modelo.setColorBG(corLinha);
		
	}
	
	public void setCorPreenchimento (boolean remover) {
		if(remover)
			corPreenchimento = null;
		else
			corPreenchimento = JColorChooser.showDialog(null, "Selecione uma cor de Preenchimento", Color.BLACK);
		
		modelo.setColorBG(corPreenchimento);
		
	}
	
	public void setEspessuraLinha (int espessuraLinha) {
		this.espessuraLinha = espessuraLinha;		
	}

	public int getPrimitivaSelecionada() {
		return primitivaSelecionada;
	}
	
	public String getNomePrimitivaSelecionada() throws ArrayIndexOutOfBoundsException{
		Primitiva atual = modelo.getPrimitivas().get(primitivaSelecionada);
		return "[" + (primitivaSelecionada + 1) + "] " + atual.getNome();
	}
	
	public void setPrimitivaSelecionada(int id) {
		primitivaSelecionada = id;
	}

	public void ordemPrimitivaSel(int pos) throws ArrayIndexOutOfBoundsException{
			modelo.ordemPrimitivas(primitivaSelecionada, pos);
			primitivaSelecionada = pos;
	}

	public void setCorLinhaSelecionada ()throws ArrayIndexOutOfBoundsException {
		Primitiva tmp = modelo.getPrimitivas().get(primitivaSelecionada);
		tmp.setCorLinha(corLinha);
		modelo.setPrimitiva(tmp, primitivaSelecionada);
	}
	
	public void setCorPreenchimentoSelecionada () throws ArrayIndexOutOfBoundsException{
		Primitiva tmp = modelo.getPrimitivas().get(primitivaSelecionada);
		tmp.setCorPreenchimento(corPreenchimento);
		modelo.setPrimitiva(tmp, primitivaSelecionada);
		
	}
	
	public void setEspessuraLinhaSelecionada (int espessuraLinha) throws ArrayIndexOutOfBoundsException{
		Primitiva tmp = modelo.getPrimitivas().get(primitivaSelecionada);
		tmp.setEspessuraLinha(espessuraLinha);
		modelo.setPrimitiva(tmp, primitivaSelecionada);
		this.espessuraLinha = espessuraLinha;		
	}

	public void setPosSelecionada() throws ArrayIndexOutOfBoundsException{
		Primitiva p = modelo.getPrimitivas().get(primitivaSelecionada);
		p.removeAllPoints();
		for (int i = 0; i < p.getQtddPontos(); i++) {
			System.out.println("Entre com os valores do " + (i+1) + "o. ponto:");
			int x = Keyboard.readInt();
			int y = Keyboard.readInt();
			p.add(new Point(x, y));
		}
		
		modelo.setPrimitiva(p, primitivaSelecionada);		
	}

	public void removerSelecionada() {		
		modelo.remove(primitivaSelecionada);	
		primitivaSelecionada = -1;
	}

	public void setModeloGrafico(ModeloGrafico modelo){
		this.modelo = modelo;
	}
}





