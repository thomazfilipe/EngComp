package bolha;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import Tools.ThreadAux;

public class Bolha implements Runnable {

	// configurações da bolha
	@SuppressWarnings("unused")
	private double distancia = 0.0;
	private Point centro;
	private int raio;
	private int id;
	private final int delayRead = 10;
	// configurações de colisão
	@SuppressWarnings("unused")
	private boolean colidiu = false;
	private double limite = 100; // aumenta a distancia que detecta a
									// aproximação
	// configurações de movimento
	private int maxVelocidade = 50;
	private int vX = 0;
	private int vY = 0;
	private int aceleracao = 2;// 1 pixel
	// vezes que executa a velocidade antes de reduzir
	private int time = 5;// fixo
	private int timeAux = 5;// altera decrementando
	private boolean finalize;
	private Image image;
	private Image imagePrevious;
	private Thread t;

	// teste nova colisão
	private ModeloGrafico modelo;
	private List<Integer> listaColisoes = new ArrayList<>();

	public Bolha(int raio, Point pos, ModeloGrafico modelo) {
		try {
			URL resource = Ponteiro.class.getResource("/resources/buble.png");
			image = (Image) ImageIO.read(resource);
			imagePrevious = (Image) ImageIO.read(resource);
		} catch (Exception e) {
			System.out.println("Image can't be loaded.");
		}

		this.raio = raio;
		this.centro = pos;
		this.modelo = modelo;

	}

	public void setImage(Image ii) {
		if (ii != null) {
			imagePrevious = image;
			image = ii;
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public void setID(int id) {
		try {
			this.id = id;
			listaColisoes.remove(this.id);
		} catch (IndexOutOfBoundsException e) {
		} finally {
			listaColisoes.add(id);
		}
	}

	public int getID() {
		return id;
	}

	public Point getCentro() {
		return centro;
	}

	@Override
	public void run() {
		finalize = false;
		while (!finalize) {
			if (vX != 0 || vY != 0) {
				// se há velocidade, movimenta
				// criar um ponto temporario prevendo o movimento
				Point tmpPoint = new Point(centro.x + vX, centro.y + vY);
				List<Bolha> bolhas = modelo.getBolhas();
				// checar colisões com outras bolhas com o ponto temporario
				for (int i = 0; i < bolhas.size(); i++) {
					// se houver colisão, verificar se a bolha que colidiu já
					// não detectou esta colisão
					if (bolhas.get(i).checaColisao(tmpPoint, true)
							&& bolhas.get(i).getID() != id) {
						boolean detect = false;
						// se detectar que a outra bolha já executou a ação, não
						// faz nada
						// senão, transfere as velocidades desta bolha para a
						// bolha que colidiu e divide a metade desta
						for (int j = 0; j < bolhas.get(i).listaColisoes.size(); j++) {
							try {
								if (bolhas.get(i).listaColisoes.get(j) == id) {
									detect = true;
									break;
								}
							} catch (Exception e) {
							}
						}
						if (!detect) {
							bolhas.get(i).eventoColisao(true, this);
							bolhas.get(i).listaColisoes.add(id);
						}
					}
				}

				// /tratando para não deixar uma bolha entrar na outra
				boolean detect = false;
				for (int i = 0; i < bolhas.size(); i++) {
					// se houver colisão, verificar se a bolha que colidiu já
					// não detectou esta colisão
					if (bolhas.get(i).checaColisao(tmpPoint, true)
							&& bolhas.get(i).getID() != id) {
						detect = true;
						break;
					}
				}

				int x = centro.x;
				int y = centro.y;
				if (!detect) {
					x += vX;
					y += vY;
				}
				// ///////////////////////////////////////////////////////////

				// Verificando se bateu nos cantos da tela
				Toolkit tk = Toolkit.getDefaultToolkit();

				if (x < raio) {
					x = raio;
					vX *= -1;
				}
				if (x > tk.getScreenSize().width - raio) {
					x = tk.getScreenSize().width - raio;
					vX *= -1;
				}
				if (y < raio) {
					y = raio;
					vY *= -1;
				}
				if (y > tk.getScreenSize().height - raio) {
					y = tk.getScreenSize().height - raio;
					vY *= -1;
				}
				// /------

				// atualiza o centro e indica que já andou evitando a colisão
				centro = new Point(x, y);
				listaColisoes.clear();
				listaColisoes.add(id);

				// atualiza as velocidades
				colidiu = false;

				if (timeAux == 0) {
					timeAux = time;
					if (vX != 0)
						if (vX > 0)
							vX--;
						else
							vX++;
					if (vY != 0)
						if (vY > 0)
							vY--;
						else
							vY++;
				} else {
					timeAux--;
				}

			}
			colidiu = false;
			// /fora do if
			ThreadAux.Pausar(delayRead);
		}
		colidiu = false;
		finalize = true;

	}

	public void setCentro(Point centro) {
		this.centro = centro;

	}

	public void desenhar(Graphics g) {
		g.drawImage(image, centro.x - raio, centro.y - raio, 2 * raio,
				2 * raio, null);

		// g.drawString("C: " +centro.x + " | " + centro.y, centro.x - raio +
		// 10, centro.y);
		// g.drawString("B" +id , centro.x - raio + 10, centro.y - 20);
		// g.drawString("vX" +vX , centro.x - raio + 10, centro.y +20);
		// g.drawString("vY" +vY , centro.x - raio + 10, centro.y + 40);

	}

	public boolean checaColisao(Point p, boolean bolha) {
		double distancia = Math.ceil(Math.sqrt(Math.pow(centro.x - p.x, 2)
				+ Math.pow(centro.y - p.y, 2))
				- (bolha ? 2 : limite));
		this.distancia = distancia;
		int raioBolha = bolha ? 2 * raio : raio;

		if (distancia > raioBolha) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checaColisaoInter(Point p, boolean bolha) {
		double distancia = Math.ceil(Math.sqrt(Math.pow(centro.x - p.x, 2)
				+ Math.pow(centro.y - p.y, 2)));
		this.distancia = distancia;
		int raioBolha = bolha ? 2 * raio : raio;

		if (distancia >= raioBolha) {
			return false;
		} else {
			return true;
		}
	}

	public void eventoColisao(boolean bolha, Object target) {
		if (bolha) {
			Bolha tmp = (Bolha) target;
			// evento colisão bolha
			vX = tmp.vX;
			vY = tmp.vY;

			tmp.vX /= Math.floor(1.5 * (-1));
			tmp.vY /= Math.floor(1.5 * (-1));
		} else {
			Point tmp = (Point) target;
			// então é ponteiro
			if (tmp.x > centro.x
					&& (tmp.y > centro.y - raio * 0.3 && tmp.y < centro.y
							+ raio * 0.3)) {
				// apenas eixo X
				if (Math.abs(vX) < maxVelocidade) {
					if (tmp.x > centro.x)
						vX -= aceleracao;
					else
						vX += aceleracao;
				}
			} else if (tmp.y > centro.y
					&& (tmp.x > centro.x - raio * 0.3 && tmp.x < centro.x
							+ raio * 0.3)) {
				// apenas eixo Y
				if (Math.abs(vY) < maxVelocidade) {
					if (tmp.y > centro.y)
						vY -= aceleracao;
					else
						vY += aceleracao;
				}
			} else {
				// os dois eixos
				if (Math.abs(vX) < maxVelocidade) {
					if (tmp.x > centro.x)
						vX -= aceleracao;
					else
						vX += aceleracao;
				}

				if (Math.abs(vY) < maxVelocidade) {
					if (tmp.y > centro.y)
						vY -= aceleracao;
					else
						vY += aceleracao;
				}
			}
		}
		ThreadAux.Pausar(delayRead);
	}

	public void stop() {
		finalize = true;
	}

	public int getVX() {
		return vX;
	}

	public int getVY() {
		return vY;
	}

	public int getMaxVelocidade() {
		return maxVelocidade;
	}

	public int getRaio() {
		return raio;
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return t.isAlive();
	}

	public void returnImage() {
		// TODO Auto-generated method stub
		image = imagePrevious;
	}
}
