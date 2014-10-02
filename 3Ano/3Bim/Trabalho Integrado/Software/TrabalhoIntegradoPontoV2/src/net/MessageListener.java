package net;

public interface MessageListener {
	void onMessage(TCPClient client, byte[] message);
}
