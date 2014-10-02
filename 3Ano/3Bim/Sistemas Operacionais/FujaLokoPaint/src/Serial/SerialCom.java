package Serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import bolha.ModeloGrafico;

public class SerialCom {

	private OutputStream saida;
	private InputStream entrada;
	private int baud;
	private String com;
	private ModeloGrafico modelo;

	public SerialCom(String com, int baud, ModeloGrafico modelo) {
		this.com = com;
		this.baud = baud;
		this.modelo = modelo;
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
						"A porta especificada não foi encontrada.", "Erro",
						JOptionPane.PLAIN_MESSAGE);
			}
			// abrindo a porta
			SerialPort porta = (SerialPort) idPorta.open("Comunicação serial",
					baud);
			saida = porta.getOutputStream();
			entrada = porta.getInputStream();
			
			// thread que recebe a resposta da serial
			SerialRecive r = new SerialRecive(entrada, saida, modelo);
			new Thread(r).start();
			
			porta.setSerialPortParams(baud, // velocidade dos dados
					SerialPort.DATABITS_8, // taxa de 10 bits 8(enviar)
					SerialPort.STOPBITS_1, // taxa de 10 bits 1(recebimento)
					SerialPort.PARITY_NONE);// recebe e envia dados
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			saida.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel fechar a porta", "Erro",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	public void send(byte[] dados) {
		try {
			saida.write(dados, 0, 30);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao enviar dados", "Erro",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	public void send(String dados) {
		PrintStream ps = new PrintStream(saida);
		ps.println(dados);

	}
}
