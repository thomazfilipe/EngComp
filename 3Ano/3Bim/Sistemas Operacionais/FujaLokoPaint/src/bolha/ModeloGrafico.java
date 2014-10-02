package bolha;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.util.*;
import java.awt.Toolkit;

import javax.imageio.ImageIO;

public class ModeloGrafico {

	private List<Bolha> bolhas;
	private Dimension tamanho;
	private List<Ponteiro> ponteiros;
	private Monitor m;

	public ModeloGrafico() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		tamanho = tk.getScreenSize();
		bolhas = new ArrayList<>();
		ponteiros = new ArrayList<>();
	}

	public void secretBolha() {
		Image img = null;
		try {
			URL resource = Ponteiro.class.getResource("/resources/duds.png");
			img = ImageIO.read(resource);
		} catch (Exception e) {
			System.out.println("Image can't be loaded.");
		}

		for (int i = 0; i < bolhas.size(); i++) {
			bolhas.get(i).setImage(img);
		}
	}
	
	public void secretMeme() {
		Image img = null;
		try {
			URL resource = Ponteiro.class.getResource("/resources/meme.png");
			img = ImageIO.read(resource);
		} catch (Exception e) {
			System.out.println("Image can't be loaded.");
		}

		for (int i = 0; i < bolhas.size(); i++) {
			bolhas.get(i).setImage(img);
		}
	}

	public void addPonteiro(Ponteiro p) {
		ponteiros.add(p);
	}

	public List<Ponteiro> getPonteiros() {
		return ponteiros;
	}

	public void startThreads() {
		for (int i = 0; i < ponteiros.size(); i++) {
			// chama os eventos a serem executados de forma assincrona
			ponteiros.get(i).start();
		}
		for (int i = 0; i < bolhas.size(); i++) {
			// chama os eventos a serem executados de forma assincrona
			bolhas.get(i).start();
		}
	}

	public void stopThreads() {
		for (int i = 0; i < ponteiros.size(); i++) {
			ponteiros.get(i).stop();
		}
		for (int i = 0; i < bolhas.size(); i++) {
			bolhas.get(i).stop();
		}
	}

	public void add(Bolha b) {
		bolhas.add(b);
	}

	public void remove(int id) {
		bolhas.remove(id);
	}

	public void setSize(int width, int height) {
		tamanho = new Dimension(width, height);
	}

	public int getWidth() {
		return tamanho.width;
	}

	public int getHeight() {
		return tamanho.height;
	}

	public Dimension getDimension() {
		return tamanho;
	}

	public List<Bolha> getBolhas() {
		return bolhas;
	}

	public void setBolha(Bolha b, int idB) {
		bolhas.set(idB, b);
	}

	public void returnImage() {
		// TODO Auto-generated method stub
		for (int i = 0; i < bolhas.size(); i++) {
			bolhas.get(i).returnImage();
		}
	}

	public void startMonitor() {
		// /cria monitor que verifica evita que uma bolha entre dentro da outra
		m = new Monitor(this);
		m.start();
	}
	
	public void stopMonitor(){
		m.stop();
	}
}
