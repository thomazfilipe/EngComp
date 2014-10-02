package Serial;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import Tools.ThreadAux;
import bolha.ModeloGrafico;
import bolha.Ponteiro;

public class SerialRecive implements Runnable {
	private InputStream entrada;
	private ModeloGrafico modelo;
	private PrintStream saida;

	public SerialRecive(InputStream entrada, OutputStream saida,
			ModeloGrafico modelo) {
		this.entrada = entrada;
		this.modelo = modelo;
		this.saida = new PrintStream(saida);
	}

	public void run() {
		while (true) {
			try {
				if( entrada.available() > 0){
					Scanner s = new Scanner(entrada);
					String dataS = s.nextLine();
					String[] quebra = dataS.split("[|]"); 
					atualizarPos(quebra[0], quebra[1], quebra[2]);
					s.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ThreadAux.Pausar(10);
		}
	}

	private void atualizarPos(String id, String xS, String yS) {
		List<Ponteiro> ponteiros = modelo.getPonteiros();

		for (int i = 0; i < ponteiros.size(); i++) {
			// /se o ponteiro em questão esta configurado para serial
			if (ponteiros.get(i).isSerial()
					&& ponteiros.get(i).getID() == Integer.parseInt(id)) {
				// divide o intervalo do joystick pela velocidade do ponteiro
				// para
				// simular aceleração
				int movimento = 1023 / ponteiros.get(i).getVelocidadeSerial();
				// converte o valor lido em um valor para movimento
				int velX = Integer.parseInt(xS) / movimento;
				int velY = Integer.parseInt(yS) / movimento;

				int x = ponteiros.get(i).getPosicao().x;
				int y = ponteiros.get(i).getPosicao().y;
				if (ponteiros.get(i).getPosicao().y >= 0
						&& ponteiros.get(i).getPosicao().y <= Toolkit
								.getDefaultToolkit().getScreenSize().height){
					y += velY;
					if(y < 0){
						y = 0;
					}
					if(y > Toolkit.getDefaultToolkit().getScreenSize().height){
						y = Toolkit.getDefaultToolkit().getScreenSize().height;
					}
				}
			

				if (ponteiros.get(i).getPosicao().x >= 0
						&& ponteiros.get(i).getPosicao().x <= Toolkit
								.getDefaultToolkit().getScreenSize().width){
					x += velX;
					if(x < 0){
						x = 0;
					}
					if(x > Toolkit.getDefaultToolkit().getScreenSize().width){
						x = Toolkit.getDefaultToolkit().getScreenSize().width;
					}
				}

				// /verifica se pode mover
				boolean move = true;
				for (int j = 0; j < modelo.getBolhas().size(); j++) {
					if (modelo.getBolhas().get(j)
							.checaColisaoInter(new Point(x, y), false)) {
						move = false;
						break;
					}
				}
				if (move)
					ponteiros.get(i).setPosicao(new Point(x, y));
				
				//confirma que está tudo ok
				saida.print(ponteiros.get(i).getID());
			}

		}
	}

}
