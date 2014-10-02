import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Serial.SerialCom;
import bolha.Bolha;
import bolha.ModeloGrafico;
import bolha.Ponteiro;


public class LoadModelo4 {
	
	private ModeloGrafico modelo;
	private double volume = 0.4;
	private int totalDeBolhas = 50;
	private List<Bolha> bolhas = new ArrayList<>();
	
	public LoadModelo4(String nBolhas, 
			Image bolhaIcon, 
			Image p1Icon, boolean serial1, boolean pcControl1,
			Image p2Icon, boolean serial2, boolean pcControl2,
			String com, int baud){
		totalDeBolhas = Integer.parseInt(nBolhas);
		modelo = new ModeloGrafico();
		// ponteiro 1
				Ponteiro ponteiro = new Ponteiro(new Dimension(50, 50),
						new Point(0, 0), modelo);
				ponteiro.setKeyboard(pcControl1);
				ponteiro.setSerial(serial1);
				ponteiro.setID(1);
				ponteiro.setImage(p1Icon);		
				// adicionando ponteiros
				modelo.addPonteiro(ponteiro);
				if (p2Icon != null){
					// ponteiro 2
					Ponteiro ponteiro2 = new Ponteiro(new Dimension(50, 50),
							new Point(0, 0), modelo);
					ponteiro2.setKeyboard(pcControl2);
					ponteiro2.setSerial(serial2);
					ponteiro2.setID(2);
					ponteiro2.setImage(p2Icon);
					modelo.addPonteiro(ponteiro2);
				}
				Ponteiro ponteiro3 = new Ponteiro(new Dimension(50, 50),
						new Point(0, 0), modelo);
				ponteiro3.setKeyboard(true);
				ponteiro3.setSerial(false);
				ponteiro3.setID(3);
				ponteiro3.setImage(p1Icon);	
				Ponteiro ponteiro4 = new Ponteiro(new Dimension(50, 50),
						new Point(0, 0), modelo);
				ponteiro4.setKeyboard(false);
				ponteiro4.setSerial(false);
				ponteiro4.setID(4);
				ponteiro4.setImage(p1Icon);	
				modelo.addPonteiro(ponteiro3);
				modelo.addPonteiro(ponteiro4);
				
				Ponteiro ponteiro5 = new Ponteiro(new Dimension(50, 50),
						new Point(0, 0), modelo);
				ponteiro5.setKeyboard(false);
				ponteiro5.setSerial(true);
				ponteiro5.setID(5);
				ponteiro5.setImage(p1Icon);	
				modelo.addPonteiro(ponteiro5);

				if (serial1 || serial2) {
					SerialCom sc = new SerialCom(com, baud, modelo);
					sc.send("E");
				}

				// cria as bolhas
				Random r = new Random();
				Toolkit tk = Toolkit.getDefaultToolkit();

				for (int i = 0; i < totalDeBolhas; i++) {
					Point p;
					int tamanho = calcTamanhoBolha();
					while (true) {
						p = new Point(r.nextInt(tk.getScreenSize().width), r.nextInt(tk
								.getScreenSize().height));
						// se o ponto gerado a bolha não ficar fora da tela
						if (p.x <= tk.getScreenSize().width - tamanho
								&& p.y <= tk.getScreenSize().height - tamanho
								&& p.x >= tamanho && p.y >= tamanho
								&& validaPoint(p, tamanho)) {
							break;
						}
					}

					Bolha b = new Bolha(tamanho, p, modelo);
					b.setID(i);
					b.setImage(bolhaIcon);
					bolhas.add(b);
					modelo.add(b);
				}
				// ////////
				// inicia Threads dos ponteiros e bolhas
				modelo.startThreads();
				modelo.startMonitor();
	}
	
	public ModeloGrafico getModelo(){
		return modelo;
	}
	
	private boolean validaPoint(Point p, int tamanho) {
		Bolha tmp = new Bolha(tamanho, p, null);
		for (int i = 0; i < bolhas.size(); i++) {
			if (tmp.checaColisao(bolhas.get(i).getCentro(), true)) {
				return false;
			}
		}
		return true;
	}

	// cria um tamanho da bolha de acordo com a tela, atualmente 70%
	private int calcTamanhoBolha() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		double tamanho = Math.sqrt(((tk.getScreenSize().width * tk
				.getScreenSize().height) * volume) / totalDeBolhas) / 2;

		return (int) tamanho;
	}

}
