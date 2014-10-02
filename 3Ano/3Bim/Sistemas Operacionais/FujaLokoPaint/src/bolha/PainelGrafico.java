package bolha;

import java.awt.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import Tools.ThreadAux;

public class PainelGrafico extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ModeloGrafico modelo;
	private int fps;
	private Image bg;
	
	public PainelGrafico(ModeloGrafico modelo, int fps) {
		this.modelo = modelo;
		this.fps = fps;
		this.bg = loadBG();
	}
	
	public Dimension getPreferredSize() {
		return modelo.getDimension();
	}
	
	private Image loadBG(){
		try {
			URL resource = Ponteiro.class.getResource("/resources/bg.png");		
			return ImageIO.read(resource);		
		} catch(Exception e) {
			System.out.println("Image can't be loaded.");
		}
		return null;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		Toolkit tk = Toolkit.getDefaultToolkit();
		g.drawImage(bg, 0, 0,tk.getScreenSize().width,tk.getScreenSize().height, null);
		for (Bolha b : modelo.getBolhas()) {
			b.desenhar(g);
		}
		
		for (Ponteiro p : modelo.getPonteiros()) {
			p.desenhar(g);
		}
		ThreadAux.Pausar(fps);
		repaint();
	}
}







