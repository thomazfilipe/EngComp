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
	
	//cria um objeto cliente para novas conex�es
	protected TCPClient createTCPClient(Socket socket) throws Exception {
		return new DefaultTCPClient(socket);
	}
	
	//aceitando conex�o
	public TCPClient accept() throws Exception {
		//seta o tempo limite para obter resposta do cliente antes de dar um TimeOutException
		serverSocket.setSoTimeout(Constants.ACCEPT_TIMEOUT);
		Socket socket = serverSocket.accept();		
		return createTCPClient(socket);
	}
	
	//seta o listener de escuta de novas conex�es
	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	//retorna o listener atual de escuta de conex�o
	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}
	
	//para a thead de escuta de conex�es e encerra o servidor criado
	public void stop() {
		super.stop(true);
	}
	
	//verifica se o servidor est� com a thread de conex�o ativa
	public boolean isRun() {
		return isRunning();
	}
	
	//m�todo que fica monitorando novas conex�es
	public void run() {
		while (isRunning()) {
			try {
				//fica aguardando um cliente
				final TCPClient client = accept();
				//se possui um listener para criar conex�o com o cliente ent�o inicia a Thread
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
						"Erro ao criar conex�o com o cliente: "+ e.getMessage(), 
						"Conex�o", 
						JOptionPane.ERROR_MESSAGE);
				reset();
			}
		}
		
		//finaliza socket do servidor
		try {
			serverSocket.close();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao tentar finalizar conex�o do servidor: " + e.getMessage(), 
					"Conex�o", 
					JOptionPane.INFORMATION_MESSAGE);

		}
	}
}











