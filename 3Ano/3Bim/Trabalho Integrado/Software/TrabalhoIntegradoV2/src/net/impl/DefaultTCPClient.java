package net.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import net.Constants;
import net.MessageListener;
import net.TCPClient;
import tools.Worker;

public class DefaultTCPClient extends Worker implements TCPClient {
	protected Socket socket;
	protected InputStream input;
	protected OutputStream output;	
	private MessageListener messageListener;

	//cria uma conexão
	public DefaultTCPClient(String address, int port) throws Exception {
		this(new Socket(address, port));
	}

	public DefaultTCPClient(Socket socket) throws Exception {
		this.socket = socket;
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
	}

	//envia bytes
	public void send(byte[] message) throws Exception {
		// TODO validar tamanho do buffer
		output.write(message);
	}
	
	//recebe bytes
	protected byte[] receive(boolean wait) throws Exception {
		if (!wait) socket.setSoTimeout(Constants.READ_TIMEOUT);
		byte[] buffer = new byte[Constants.BUFFER_LENGTH];
		int length = input.read(buffer);
		if (length == -1) {
			throw new Exception("invalid data length");
		}
		return Arrays.copyOf(buffer, length);
	}

	//seta o método de escuta de mensagens
	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}
	
	//retorna o listener de escuta das mensagens
	public MessageListener getMessageListener() {
		return messageListener;
	}
	
	//para de escutar as mensagens e encerra a conexão
	public void stop() {
		super.stop(true);
	}

	//evento da Thread que fica escutando as mensagens enviadas
	public void run() {
		while (isRunning()) {
			try {
				byte[] message = receive(false);
				if (messageListener != null) {
					messageListener.onMessage(this, message);
				}
			} catch(SocketTimeoutException e) {
				// ignore
			} catch(Exception e) {//se deu erro de timeout reseta o socket ara uma nova conexão
				JOptionPane.showMessageDialog(null, "A conexão com o Ponto foi encerrada!", "Conexão", JOptionPane.INFORMATION_MESSAGE);
				reset();
			}
		}
		
		///finaliza a conexão antes de encerra a thread
		try {
			socket.close();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Não foi possivel encerrar a conexão: "+ e.getMessage(), "Conexão", JOptionPane.ERROR_MESSAGE);
		}
	}

	//verifica se a thead de coneção esta ativa
	public boolean isConnected() {
		return isRunning();
	}
}








