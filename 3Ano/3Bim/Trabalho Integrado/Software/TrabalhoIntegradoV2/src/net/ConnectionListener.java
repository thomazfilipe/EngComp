package net;

public interface ConnectionListener {
	//interface para o m�todo de recep��o de conex�o
	void onConnection(TCPClient client);
}
