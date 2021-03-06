package paint.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Point;

@SuppressWarnings("serial")
public class Quadrado extends Primitiva {
	public Quadrado() {
	}

	public void desenhar(Graphics g) {
		// e se nao tiver todos os pontos
		
		Point p1 = pontos.get(0);
		Point p2 = pontos.get(1);
		
		if (corPreenchimento != null) {
			g.setColor(corPreenchimento);			
			// transpor os pontos?
			g.fillRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
		}
		
		g.setColor(corLinha);
		((Graphics2D) g).setStroke(new BasicStroke(espessuraLinha));
		// transpor os pontos?
		g.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
	}
	
	public int getQtddPontos() { 
		return 2; 
	}
	
	public String getNome() {
		return "Quadrado/Retangulo";
	}
	
	public String getDescricao() {
		return "representa um quadrado/retangulo";
	}
		
	public String getXML() {
		String pS = null;
		for(int i = 0; i < pontos.size(); i++){
			pS += "p" + (i+1)+"=\""+ pontos.get(i).x+";"+pontos.get(i).y+"\"";
		}
		return pS;
	}
}




