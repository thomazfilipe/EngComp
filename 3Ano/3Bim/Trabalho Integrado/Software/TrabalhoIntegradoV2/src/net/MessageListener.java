package net;
//interface para escutar as mensagens enviadas pelos clientes
public interface MessageListener {
	void onMessage(TCPClient client, byte[] message);
}
