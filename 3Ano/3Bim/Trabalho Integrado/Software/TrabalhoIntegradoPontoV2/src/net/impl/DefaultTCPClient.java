package net.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import Serial.SerialCom;
import net.Constants;
import net.MessageListener;
import net.TCPClient;
import util.Worker;

public class DefaultTCPClient extends Worker implements TCPClient {
	protected Socket socket;
	protected InputStream input;
	protected OutputStream output;	
	private MessageListener messageListener;
	private SerialCom sc;

	public DefaultTCPClient(String address, int port) throws Exception {
		this(new Socket(address, port));
	}
	
	public void setSerial(SerialCom sc){
		this.sc = sc;
	}

	public DefaultTCPClient(Socket socket) throws Exception {
		this.socket = socket;
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
	}

	@Override
	public void send(byte[] message) throws Exception {
		// TODO validar tamanho do buffer
		output.write(message);
	}
	
	protected byte[] receive(boolean wait) throws Exception {
		if (!wait) socket.setSoTimeout(Constants.READ_TIMEOUT);
		byte[] buffer = new byte[Constants.BUFFER_LENGTH];
		int length = input.read(buffer);
		if (length == -1) {
			throw new Exception("invalid data length");
		}
		return Arrays.copyOf(buffer, length);
	}

	@Override
	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}
	
	@Override
	public MessageListener getMessageListener() {
		return messageListener;
	}
	
	@Override
	public void stop() {
		super.stop(true);
	}

	@Override
	public void run() {
		while (isRunning()) {
			try {
				byte[] message = receive(false);
				if (messageListener != null) {
					messageListener.onMessage(this, message);
					System.out.println("O supervisor enviou: " + new String(message));
				}
			} catch(SocketTimeoutException e) {
				// ignore
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, "O supervisor encerrou a conexão com o PONTO");
				//manda comando para desligar a fpga
				sc.send("0110");
				//reset();
				System.exit(1);
			}
		}
		
		try {
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return isRunning();
	}
	
	
}








