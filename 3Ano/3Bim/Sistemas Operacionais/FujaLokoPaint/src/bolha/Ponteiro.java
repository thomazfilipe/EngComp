package bolha;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import Tools.ThreadAux;


public class Ponteiro implements Runnable{
	
	private int id;
	private Dimension tamanho;
	private Point posicao;
	private boolean stop;
	private ModeloGrafico modelo;
	private Thread t;
	private boolean keyboard;
	private boolean serial;
	private int velocidadePonteiro = 20;
	private int velocidadePonteiroSerial = 100;
	private Image image;
	
	public Ponteiro(Dimension tamanho, Point posicao, ModeloGrafico modelo){
		
		try {
			URL resource = Ponteiro.class.getResource("/resources/cursor.png");		
			image = (Image) ImageIO.read(resource);		
		} catch(Exception e) {
			System.out.println("Image can't be loaded.");
		}
		
		this.tamanho = tamanho;
		this.posicao = posicao;
		this.modelo = modelo;
		keyboard = false;
		serial = false;
		id = new Random(10).nextInt();
	}
	
	public boolean isSerial(){
		return serial;
	}
	
	public void setSerial(boolean serial){
		this.serial = serial;
	}
	
	public void setImage(Image ii){
		if(ii != null)
			image = ii;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public boolean isKeyboard(){
		return keyboard;
	}
	
	public int getVelocidade(){
		return velocidadePonteiro;
	}
	
	public void setKeyboard(boolean use){
		keyboard = use;
	}
	
	public boolean getKeyboard(){
		return keyboard;
	}
	
	public void start ()
	{
		if (t == null)
	    {			
	        t = new Thread (this);
	        t.start ();
	    }
	}	
	
	@Override
	public void run() {
		ThreadAux.Pausar(1000);
		stop = false;
		while(!stop){
			List<Bolha> bolhas = modelo.getBolhas();
			//percorre a lista de bolhas
			for(int i = 0; i < bolhas.size(); i++){
				//verifica a colisão da bolha selecionada com o ponteiro
				if(bolhas.get(i).checaColisao(posicao, false)){
					bolhas.get(i).eventoColisao(false, posicao);
				}
				
			}
			//ThreadAux.Pausar(10);
		}
		
	}
	
	public void desenhar(Graphics g) {		
		g.setColor(Color.BLACK);
	    g.drawString("Posição do PONTEIRO MALDITO ["+id+"]: " + posicao.x +" | " + posicao.y, (300 * id) - 290, 10);
		g.drawImage(image, posicao.x, posicao.y,tamanho.width,tamanho.height, null);
	}
	
	//seta a posição do cursor
	public void setPosicao(Point posicao){
			if(posicao != null)
				this.posicao = posicao;
	}
	
	public void stop(){
		stop = true;
	}

	public Point getPosicao() {
		// TODO Auto-generated method stub
		return posicao;
	}
	
	public int getID(){
		return id;
	}
	 public int getVelocidadeSerial(){
		 return velocidadePonteiroSerial;
	 }
}
