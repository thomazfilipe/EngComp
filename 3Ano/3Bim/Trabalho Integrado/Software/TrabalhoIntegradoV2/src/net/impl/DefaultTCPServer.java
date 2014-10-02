package net.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

import net.ConnectionListener;
import net.Constants;
import net.TCPClient;
import net.TCPServer;
import tools.Worker;

public class DefaultTCPServer extends Worker implements TCPServer {
	private ServerSocket serverSocket;
	private ConnectionListener connectionListener;
	
	//cria o servidor com a porta informada
	public DefaultTCPServer(int port) throws Exception {
		serverSocket = new ServerSocket(port);	}
	
	//cria um objeto cliente para novas conexões
	protected TCPClient createTCPClient(Socket socket) throws Exception {
		return new DefaultTCPClient(socket);
	}
	
	//aceitando conexão
	public TCPClient accept() throws Exception {
		//seta o tempo limite para obter resposta do cliente antes de dar um TimeOutException
		serverSocket.setSoTimeout(Constants.ACCEPT_TIMEOUT);
		Socket socket = serverSocket.accept();		
		return createTCPClient(socket);
	}
	
	//seta o listener de escuta de novas conexões
	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	//retorna o listener atual de escuta de conexão
	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}
	
	//para a thead de escuta de conexões e encerra o servidor criado
	public void stop() {
		super.stop(true);
	}
	
	//verifica se o servidor está com a thread de conexão ativa
	public boolean isRun() {
		return isRunning();
	}
	
	//método que fica monitorando novas conexões
	public void run() {
		while (isRunning()) {
			try {
				//fica aguardando um cliente
				final TCPClient client = accept();
				//se possui um listener para criar conexão com o cliente então inicia a Thread
				if (connectionListener != null) {
					new Worker() {
						@Override
						public void run() {
							connectionListener.onConnection(client);	
						}
					}.start();					
				}
			} catch(SocketTimeoutException e) {
				// ignore
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null,
						"Erro ao criar conexão com o cliente: "+ e.getMessage(), 
						"Conexão", 
						JOptionPane.ERROR_MESSAGE);
				reset();
			}
		}
		
		//finaliza socket do servidor
		try {
			serverSocket.close();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao tentar finalizar conexão do servidor: " + e.getMessage(), 
					"Conexão", 
					JOptionPane.INFORMATION_MESSAGE);

		}
	}
}











