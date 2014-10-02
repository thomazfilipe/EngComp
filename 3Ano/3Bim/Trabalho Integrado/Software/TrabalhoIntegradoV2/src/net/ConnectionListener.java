package net;

public interface ConnectionListener {
	//interface para o método de recepção de conexão
	void onConnection(TCPClient client);
}
