package Serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JOptionPane;

import util.Worker;
import net.TCPClient;
import tools.ThreadAux;

public class SerialRecive extends Worker {
	private InputStream entrada;
	@SuppressWarnings("unused")
	private PrintStream saida;
	private TCPClient tcpClient;

	public SerialRecive(InputStream entrada, OutputStream saida, TCPClient tcpClient) {
		this.entrada = entrada;
		this.saida = new PrintStream(saida);
		this.tcpClient = tcpClient;
	}

	public void run() {
		while (isRunning()) {
			try {
				if( entrada.available() > 0){
					Scanner s = new Scanner(entrada);
					String dataS = s.nextLine();
					String[] quebra = dataS.split("[|]"); 
					if(!quebra[0].equals("#")  && quebra.length > 1){
						try {
							tcpClient.send(dataS.getBytes());
							System.out.println("Enviando ao supervisor: " + dataS);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,
									"Erro ao tentar enviar comando ao Supervisor: "+ e.getMessage(),
									"Erro de comunicação", 
									JOptionPane.ERROR_MESSAGE);
						}
					}
					s.close();
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Erro ao tentar ler a serial: "+ e.getMessage(),
						"Erro Serial", 
						JOptionPane.ERROR_MESSAGE);
			}
			ThreadAux.Pausar(10);
		}
	}
	
	public void stop(){
		super.stop(true);
	}
}
