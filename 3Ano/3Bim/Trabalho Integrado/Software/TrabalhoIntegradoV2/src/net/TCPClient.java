package net;

public interface TCPClient {
	void send(byte[] message) throws Exception;
	void setMessageListener(MessageListener messageListener);
	MessageListener getMessageListener();
	void start();
	void stop();
	boolean isConnected();
}
