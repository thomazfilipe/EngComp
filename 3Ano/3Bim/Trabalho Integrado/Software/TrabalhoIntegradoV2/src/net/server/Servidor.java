package net.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import javax.swing.JOptionPane;

import database.Db;
import net.ConnectionListener;
import net.MessageListener;
import net.TCPClient;
import net.TCPServer;
import net.impl.SecureTCPServer;

public class Servidor extends Observable implements ConnectionListener,
		MessageListener {
	private TCPServer tcpServer;
	private TCPClient tcpClient;
	private Db conexao;

	// passa a conexão do banco de dados para o servidor
	public Servidor(Db conexao) {
		this.conexao = conexao;
	}

	// inicia um servidor seguro com a porta e com 1024bits
	public void start(int porta, int bits) throws Exception {
		tcpServer = new SecureTCPServer(porta, 1024);
		// adiciona o listener de conexão que esta neste objeto
		tcpServer.setConnectionListener(this);
		tcpServer.start();
	}

	public boolean isRunning() {
		return tcpServer == null ? false : tcpServer.isRun();
	}

	public void stop() {
		tcpServer.stop();
	}

	// Método do listener de conexão, assim que recebe um pedido de conexão
	// chama esse método
	public void onConnection(TCPClient client) {
		this.tcpClient = client;
		client.setMessageListener(this);
		client.start();
	}

	// Método do listener de menssagens recebidas, assim que recebe, chama esse
	// método
	public void onMessage(TCPClient client, byte[] message) {
		// /ao receber o pacote do Ponto quebra o pacote e guarda no banco de
		// dados
		String[] dados = new String(message).split("[|]");
		// /formata a data atual para o formato do banco de dados
		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		
		// verifica se os dados é do status desligado se sim ignora
		if (!dados[2].equals("0110")) {
			conexao.executar("INSERT INTO fluxo (quantidadea,quantidadeb,dataleitura) "
					+ "VALUES ('"
					+ dados[0]
					+ "','"
					+ dados[1]
					+ "','"
					+ data
					+ "')");
			setChanged();
			notifyObservers();
		}
	}

	// envia mensagem
	public void sendMessege(String data) {
		try {
			tcpClient.send(data.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro ao tentar se comunicar com o Ponto.");
		}
	}
}
