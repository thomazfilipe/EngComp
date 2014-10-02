package Serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import net.TCPClient;

public class SerialCom {

	private OutputStream saida;
	private InputStream entrada;
	private int baud;
	private String com;
	private TCPClient tcpClient;
	private SerialRecive r;

	public SerialCom(String com, int baud, TCPClient tcpClient) {
		this.com = com;
		this.baud = baud;
		this.tcpClient = tcpClient;
		open();
	}

	private void open() {
		// tenta abrir a porta
		try {
			CommPortIdentifier idPorta = null;
			try {
				idPorta = CommPortIdentifier.getPortIdentifier(com);
			} catch (NoSuchPortException e) {
				// se não tem a porta especificada
				JOptionPane.showMessageDialog(null,
						"A porta especificada não foi encontrada.", "Erro de porta",
						JOptionPane.ERROR_MESSAGE);
			}
			// abrindo a porta
			SerialPort porta = (SerialPort) idPorta.open("Comunicação serial",
					baud);
			saida = porta.getOutputStream();
			entrada = porta.getInputStream();
			
			// cria objeto monitor da serial para receber dados 
			SerialRecive r = new SerialRecive(entrada, saida, tcpClient);
			r.start();
			
			porta.setSerialPortParams(baud, // velocidade dos dados
					SerialPort.DATABITS_8, // taxa de 10 bits 8(enviar)
					SerialPort.STOPBITS_1, // taxa de 10 bits 1(recebimento)
					SerialPort.PARITY_NONE);// recebe e envia dados
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao tentar se conectar a porta especificada: "+ e.getMessage(),
					"Erro de comunicação", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void close() {
		try {
			r.stop();
			saida.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel fechar a porta", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void send(byte[] dados) {
		try {
			//envia até 30 bytes
			if(dados.length <= 30)
				saida.write(dados, 0, dados.length);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao enviar dados", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void send(String dados) {
		PrintStream ps = new PrintStream(saida);
		ps.print(dados);
	}
}
