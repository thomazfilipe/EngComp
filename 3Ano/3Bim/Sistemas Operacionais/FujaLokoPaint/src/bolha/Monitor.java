package bolha;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;

import Tools.ThreadAux;

public class Monitor implements Runnable {
	
	private ModeloGrafico modelo;
	private Thread t;
	private boolean running;
	
	public Monitor(ModeloGrafico modelo){
		this.modelo = modelo;
		running = false;
	}
	
	public void start(){
		running = true;
		t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		running = false;
	}

	public void run() {
		List<Bolha> bolhas = modelo.getBolhas();
		Toolkit tk = Toolkit.getDefaultToolkit();
		while(running){			
			for(int i  = 0; i < bolhas.size(); i++){
				int x = bolhas.get(i).getCentro().x;
				int y = bolhas.get(i).getCentro().y;
				//verifica se a thread morreu
				if(!bolhas.get(i).isRunning())
					bolhas.get(i).start();
				//se houver colisão, verificar se a bolha que colidiu já não detectou esta colisão
				for(int j = 0; j < bolhas.size(); j++){
					if(bolhas.get(i).checaColisaoInter(bolhas.get(j).getCentro(), true) &&
							bolhas.get(i).getID() != bolhas.get(j).getID()){
						if(x < bolhas.get(j).getCentro().x &&
								x > bolhas.get(i).getRaio()){
							x -= bolhas.get(i).getRaio();
							//verifica se a nova posição não ultrapassa os limites, se sim assume o limite
							if(x < bolhas.get(i).getRaio()){
								x = bolhas.get(i).getRaio();
							}
							if(x > Toolkit.getDefaultToolkit().getScreenSize().width - bolhas.get(i).getRaio()){
								x = Toolkit.getDefaultToolkit().getScreenSize().width - bolhas.get(i).getRaio();
							}
						}
						if(x > bolhas.get(j).getCentro().x &&
								x < tk.getScreenSize().width - bolhas.get(i).getRaio()){
							x += bolhas.get(i).getRaio();
							//verifica se a nova posição não ultrapassa os limites, se sim assume o limite
							if(x < bolhas.get(i).getRaio()){
								x = bolhas.get(i).getRaio();
							}
							if(x > Toolkit.getDefaultToolkit().getScreenSize().width - bolhas.get(i).getRaio()){
								x = Toolkit.getDefaultToolkit().getScreenSize().width - bolhas.get(i).getRaio();
							}
						}
						
						if(y < bolhas.get(j).getCentro().y &&
								y > bolhas.get(i).getRaio()){
							y -= bolhas.get(i).getRaio();
							//verifica se a nova posição não ultrapassa os limites, se sim assume o limite
							if(y < bolhas.get(i).getRaio()){
								y = bolhas.get(i).getRaio();
							}
							if(y > Toolkit.getDefaultToolkit().getScreenSize().height - bolhas.get(i).getRaio()){
								y = Toolkit.getDefaultToolkit().getScreenSize().height - bolhas.get(i).getRaio();
							}
						}
						if(y > bolhas.get(j).getCentro().y &&
								y < tk.getScreenSize().height - bolhas.get(i).getRaio()){
							y += bolhas.get(i).getRaio();
							//verifica se a nova posição não ultrapassa os limites, se sim assume o limite
							if(y < bolhas.get(i).getRaio()){
								y = bolhas.get(i).getRaio();
							}
							if(y > Toolkit.getDefaultToolkit().getScreenSize().height - bolhas.get(i).getRaio()){
								y = Toolkit.getDefaultToolkit().getScreenSize().height - bolhas.get(i).getRaio();
							}
						}
					}
					if(!running)
						break;
				}
				if(!running)
					break;
				
				bolhas.get(i).setCentro(new Point(x, y));
			}		
			ThreadAux.Pausar(10);
		}
	}

}
