package bolha;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import Ferramentas.AbstractScreen;

@SuppressWarnings("serial")
public class Screen extends AbstractScreen {

	private ModeloGrafico modelo;
	private JPanel painelGrafico;
	private Point captureMouse;
	private char[] secret = new char[6];
	private boolean isSecret = false;

	public Screen(ModeloGrafico modelo, int fps) {
		this.modelo = modelo;
		int val = 1000 / fps;
		painelGrafico = new PainelGrafico(modelo, val);
		setUndecorated(true);
		add(painelGrafico);

		addKeyListener(this);
		addMouseMotionListener(this);
	}

	public void mouseMoved(MouseEvent e) {
		List<Ponteiro> ponteiros = modelo.getPonteiros();
		Point captureMouseTmp = new Point(e.getPoint().x, e.getPoint().y);
		boolean move = true;
		for(int i = 0 ; i < modelo.getBolhas().size(); i++){
			if(modelo.getBolhas().get(i).checaColisaoInter(captureMouseTmp,false)){
				move = false;
				break;
			}
		}
		if(move)
			captureMouse = captureMouseTmp;

		for (int i = 0; i < ponteiros.size(); i++) {
			if(!ponteiros.get(i).isKeyboard() && !ponteiros.get(i).isSerial()){
				ponteiros.get(i).setPosicao(captureMouse);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		List<Ponteiro> ponteiros = modelo.getPonteiros();

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			for (int i = 0; i < ponteiros.size(); i++) {
				if (ponteiros.get(i).isKeyboard() && !ponteiros.get(i).isSerial()) {
					int x = ponteiros.get(i).getPosicao().x;
					int y = ponteiros.get(i).getPosicao().y;
					if (ponteiros.get(i).getPosicao().y > 0)
						y -= ponteiros.get(i).getVelocidade();
					///verifica se pode mover
					boolean move = true;
					for(int j = 0 ; j < modelo.getBolhas().size(); j++){
						if(modelo.getBolhas().get(j).checaColisaoInter(new Point(x,y),false)){
							move = false;
							break;
						}
					}
					if(move)
						ponteiros.get(i).setPosicao(new Point(x, y));
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			for (int i = 0; i < ponteiros.size(); i++) {
				if (ponteiros.get(i).isKeyboard() && !ponteiros.get(i).isSerial()) {
					int x = ponteiros.get(i).getPosicao().x;
					int y = ponteiros.get(i).getPosicao().y;
					if (ponteiros.get(i).getPosicao().y < Toolkit
							.getDefaultToolkit().getScreenSize().height - 20)
						y += ponteiros.get(i).getVelocidade();

					boolean move = true;
					for(int j = 0 ; j < modelo.getBolhas().size(); j++){
						if(modelo.getBolhas().get(j).checaColisaoInter(new Point(x,y),false)){
							move = false;
							break;
						}
					}
					if(move)
						ponteiros.get(i).setPosicao(new Point(x, y));
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			for (int i = 0; i < ponteiros.size(); i++) {
				if (ponteiros.get(i).isKeyboard() && !ponteiros.get(i).isSerial()) {
					int x = ponteiros.get(i).getPosicao().x;
					int y = ponteiros.get(i).getPosicao().y;
					if (ponteiros.get(i).getPosicao().x > 0)
						x -= ponteiros.get(i).getVelocidade();

					boolean move = true;
					for(int j = 0 ; j < modelo.getBolhas().size(); j++){
						if(modelo.getBolhas().get(j).checaColisaoInter(new Point(x,y),false)){
							move = false;
							break;
						}
					}
					if(move)
						ponteiros.get(i).setPosicao(new Point(x, y));
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			for (int i = 0; i < ponteiros.size(); i++) {
				if (ponteiros.get(i).isKeyboard() && !ponteiros.get(i).isSerial()) {
					int x = ponteiros.get(i).getPosicao().x;
					int y = ponteiros.get(i).getPosicao().y;
					if (ponteiros.get(i).getPosicao().x < Toolkit
							.getDefaultToolkit().getScreenSize().width - 20)
						x += ponteiros.get(i).getVelocidade();

					boolean move = true;
					for(int j = 0 ; j < modelo.getBolhas().size(); j++){
						if(modelo.getBolhas().get(j).checaColisaoInter(new Point(x,y),false)){
							move = false;
							break;
						}
					}
					if(move)
						ponteiros.get(i).setPosicao(new Point(x, y));
				}
			}
		}
		
		////////////SECRETS
		//swap			
		secret[5] = secret[4];
		secret[4] = secret[3];
		secret[3] = secret[2];	
		secret[2] = secret[1];
		secret[1] = secret[0];	
		secret[0] = e.getKeyChar();	
		///duds
		if(secret[3] == 'd' && 
				secret[2] == 'u' &&
				secret[1] == 'd' &&
				secret[0] == 's' && !isSecret){
			modelo.secretBolha();
			isSecret = true;
		}	
		else if(secret[3] == 'm' && 
				secret[2] == 'e' &&
				secret[1] == 'm' &&
				secret[0] == 'e' && !isSecret){
			modelo.secretMeme();
			isSecret = true;
		}
		///return
		else if(secret[5] == 'r' &&
				secret[4] == 'e' &&
				secret[3] == 't' &&
				secret[2] == 'u' &&
				secret[1] == 'r' &&
				secret[0] == 'n' && isSecret){
			modelo.returnImage();
			isSecret = false;
		}
		/////////////////////////
		/////ESC para Sair
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			int result = JOptionPane.showConfirmDialog(this, "Deseja fechar o programa?");
			if (result == JOptionPane.OK_OPTION) {
				modelo.stopThreads();
				modelo.stopMonitor();
				System.exit(0);
			}			
		}
	}
}