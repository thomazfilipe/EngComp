package paint;

import paint.model.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

// VIEW

@SuppressWarnings("serial")
public class PainelGrafico extends JPanel implements Observer {
	private ModeloGrafico modelo;
	
	public PainelGrafico(ModeloGrafico modelo) {
		this.modelo = modelo;
		modelo.addObserver(this);
	}
	
	public Dimension getPreferredSize() {
		return modelo.getDimension();
	}
	
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);		
			BufferedImage b = null;
			try {
				// pega a imagem dentro do pacote
				b = ImageIO.read(getClass().getResourceAsStream(
						"/resources/grid1.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Rectangle2D rect = new Rectangle(0, 0, 192, 192);
			//crio a repetição da imagem carregada
			TexturePaint p = new TexturePaint(b, rect);
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(p);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			if(modelo.getColorBG() != null)
				g.setColor(modelo.getColorBG());			
			// transpor os pontos?
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (Primitiva p1 : modelo.getPrimitivas()) {
			p1.desenhar(g);
		}
	}
}







